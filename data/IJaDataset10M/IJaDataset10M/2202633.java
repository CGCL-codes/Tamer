package time.burrito.menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Marcadores extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marcadores);
        ((Button) findViewById(R.id.btnRegresarMarcadores)).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                finish();
                Intent intencion = new Intent(Marcadores.this, MenuPrincipal.class);
                startActivity(intencion);
            }
        });
        TextView tvScoreN1 = (TextView) findViewById(R.id.scoreN1);
        TextView tvScoreN2 = (TextView) findViewById(R.id.scoreN2);
        TextView tvScoreN3 = (TextView) findViewById(R.id.scoreN3);
        TextView tvUserN1 = (TextView) findViewById(R.id.userN1);
        TextView tvUserN2 = (TextView) findViewById(R.id.userN2);
        TextView tvUserN3 = (TextView) findViewById(R.id.userN3);
        SharedPreferences prefs = getSharedPreferences("marcadores", Context.MODE_PRIVATE);
        String nombreDefault = "AAA";
        int scoreN1 = prefs.getInt("scoreN1", 0);
        int scoreN2 = prefs.getInt("scoreN2", 0);
        int scoreN3 = prefs.getInt("scoreN3", 0);
        String userN1 = prefs.getString("userN1", nombreDefault);
        String userN2 = prefs.getString("userN2", nombreDefault);
        String userN3 = prefs.getString("userN3", nombreDefault);
        tvScoreN1.setText(scoreN1 + "");
        tvUserN1.setText(userN1);
        tvScoreN2.setText(scoreN2 + "");
        tvUserN2.setText(userN2);
        tvScoreN3.setText(scoreN3 + "");
        tvUserN3.setText(userN3);
    }
}
