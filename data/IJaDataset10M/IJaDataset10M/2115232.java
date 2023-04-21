package com.projetoptymo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Activity qui affiche la position actuelle de l'utilisateur
 * Les coordonn�es GPS sont affich�s.
 * @version 0.1
 */
public class PositionActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.positionlayout);
        ImageView btnRetour = (ImageView) findViewById(R.id.retMenuPos01);
        btnRetour.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }
}
