package br.com.wacoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Barbeador extends Activity {

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

    public static float banheiro_tbarb;

    public static float banheiro_qbarb;

    public static float banheiro_pbarb;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barbeador);
        Button BARBVoltar = (Button) findViewById(R.id.BTBarbeadorVoltar);
        BARBVoltar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText banheiro_tempobarb = (EditText) findViewById(R.id.EDITBarbeadorTempo);
                EditText banheiro_quantbarb = (EditText) findViewById(R.id.EDITBarbeadorQuant);
                EditText banheiro_potbarb = (EditText) findViewById(R.id.EDITBarbeadorPotencia);
                if (banheiro_tempobarb.getText() == null || banheiro_tempobarb.getText().length() == 0 || banheiro_tempobarb.getText().equals("0")) {
                    banheiro_tempobarb.setText("0");
                } else {
                }
                if (banheiro_quantbarb.getText() == null || banheiro_quantbarb.getText().length() == 0 || banheiro_quantbarb.getText().equals("0")) {
                    banheiro_quantbarb.setText("0");
                } else {
                }
                if (banheiro_potbarb.getText() == null || banheiro_potbarb.getText().length() == 0 || banheiro_potbarb.getText().equals("0")) {
                    banheiro_potbarb.setText("0");
                } else {
                }
                banheiro_tbarb = Float.parseFloat(banheiro_tempobarb.getText().toString());
                banheiro_qbarb = Float.parseFloat(banheiro_quantbarb.getText().toString());
                banheiro_pbarb = Float.parseFloat(banheiro_potbarb.getText().toString());
                finish();
            }
        });
        Button BarbLimpar = (Button) findViewById(R.id.BTBarbeadorLimpar);
        BarbLimpar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText banheiro_tempobarb = (EditText) findViewById(R.id.EDITBarbeadorTempo);
                EditText banheiro_quantbarb = (EditText) findViewById(R.id.EDITBarbeadorQuant);
                EditText banheiro_potbarb = (EditText) findViewById(R.id.EDITBarbeadorPotencia);
                banheiro_tempobarb.setText("");
                banheiro_quantbarb.setText("");
                banheiro_potbarb.setText("");
            }
        });
        Button BarbSug = (Button) findViewById(R.id.BTSugestaoBarbeador);
        BarbSug.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText banheiro_tempobarb = (EditText) findViewById(R.id.EDITBarbeadorTempo);
                EditText banheiro_quantbarb = (EditText) findViewById(R.id.EDITBarbeadorQuant);
                EditText banheiro_potbarb = (EditText) findViewById(R.id.EDITBarbeadorPotencia);
                banheiro_tempobarb.setText("0.3");
                banheiro_quantbarb.setText("1");
                banheiro_potbarb.setText("10");
            }
        });
    }
}
