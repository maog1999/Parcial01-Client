package com.example.parcial01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button btnVerde,btnAmarillo,btnRojo,btnVista,btnConfirmar;
    private EditText posX,posY,txtRecordatorio;
    private BufferedWriter bWriter;
    private boolean bVerde=false, bAmarillo=false, bRojo=false, undio = false;
    private String color;
    private String col,posX2,posY2,text,confir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Referenciar
        btnVerde = findViewById(R.id.btnVerde);
        btnAmarillo = findViewById(R.id.btnAmarillo);
        btnRojo = findViewById(R.id.btnRojo);
        btnVista = findViewById(R.id.btnVista);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        posX = findViewById(R.id.posX);
        posY = findViewById(R.id.posY);
        txtRecordatorio = findViewById(R.id.txtRecordatorio);


        //PONER LO DEL INTERNET 


        //Hilo Socket
        new Thread(
                ()->{
                    Socket socket = null;
                    try {
                        socket = new Socket("10.0.2.2",5000);
                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        bWriter = new BufferedWriter(osw);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        ).start();

        //Logica botones
        btnVerde.setOnClickListener(
                (v)->{
                    bVerde = true;
                    bRojo = false;
                    bAmarillo = false;

                    if(bVerde){
                        color = "verde";
                        undio = true;

                    }

                }
        );

        btnAmarillo.setOnClickListener(
                (v)->{
                    bAmarillo = true;
                    bVerde = false;
                    bRojo = false;

                    if(bAmarillo){
                        color = "amarillo";
                        undio = true;

                    }

                }
        );

        btnRojo.setOnClickListener(
                (v)->{
                    bRojo = true;
                    bVerde = false;
                    bAmarillo = false;

                    if(bRojo){
                        color = "rojo";
                        undio = true;

                    }
                }
        );

        btnVista.setOnClickListener(
                (v)->{
                    boolean checkeo =  posX.getText().toString().isEmpty() || posY.getText().toString().isEmpty() || txtRecordatorio.getText().toString().isEmpty();
                    Log.e("-->",""+checkeo);
                    Log.e("----->",""+undio);

                    if(checkeo == true) {
                        Toast mensaje = Toast.makeText(this, "Completar todos los datos", Toast.LENGTH_LONG);
                        mensaje.show();
                    }else {
                        posX2 = posX.getText().toString();
                        posY2 = posY.getText().toString();
                        text = txtRecordatorio.getText().toString();
                        confir = "vista";
                        recordatorioJson(color, posX2, posY2, text, confir);
                    }

                }
        );

        btnConfirmar.setOnClickListener(
                (v)->{
                    boolean checkeo =  posX.getText().toString().isEmpty() || posY.getText().toString().isEmpty() || txtRecordatorio.getText().toString().isEmpty();
                    if(checkeo == true) {
                        Toast mensaje = Toast.makeText(this, "Completar todos los datos", Toast.LENGTH_LONG);
                        mensaje.show();
                    }if(checkeo == false) {
                        posX2 = posX.getText().toString();
                        posY2 = posY.getText().toString();
                        text = txtRecordatorio.getText().toString();
                        confir = "confirmar";
                        recordatorioJson(color,posX2,posY2,text,confir);
                        posX.getText().clear();
                        posY.getText().clear();
                        txtRecordatorio.getText().clear();
                    }

                }
        );

    }

    public void recordatorioJson(String color, String posX, String posY, String texto, String confirmar){

        Gson gson = new Gson();
        Recordatorio info = new Recordatorio(color, posX, posY, texto, confirmar);

        //Serializacion
        String recordatorioStr = gson.toJson(info);
        new Thread(
                ()-> {

                    try {
                        bWriter.write(recordatorioStr + "\n");
                        bWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();


    }
}