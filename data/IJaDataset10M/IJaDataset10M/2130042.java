package lpd2i.ApplicationAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activit� principale de l'application de gestion de mat�riel
 * 
 * @author melanie.marc
 */
public class Accueil extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /**
	 * Permet de charger une nouvelle configuration
	 * � partir d'une liste des config accesible 
	 * sur un dossier sp�cifique du t�l�phone
	 * 
	 * @param v no�n utilis�
	 */
    public void onValidationCharger(View v) {
        Intent intention = new Intent(Accueil.this, ChargementConfiguration.class);
        Bundle bundleObject = new Bundle();
        intention.putExtras(bundleObject);
        startActivity(intention);
    }

    /**
	 * Permet de charger l'application avec la dernierre configuration
	 * lors du clique sur le bouton charger
	 * 
	 * @param v non utilis�
	 */
    public void onValidationLancer(View v) {
        Intent intention = new Intent(Accueil.this, DetectionBatiments.class);
        Bundle bundleObject = new Bundle();
        intention.putExtras(bundleObject);
        startActivity(intention);
    }

    /**
	 * Permet de quitter l'application
	 *
	 * @param v Non utilis�
	 */
    public void onValidationQuitter(View v) {
        AlertDialog.Builder alertBuild = new AlertDialog.Builder(this);
        alertBuild.setMessage("Voulez vous vraiment quitter?").setCancelable(false);
        alertBuild.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        alertBuild.setNegativeButton("NON", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertBuild.create();
        alert.show();
    }
}
