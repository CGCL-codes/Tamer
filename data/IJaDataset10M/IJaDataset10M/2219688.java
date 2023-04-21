package nl.hu.vakantievibes.server;

import java.util.ArrayList;
import nl.hu.vakantievibes.domain.Gebruiker;

public class AlleGebruikers {

    private static AlleGebruikers gg;

    private ArrayList<Gebruiker> alleGebruikers = new ArrayList<Gebruiker>();

    private AlleGebruikers() {
    }

    public static AlleGebruikers get() {
        if (gg == null) {
            gg = new AlleGebruikers();
        }
        return gg;
    }

    public boolean addGebruiker(String gebNaam, String ww) {
        Gebruiker g = new Gebruiker(gebNaam, ww);
        return alleGebruikers.add(g);
    }

    public ArrayList<Gebruiker> getGebruikers() {
        return alleGebruikers;
    }

    public Gebruiker getGebruiker(String gebruikersNaam) {
        Gebruiker tempG = null;
        for (Gebruiker g : alleGebruikers) {
            if (gebruikersNaam.equals(g.getGebruikersNaam())) {
                tempG = g;
            }
        }
        return tempG;
    }
}
