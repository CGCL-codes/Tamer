package fhi.bg.logik.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import fhi.bg.fachklassen.AnderesSpielAktivException;
import fhi.bg.fachklassen.BeerGame;
import fhi.bg.fachklassen.Gruppe;
import fhi.bg.fachklassen.InvalidParameterException;
import fhi.bg.fachklassen.KeinSpielAktivException;
import fhi.bg.fachklassen.Spiel;
import fhi.bg.fachklassen.Spieler;
import fhi.bg.logik.S03_BestellungSenden;
import fhi.bg.logik.S04_BestaetigungSenden;
import fhi.bg.logik.SL02_SpielErstellen;
import fhi.bg.logik.SL10_SpielVerlassen;

public abstract class S03_Base {

    public String[] getNamen() {
        return new String[] { "A", "B", "C", "D", "E" };
    }

    Spiel spiel;

    ArrayList<Spieler> spieler;

    @Before
    public void setup() {
        BeerGame.get().init("");
        String[] namen = this.getNamen();
        spieler = new ArrayList<Spieler>();
        try {
            SL02_SpielErstellen.erstelleSpiel("Testspiel", (namen.length / 5), namen);
        } catch (InvalidParameterException e) {
            fail(e.toString());
        } catch (AnderesSpielAktivException e) {
        }
        spiel = BeerGame.get().getAktuellesSpiel();
        assertNotNull(spiel);
        assertEquals((namen.length / 5), spiel.getGruppen().length);
        for (Gruppe g : spiel.getGruppen()) {
            spieler.add(g.getEinzelhaendler().getSpieler());
            spieler.add(g.getGrosshaendler().getSpieler());
            spieler.add(g.getZentrallager().getSpieler());
            spieler.add(g.getBrauerei().getSpieler());
            spieler.add(g.getFlaschenhersteller().getSpieler());
        }
    }

    public int[] lagerMengen() {
        int[] mengen = new int[spieler.size()];
        int index = 0;
        for (Spieler s : spieler) {
            mengen[index++] = s.getRolle().getLagerMenge();
        }
        return mengen;
    }

    public int[] weMengen() {
        int[] mengen = new int[spieler.size()];
        int index = 0;
        for (Spieler s : spieler) {
            mengen[index++] = s.getRolle().getWareneingangMenge();
        }
        return mengen;
    }

    public int[] waMengen() {
        int[] mengen = new int[spieler.size()];
        int index = 0;
        for (Spieler s : spieler) {
            mengen[index++] = s.getRolle().getWarenausgangMenge();
        }
        return mengen;
    }

    public int[] fehlMengen() {
        int[] mengen = new int[spieler.size()];
        int index = 0;
        for (Spieler s : spieler) {
            mengen[index++] = s.getRolle().getFehlMenge(false);
        }
        return mengen;
    }

    public void bestaetigen(String msg) throws Exception {
        bestaetigen(msg, false);
    }

    public void bestaetigen(String msg, boolean reverse) throws Exception {
        if (reverse) {
            for (int i = spieler.size() - 1; i >= 0; i--) {
                S04_BestaetigungSenden.sendeBestaetigung(spieler.get(i), msg);
            }
        } else {
            for (int i = 0; i < spieler.size(); i++) {
                S04_BestaetigungSenden.sendeBestaetigung(spieler.get(i), msg);
            }
        }
    }

    public void bestelleRunde(int runde, int[] bestellungen) throws Exception {
        assertEquals(runde, spiel.getAktuelleRunde().getRundenNummer());
        int index = 0;
        for (Spieler s : spieler) {
            S03_BestellungSenden.sendeBestellung(s, bestellungen[index++]);
        }
    }

    @After
    public void fin() throws InvalidParameterException, KeinSpielAktivException {
        SL10_SpielVerlassen.verlasseSpiel(true);
        BeerGame.get().databaseShutdown();
    }
}
