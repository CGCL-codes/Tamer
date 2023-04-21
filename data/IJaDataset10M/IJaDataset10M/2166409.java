package br.com.wacoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Geladeira extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.inicio:
                Intent i = new Intent(this, Inicio.class);
                startActivity(i);
                return true;
            case R.id.comodos:
                Intent i2 = new Intent(this, Inicio.class);
                startActivity(i2);
                return true;
            case R.id.configs:
                Intent i3 = new Intent(this, Config.class);
                startActivity(i3);
                return true;
            case R.id.ajuda:
                Intent i4 = new Intent(this, Ajuda.class);
                startActivity(i4);
                return true;
            case R.id.calculos:
                Intent i5 = new Intent(this, Calculos.class);
                startActivity(i5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static float cozinha_tgela;

    public static float cozinha_qgela;

    public static float cozinha_pgela;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geladeira);
        Button GELAVoltar = (Button) findViewById(R.id.BTGeladeiraVoltar);
        GELAVoltar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText cozinha_tempogela = (EditText) findViewById(R.id.EDITGeladeiraTempo);
                EditText cozinha_quantgela = (EditText) findViewById(R.id.EDITGeladeiraQuant);
                EditText cozinha_potgela = (EditText) findViewById(R.id.EDITGeladeiraPotencia);
                if (cozinha_tempogela.getText() == null || cozinha_tempogela.getText().length() == 0 || cozinha_tempogela.getText().equals("0")) {
                    cozinha_tempogela.setText("0");
                } else {
                }
                if (cozinha_quantgela.getText() == null || cozinha_quantgela.getText().length() == 0 || cozinha_quantgela.getText().equals("0")) {
                    cozinha_quantgela.setText("0");
                } else {
                }
                if (cozinha_potgela.getText() == null || cozinha_potgela.getText().length() == 0 || cozinha_potgela.getText().equals("0")) {
                    cozinha_potgela.setText("0");
                } else {
                }
                cozinha_tgela = Float.parseFloat(cozinha_tempogela.getText().toString());
                cozinha_qgela = Float.parseFloat(cozinha_quantgela.getText().toString());
                cozinha_pgela = Float.parseFloat(cozinha_potgela.getText().toString());
                finish();
            }
        });
        Button GelaLimpar = (Button) findViewById(R.id.BTGeladeiraLimpar);
        GelaLimpar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText cozinha_tempogela = (EditText) findViewById(R.id.EDITGeladeiraTempo);
                EditText cozinha_quantgela = (EditText) findViewById(R.id.EDITGeladeiraQuant);
                EditText cozinha_potgela = (EditText) findViewById(R.id.EDITGeladeiraPotencia);
                cozinha_tempogela.setText("");
                cozinha_quantgela.setText("");
                cozinha_potgela.setText("");
            }
        });
        Button GelaSug = (Button) findViewById(R.id.BTSugestaoGeladeira);
        GelaSug.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText cozinha_tempogela = (EditText) findViewById(R.id.EDITGeladeiraTempo);
                EditText cozinha_quantgela = (EditText) findViewById(R.id.EDITGeladeiraQuant);
                EditText cozinha_potgela = (EditText) findViewById(R.id.EDITGeladeiraPotencia);
                cozinha_tempogela.setText("24");
                cozinha_quantgela.setText("1");
                cozinha_potgela.setText("130");
            }
        });
    }
}
