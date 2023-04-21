package model;

import control.cWagen;
import control.modell;
import control.interfaces.interface_Wagen;

public class wagen extends cWagen implements interface_Wagen {

    public wagen(String epoche, String wagennummer, String wagentyp) {
        super(epoche, wagennummer, wagentyp);
    }

    @Override
    public void wagenanlegen() {
    }

    @Override
    public void wagenbearbeiten() {
    }

    @Override
    public void wagenloeschen() {
    }

    @Override
    public void wagensuchen() {
    }

    @Override
    public void wagenanlegen(String herstellername, String herstelleranschrift, String herstellerwebseite, String bezeichnung, String Bestellnummer, String Kaufdatum, String Spurweite, String Sturtyp, String epoche, String wagennummer, String wagentyp) {
    }
}
