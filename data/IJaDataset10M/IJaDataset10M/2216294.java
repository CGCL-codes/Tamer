package com.cwb3.moto.vragenstellencontrollerview;

import android.content.Intent;
import android.widget.ArrayAdapter;
import com.cwb3.moto.R;
import com.cwb3.moto.domeinmodel.Vak;
import com.cwb3.moto.globalcontrollerview.Feature;
import com.cwb3.moto.sjabloonview.ObjectListView;

/**
 * Geef een lijst met alle vakken weer.
 * 
 * @author CWB3
 *
 */
public class VakLijstView extends ObjectListView<Vak> implements Feature {

    private static final String ACTION = "com.cwb3.moto.vragenstellencontrollerview.vak.LAUNCH";

    @Override
    protected String geefHoofding() {
        return "Kies een vak";
    }

    @Override
    protected Vak[] geefWeerTeGevenObjecten() {
        return VragenStellenController.geefVragenStellenController().geefAlleVakken().toArray(new Vak[0]);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.vragenstellen_vak_lijst);
    }

    @Override
    protected ArrayAdapter<Vak> invullenDataDoorObject() {
        return new VakAdapter(this, R.layout.aankondiging_vak_klein, geefObjectArray());
    }

    @Override
    protected Intent opstartenNieweActiviteit(Vak vak) {
        VragenStellenController.geefVragenStellenController().slaTijdelijkVakOp(vak);
        return new Intent(this, ContactMomentLijstView.class);
    }

    public String geefBeschrijving() {
        return "Een vraag stellen, bekijken of beantwoorden gaat hier";
    }

    public Intent geefIntent() {
        return new Intent().setAction(ACTION);
    }

    public String geefTitel() {
        return "VRAAG STELLEN";
    }
}
