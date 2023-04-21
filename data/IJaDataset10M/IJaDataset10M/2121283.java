package org.schnelln.spiel.karten;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.schnelln.spiel.farbe.Farbe;
import org.schnelln.spiel.karten.Karte;

public class Kartendeck extends ArrayList<Karte> implements Serializable {

    private static final long serialVersionUID = 1815291071022710025L;

    public Kartendeck() {
        super();
    }

    /**
	 * Diese Methode sucht uns im Kartendeck nach bestimmten Farben
	 * @param Farbe - Die Farbe die gesucht werden soll
	 * @return boolean - True, genau dann, wenn die Farbe im Deck enthalten
	 */
    public boolean hasFarbeInKartendeck(Farbe f) {
        for (int i = 0; i < this.size(); i++) {
            if ((this.get(i).getFarbe()).equals(f)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Diese Methode &uuml;berpr&uuml;ft, ob im Kartendeck alle Karten kleiner/gleich dem Wert 
	 * 10 sind.
	 * @return true, falls alle Karten <= 10, ansonsten false
	 */
    public boolean alleKartenKleinerGleichZehn() {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getWert() > 10 && this.get(i).getWert() != 14) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Diese Methode ersetzt mehrere Karten im Kartendeck durch neue, zuf&auml;llige
	 * Karten 
	 * @param Queue<Integer> - Die Liste der Karten mit der Nummer der Position im Kartendeck
	 * @param stapel - Der Kartenstapel von dem gezogen werden soll
	 */
    public void fuegeKarteHinzu(int position, Stapel stapel) {
        this.set(position, stapel.pop());
    }

    /**
	 * Die Methode sucht eine Karte im Kartendeck und retourniert
	 * deren Index, falls sie gefunden wurde.
	 * Ansonsten wird eine NoSuchElementException geworfen.
	 * @param k
	 * @return
	 */
    public int getIndexFromKarte(Karte k) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(k)) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    public Karte getKarte(Karte k) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(k)) {
                return this.remove(i);
            }
        }
        throw new NoSuchElementException();
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < this.size(); i++) {
            out = out + this.get(i).getFarbe().getFarbenBezeichnung() + " " + this.get(i).getBezeichnung() + "; ";
        }
        return out;
    }

    public boolean remove(Karte k) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(k)) {
                this.remove(i);
                return true;
            }
        }
        return false;
    }
}
