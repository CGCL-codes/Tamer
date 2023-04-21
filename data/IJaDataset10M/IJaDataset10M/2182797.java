package com.cwb3.moto.aankondigingencontrollerview;

import java.util.Calendar;
import java.util.TreeSet;
import com.cwb3.moto.R;
import com.cwb3.moto.domeinmodel.Aankondiging;
import com.cwb3.moto.domeinmodel.Gebruiker;
import com.cwb3.moto.aankondigingencontrollerview.AankondigingAdapter;
import com.cwb3.moto.aankondigingencontrollerviewOLD.AankondigingenController;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AankondigingView extends Activity {

    private Aankondiging aankondiging = AankondigingController.geefAankondigingenController().geefTijdelijkeAankondiging();

    private Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aankondiging_aankondiging_groot);
        TextView tekst = (TextView) findViewById(R.id.aankondingtekstgr);
        TextView titel = (TextView) findViewById(R.id.aankondingbeschrijvinggr);
        TextView datum = (TextView) findViewById(R.id.aankondingdatumgr);
        TextView vak = (TextView) findViewById(R.id.aankondingvakgr);
        TextView poster = (TextView) findViewById(R.id.aankondingpostergr);
        tekst.setText(aankondiging.geefTekst());
        titel.setText(aankondiging.geefTitel());
        vak.setText(aankondiging.geefVak().geefNaam());
        datum.setText("test");
        if (poster != null) {
            if (aankondiging.geefAankondiger() != Gebruiker.geenGebruiker) {
                poster.setText("door" + " " + aankondiging.geefAankondiger().geefAchternaam() + " " + aankondiging.geefAankondiger().geefVoornaam());
            } else {
                poster.setText("door onbekend");
            }
        }
    }
}
