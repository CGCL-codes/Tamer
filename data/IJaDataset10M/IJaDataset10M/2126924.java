package skat09.ui;

import java.io.IOException;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import skat09.Controller;
import skat09.Tisch;
import skat09.spielart.Farbspiel;
import skat09.spielart.Grandspiel;
import skat09.spielart.Nullspiel;
import skat09.spieler.SpielerEnum;
import skat09.spielkarte.Farbe;
import skat09.spielkarte.Spielkarte;
import skat09.test.interfaces.IController;
import skat09.test.interfaces.ISpielart;
import skat09.test.interfaces.ISpieler;
import skat09.ui.gui.*;

/**
 * Diese Klasse besch&auml;ftigt sich mit der gesamten Ausgabe des Spiels fuer
 * den menschlichen Spieler.
 * 
 * @author Ann-Christine Kycler, Sebastian Schlatow, Mathias Stoislow, Martin Bruhns
 * @version 03.07.2009
 * 
 */
public class GUIausgabe extends Ausgabe {

    /**
	 * Der Tisch, auf dem gespielt wird
	 */
    private Tisch tisch;

    /**
	 * Das Hauptfenster des Spiels
	 */
    private HFenster hfenster;

    /**
	 * Das Einstellungsfenster des Spiels
	 */
    private Fenster fenster;

    /**
	 * Der Konstruktor der Klasse GUIAusgabe
	 * 
	 * @param tisch
	 *           Der Tisch, auf dem gespielt wird
	 */
    public GUIausgabe(Tisch tisch) {
        this.tisch = tisch;
        fenster = new Fenster(this);
    }

    @Override
    public boolean hoeren(int wert) {
        hfenster.hoeren(wert);
        warte();
        boolean erg = hfenster.getHoeren();
        return erg;
    }

    @Override
    public boolean sagen(int alterWert) {
        hfenster.sagen(alterWert);
        warte();
        boolean erg = hfenster.getSagen();
        return erg;
    }

    @Override
    public void spiel() {
    }

    @Override
    public String name() {
        return fenster.getName();
    }

    @Override
    public String gegner(int nummer) {
        String s = "";
        if (nummer == 1) {
            if (fenster.getMitspieler1() == SpielerEnum.OMA) {
                s = "o";
            } else if (fenster.getMitspieler1() == SpielerEnum.REGELKONFORM) {
                s = "r";
            } else if (fenster.getMitspieler1() == SpielerEnum.INTELLIGENT) {
                s = "s";
            }
        }
        if (nummer == 2) {
            if (fenster.getMitspieler2() == SpielerEnum.OMA) {
                s = "o";
            } else if (fenster.getMitspieler2() == SpielerEnum.REGELKONFORM) {
                s = "r";
            } else if (fenster.getMitspieler2() == SpielerEnum.INTELLIGENT) {
                s = "s";
            }
        }
        return s;
    }

    @Override
    public String frageVariante() {
        String erg;
        int i = fenster.getSkatWahl();
        if (i == 1) {
            erg = "i";
        } else if (i == 2) {
            erg = "b";
        } else erg = "r";
        return erg;
    }

