package time.burrito.menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class Opciones extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones);
        ((Button) findViewById(R.id.btnRegresarOpciones)).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                finish();
                Intent intencion = new Intent(Opciones.this, MenuPrincipal.class);
                startActivity(intencion);
            }
        });
        if (((ToggleButton) findViewById(R.id.btnSonido)).isChecked()) {
            SharedPreferences prefs = getSharedPreferences("sonido", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sonido", true);
            editor.commit();
        } else {
            SharedPreferences prefs = getSharedPreferences("sonido", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sonido", false);
            editor.commit();
        }
    }
}
