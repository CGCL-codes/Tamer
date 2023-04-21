package com.projetoptymo;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

/**
 * Activity qui d�crit textuellement l'itin�raire.
 * Il prend en param�tres 2 num�ros d'arrets
 * Lance le calcul et affiche le r�sultat
 * @version 0.3
 */
public class RouteActivity extends MapActivity {

    BubbleOverlay bubb;

    private int NumeroArretDepart;

    private int NumeroArretArrivee;

    private int Hour;

    private int Minute;

    private int Day;

    private int Month;

    private int Year;

    private RouteActivity me;

    private TextView loadingText;

    private Drawable iconesligneUp[];

    private Drawable iconesligneDown[];

    final Handler uiThreadCallback = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadinglayout);
        me = this;
        final Runnable runInUIThread = new Runnable() {

            public void run() {
                affiche_resultat();
            }
        };
        loadingText = (TextView) findViewById(R.id.LoadingTextView);
        iconesligneUp = new Drawable[7];
        iconesligneUp[0] = getResources().getDrawable(R.drawable.ligne1up);
        iconesligneUp[1] = getResources().getDrawable(R.drawable.ligne2up);
        iconesligneUp[2] = getResources().getDrawable(R.drawable.ligne3up);
        iconesligneUp[3] = getResources().getDrawable(R.drawable.ligne4up);
        iconesligneUp[4] = getResources().getDrawable(R.drawable.ligne5up);
        iconesligneUp[5] = getResources().getDrawable(R.drawable.ligne6up);
        iconesligneUp[6] = getResources().getDrawable(R.drawable.ligne7up);
        iconesligneDown = new Drawable[7];
        iconesligneDown[0] = getResources().getDrawable(R.drawable.ligne1down);
        iconesligneDown[1] = getResources().getDrawable(R.drawable.ligne2down);
        iconesligneDown[2] = getResources().getDrawable(R.drawable.ligne3down);
        iconesligneDown[3] = getResources().getDrawable(R.drawable.ligne4down);
        iconesligneDown[4] = getResources().getDrawable(R.drawable.ligne5down);
        iconesligneDown[5] = getResources().getDrawable(R.drawable.ligne6down);
        iconesligneDown[6] = getResources().getDrawable(R.drawable.ligne7down);
        Bundle objetbunble = this.getIntent().getExtras();
        NumeroArretDepart = Integer.parseInt(objetbunble.getString("DepartNo"));
        NumeroArretArrivee = Integer.parseInt(objetbunble.getString("ArriveeNo"));
        Minute = Integer.parseInt(objetbunble.getString("Minute"));
        Hour = Integer.parseInt(objetbunble.getString("Hour"));
        Day = Integer.parseInt(objetbunble.getString("Day"));
        Month = Integer.parseInt(objetbunble.getString("Month"));
        Year = Integer.parseInt(objetbunble.getString("Year"));
        new Thread() {

            @Override
            public void run() {
                CalculItineraire ci = new CalculItineraire(me, NumeroArretDepart, NumeroArretArrivee, Minute, Hour, Day, Month, Year);
                loadingText.setText("T�l�chargement des donn�es de r�sultat ...");
                ci.Download();
                if (ci.ParseResult()) {
                    bubb = new BubbleOverlay(me, "Informations Trajet. dur�e : " + ci.getItineraireDuree() + " min", 10);
                    int nbEtapes = ci.getEtapeCount();
                    for (int i = 0; i < nbEtapes; i++) {
                        String texte = ci.getEtapeActionAt(i);
                        texte += " � l'arret : ";
                        texte += ci.getEtapeArretAt(i);
                        if (ci.getEtapeLigneAt(i) < 1 || ci.getEtapeLigneAt(i) > 6) bubb.addItem(new BubbleItem(texte + ", ligne " + ci.getEtapeLigneAt(i), ci.getEtapeHeureAt(i), null)); else if ("Monter".equals(ci.getEtapeActionAt(i))) bubb.addItem(new BubbleItem(texte, ci.getEtapeHeureAt(i), iconesligneUp[ci.getEtapeLigneAt(i) - 1])); else bubb.addItem(new BubbleItem(texte, ci.getEtapeHeureAt(i), iconesligneDown[ci.getEtapeLigneAt(i) - 1]));
                    }
                } else {
                    bubb = new BubbleOverlay(me, "Aucune correspondance trouv�e", 10);
                }
                uiThreadCallback.post(runInUIThread);
            }
        }.start();
    }

    /**
	 * Affiche le r�sultat dans le layout
	 */
    public void affiche_resultat() {
        setContentView(R.layout.routelayout);
        LinearLayout layout = (LinearLayout) findViewById(R.id.RouteLayout01);
        MapView mv = (MapView) findViewById(R.id.MapView01);
        layout.removeView(mv);
        layout.addView(bubb);
    }

    protected boolean isRouteDisplayed() {
        return false;
    }
}
