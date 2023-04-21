package skat09.test.stub;

import java.util.ArrayList;
import skat09.spielart.Farbspiel;
import skat09.spieler.Position;
import skat09.spielkarte.Farbe;
import skat09.spielkarte.Spielkarte;
import skat09.test.interfaces.IMenschlicherSpieler;
import skat09.test.interfaces.ISpielart;
import skat09.test.interfaces.ISpieler;

public class MenschlicherSpielerStub implements ISpieler, IMenschlicherSpieler {

    private String name;

    private boolean istAlleinspieler;

    private ArrayList<Spielkarte> blatt;

    Position position;

    public MenschlicherSpielerStub(String name) {
        this.name = name;
        this.istAlleinspieler = false;
        this.blatt = new ArrayList<Spielkarte>();
    }

    public boolean agent() {
        boolean ergebnis = true;
        return ergebnis;
    }

    public void blattSortieren(ISpielart spielart) {
    }

    public Spielkarte[] druecken(Spielkarte[] skat) {
        return skat;
    }

    public boolean equals(ISpieler spieler) {
        return false;
    }

    public Farbspiel farbe() {
        return new Farbspiel(Farbe.KARO);
    }

    public ArrayList<Spielkarte> getBlatt() {
        return blatt;
    }

    public boolean getIstAlleinspieler() {
        return istAlleinspieler;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public ArrayList<Integer> getSpiele() {
        return null;
    }

    public ArrayList<Spielkarte> getStiche() {
        return null;
    }

    public boolean handspiel() {
        return false;
    }

    public boolean hoeren(int reizwert) {
        boolean ergebnis = false;
        if (reizwert <= 22) {
            ergebnis = true;
        }
        return ergebnis;
    }

    public ArrayList<Integer> neuerEintrag(int punkte) {
        return null;
    }

    public boolean ouvert() {
        return true;
    }

    public int reizlimitFestlegen() {
        return 30;
    }

    public boolean sagen(int alterWert) {
        boolean ergebnis = false;
        if (alterWert <= 22) {
            ergebnis = true;
        }
        return ergebnis;
    }

    public boolean schneider() {
        return true;
    }

    public boolean schwarz() {
        return true;
    }

    public void setBlatt(ArrayList<Spielkarte> blatt) {
    }

    public void setIstAlleinspieler(boolean istAlleinspieler) {
        this.istAlleinspieler = istAlleinspieler;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setSpielart(ISpielart spielart) {
    }

    public void setStiche(ArrayList<Spielkarte> stiche) {
    }

    public ISpielart spielAnsagen() {
        return new Farbspiel(null);
    }

    public Spielkarte spieleKarte(Spielkarte[] gespielteKarten) {
        return null;
    }

    public void stichHinzufuegen(Spielkarte[] stich) {
    }

    public void bubeneinordnen() {
    }

    public void farbeneinordnen() {
    }

    public void gespielteKartenHinzufuegen(Spielkarte[] gespielteKarten) {
    }

    public ArrayList<Spielkarte> getAllegespieltenkarten() {
        return null;
    }

    public ISpieler getMitspieler() {
        return null;
    }

    public ArrayList<Spielkarte> getRestblatt() {
        return null;
    }

    public ISpielart getSpielart() {
        return null;
    }

    public void setAlleGespieltenKarten(ArrayList<Spielkarte> karten) {
    }

    public void setDeck(ArrayList<Spielkarte> deck) {
    }

    public void setMitspieler(ISpieler mitspieler) {
    }

    public void setName(String name) {
    }

    public void setSkat(ArrayList<Spielkarte> skat) {
    }

    public void setSpiele(ArrayList<Integer> spiele) {
    }

    public void setTruempfe(Spielkarte[] truempfe) {
    }

    public ArrayList<Spielkarte> spielbareKarten(Spielkarte[] gespielteKarten) {
        return null;
    }

    public Spielkarte[] spitzenEinordnen() {
        return null;
    }

    public int spitzenMit(int erg) {
        return 0;
    }

    public int spitzenOhne(int erg) {
        return 0;
    }

    public int spitzenZahl() {
        return 0;
    }

    public Spielkarte zufaelligErlaubteKarteSpielen(Spielkarte[] gespielteKarten) {
        return null;
    }

    public int getHandspiele() {
        return 0;
    }

    public void setHandspiele(int handspiele) {
    }

    public int bubeneinordnenhilf(int bubenwert) {
        return 0;
    }

    public int farbeeinordnenhilf(int i) {
        return 0;
    }
}