    @Override
    public void falscheEingabe() {
        JFrame frame = new JFrame("ACHTUNG!");
        frame.add(new JLabel("Achtung, ihre Eingabe wahr fehlerhaft!"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public boolean handspiel() {
        hfenster.handspiel();
        warte();
        boolean wert = hfenster.getHandspiel();
        return wert;
    }

    @Override
    public int druecken(ArrayList<Spielkarte> blatt, int nummer) {
        hfenster.blattneu(tisch.gibMenschlicherSpieler());
        hfenster.setZug(true);
        warte();
        int kartennummer = hfenster.getGewaehlteKarte();
        hfenster.setZug(false);
        return kartennummer;
    }

    @Override
    public Farbspiel farbe() {
        Farbspiel spiel = null;
        String farbe = hfenster.getFarbe();
        spiel = farbeansagen(farbe);
        return spiel;
    }

    @Override
    public boolean ouvert() {
        hfenster.ouvert();
        warte();
        boolean wert = hfenster.getOuvert();
        return wert;
    }

    @Override
    public boolean schneider() {
        hfenster.schneider();
        warte();
        boolean wert = hfenster.getSchneider();
        return wert;
    }

    @Override
    public boolean schwarz() {
        hfenster.schwarz();
        warte();
        boolean wert = hfenster.getSchwarz();
        return wert;
    }

    @Override
    public ISpielart spielAnsagen() {
        ISpielart spiel = null;
        hfenster.ansagen();
        warte();
        String spielart = hfenster.getSpielart();
        if (spielart.compareTo("Grand") == 0) {
            spiel = new Grandspiel();
        } else if (spielart.compareTo("Null") == 0) {
            spiel = new Nullspiel();
        } else {
            spiel = new Farbspiel(null);
        }
        return spiel;
    }

    @Override
    public Spielkarte spieleKarte(Spielkarte[] gespielteKarten, ISpieler spieler) throws IOException {
        hfenster.setGespielteKarten(gespielteKarten);
        zeigegespielteKarten(gespielteKarten);
        blattAusgeben(spieler);
        hfenster.setZug(true);
        warte();
        int kartennummer = hfenster.getGewaehlteKarte();
        Spielkarte karte = spieler.getBlatt().remove(kartennummer);
        hfenster.setZug(false);
        return karte;
    }

    private void zeigegespielteKarten(Spielkarte[] gespielteKarten) {
        hfenster.tischRaumen();
        if (gespielteKarten[0] != null) {
            hfenster.spieltKarte(gespielteKarten[0]);
            if (gespielteKarten[1] != null) {
                hfenster.spieltKarte(gespielteKarten[1]);
            }
        }
    }

    @Override
    public void stichGewonnen(ISpieler spieler) {
        hfenster.stichGewonnen(spieler);
    }

    @Override
    public void blattAusgeben(ISpieler spieler) throws IOException {
        hfenster.blattAusgeben(spieler);
        hfenster.gegnerKarten(tisch.getSpieler2(), tisch.getSpieler3());
    }

    @Override
    public void skatAusgeben(Spielkarte[] skat) throws IOException {
        hfenster.skatAusgeben(skat);
    }

    @Override
    public void auswertung(boolean gewonnen) {
        hfenster.auswertung(gewonnen);
    }

    @Override
    public void spielBeendet() {
        hfenster.skataufTisch();
        warte();
    }

    @Override
    public void weg(ISpieler spieler) {
    }

    @Override
    public void andereKarte() {
        hfenster.andereKarte();
    }

    @Override
    public void hhVSgewinner(ISpieler gewinner) {
    }

    @Override
    public void leerzeile() {
    }

    @Override
    public void mhVSvh() {
    }

    @Override
    public void spielBeginnt() {
        hfenster.spielBeginnt();
    }

    @Override
    public void spielEinpassen() {
    }

    @Override
    public void augen(int augen) {
        hfenster.setAugen(augen);
    }

    @Override
    public void punkte(int punkte) {
    }

    @Override
    public void alleinspieler() {
        hfenster.alleinspieler();
    }

    @Override
    public void trumpf() {
        hfenster.trumpf();
    }

    @Override
    public void neuesSpiel() {
    }

    @Override
    public String einlesen() {
        return null;
    }

    @Override
    public boolean reizAgent() {
        hfenster.reizagent();
        warte();
        boolean erg = hfenster.getAgent();
        return erg;
    }

    @Override
    public int reizlimitFestlegen() {
        boolean fertig = false;
        hfenster.reizLimfest();
        warte();
        int wert = hfenster.getReizlim();
        while (!fertig) {
            SortedSet reizwerte = tisch.getReizwerte();
            if (reizwerte.contains(wert) || wert == 0) {
                fertig = true;
            } else {
                hfenster.reizLimfest();
                warte();
                wert = hfenster.getReizlim();
            }
        }
        return wert;
    }

    @Override
    public void spieltKarte(ISpieler spieler, Spielkarte karte) {
        if (tisch.gibMenschlicherSpieler().getName() == spieler.getName()) {
            hfenster.menschSpieltKarte();
        } else {
            hfenster.spieltKarte(karte);
        }
    }

    @Override
    public boolean spielBeenden() {
        return false;
    }

    @Override
    public void punkteAusgeben() {
    }

    /**
	 * Die GUIAusgabe soll solange warten, bis die Variable release wieder auf
	 * true gesetzt wird
	 */
    public void warte() {
        while (!getRelease()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setRelease(false);
    }

    /**
	 * Fragt ab welche Farbe gespielt werden soll
	 * @param s Der String, der die Farbe beinhaltet
	 * @return Das gew&auml;hlte Farbspiel
	 */
    public Farbspiel farbeansagen(String s) {
        Farbspiel spiel = null;
        if (s.compareTo("Herz") == 0) {
            spiel = new Farbspiel(Farbe.HERZ);
        } else if (s.compareTo("Karo") == 0 || s.compareTo("Schellen") == 0) {
            spiel = new Farbspiel(Farbe.KARO);
        } else if (s.compareTo("Pik") == 0 || s.compareTo("Gruen") == 0) {
            spiel = new Farbspiel(Farbe.PIK);
        } else if (s.compareTo("Kreuz") == 0 || s.compareTo("Eichel") == 0) {
            spiel = new Farbspiel(Farbe.KREUZ);
        } else {
            System.out.println("Fehler in farbeansagen()");
        }
        return spiel;
    }

    @Override
    public void tischLoeschen() {
        hfenster.tischRaumen();
    }

    @Override
    public String getBlattwahl() {
        String s = "";
        if (fenster.getDeutsch() == true) {
            s = "d";
        } else {
            s = "f";
        }
        return s;
    }

    @Override
    public String frageSechserskat() {
        String erg;
        boolean b = fenster.getSechserkat();
        if (b) {
            erg = "j";
        } else {
            erg = "n";
        }
        return erg;
    }

    @Override
    public void hauptfensterOeffnen() {
        hfenster = new HFenster(this.tisch, this);
    }

    @Override
    public void positionAnzeigen() {
        hfenster.gegnerKarten(tisch.getSpieler2(), tisch.getSpieler3());
    }

    @Override
    public void guiAufraumen() {
        hfenster.spielAufrauemen();
    }

    @Override
    public void statistik() {
        hfenster.statistik();
        hfenster.neuesSpiel();
        warte();
    }
}
