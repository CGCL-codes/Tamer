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

public class Batedeira extends Activity {

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

    public static float cozinha_tbate;

    public static float cozinha_qbate;

    public static float cozinha_pbate;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.batedeira);
        Button BATVoltar = (Button) findViewById(R.id.BTBatedeiraVoltar);
        BATVoltar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText cozinha_tempobate = (EditText) findViewById(R.id.EDITBatedeiraTempo);
                EditText cozinha_quantbate = (EditText) findViewById(R.id.EDITBatedeiraQuant);
                EditText cozinha_potbate = (EditText) findViewById(R.id.EDITBatedeiraPotencia);
                if (cozinha_tempobate.getText() == null || cozinha_tempobate.getText().length() == 0 || cozinha_tempobate.getText().equals("0")) {
                    cozinha_tempobate.setText("0");
                } else {
                }
                if (cozinha_quantbate.getText() == null || cozinha_quantbate.getText().length() == 0 || cozinha_quantbate.getText().equals("0")) {
                    cozinha_quantbate.setText("0");
                } else {
                }
                if (cozinha_potbate.getText() == null || cozinha_potbate.getText().length() == 0 || cozinha_potbate.getText().equals("0")) {
                    cozinha_potbate.setText("0");
                } else {
                }
                cozinha_tbate = Float.parseFloat(cozinha_tempobate.getText().toString());
                cozinha_qbate = Float.parseFloat(cozinha_quantbate.getText().toString());
                cozinha_pbate = Float.parseFloat(cozinha_potbate.getText().toString());
                finish();
            }
        });
        Button BateLimpar = (Button) findViewById(R.id.BTBatedeiraLimpar);
        BateLimpar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText cozinha_tempobate = (EditText) findViewById(R.id.EDITBatedeiraTempo);
                EditText cozinha_quantbate = (EditText) findViewById(R.id.EDITBatedeiraQuant);
                EditText cozinha_potbate = (EditText) findViewById(R.id.EDITBatedeiraPotencia);
                cozinha_tempobate.setText("");
                cozinha_quantbate.setText("");
                cozinha_potbate.setText("");
            }
        });
        Button BateSug = (Button) findViewById(R.id.BTSugestaoBatedeira);
        BateSug.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText cozinha_tempobate = (EditText) findViewById(R.id.EDITBatedeiraTempo);
                EditText cozinha_quantbate = (EditText) findViewById(R.id.EDITBatedeiraQuant);
                EditText cozinha_potbate = (EditText) findViewById(R.id.EDITBatedeiraPotencia);
                cozinha_tempobate.setText("0.3");
                cozinha_quantbate.setText("1");
                cozinha_potbate.setText("200");
            }
        });
    }
}
