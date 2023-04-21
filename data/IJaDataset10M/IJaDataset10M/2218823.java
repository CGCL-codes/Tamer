package br.com.wacoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Inicio extends Activity {

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

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        Button BTChamaSala = (Button) findViewById(R.id.BTChamaSala);
        BTChamaSala.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(Inicio.this, Sala.class);
                startActivity(i);
            }
        });
        Button BTChamaQuarto = (Button) findViewById(R.id.BTChamaQuarto);
        BTChamaQuarto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(Inicio.this, Quarto.class);
                startActivity(i);
            }
        });
        Button BTChamaCozinha = (Button) findViewById(R.id.BTChamaCozinha);
        BTChamaCozinha.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(Inicio.this, Cozinha.class);
                startActivity(i);
            }
        });
        Button BTChamaBanheiro = (Button) findViewById(R.id.BTChamaBanheiro);
        BTChamaBanheiro.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(Inicio.this, Banheiro.class);
                startActivity(i);
            }
        });
    }
}
