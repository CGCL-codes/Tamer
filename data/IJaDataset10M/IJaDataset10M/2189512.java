package net.sf.fkk.spiel.optionen;

import java.util.Vector;
import java.util.Iterator;
import java.io.Serializable;
import net.sf.fkk.global.ParameterException;
import net.sf.fkk.global.FatalException;

/**
 * Die Klasse enthaelt die Spieloptionen fuer ein Spiel
 * @author Marcel Koeppen
 */
public class SpielOptionen implements Serializable {

    /**
     * Erstellt ein neues SpielOptionen Objekt
     */
    public SpielOptionen() {
        spielerOptionen = new Vector(MAX_SPIELER);
    }

    /**
     * Gibt die anzahl der benoetigten Chips zurueck
     * @return benoetigte Gewinnchips
     */
    public int getGewinnChips() {
        return (gewinnChips);
    }

    /**
     * Setzt die anzahl der benoetigten Gewinn-Chips
     * @param chips benoetigte Gewinnchips
     */
    public void setGewinnChips(int chips) {
        if (chips < 0) throw new ParameterException("Negative Chipanzahl");
        gewinnChips = chips;
    }

    /**
     * Fuegt die Uebergebenen Optionen fuer einen Spieler am Anfang in die Liste ein, wenn die Liste noch nicht die maximale Anzahl von Spieleroptionen enthaelt (vier) und noch kein Spieler die gewaehlte Farbe hat. Die Spieler muessen also in umgekehrter Reihenfolge in die Liste eingefuegt werden!
     */
    public void addSpielerOptionen(SpielerOptionen sopts) throws SpielerAnzahlException, FarbClashException {
        if (sopts == null) throw new ParameterException("Optionen sind null");
        if (spielerOptionen.contains(sopts)) throw new FatalException("Diese Spieleroptionen sind schon registriert");
        if (spielerOptionen.size() == MAX_SPIELER) throw new SpielerAnzahlException("Es koennen nicht so viele Spieler mitspielen!");
        Iterator iter = spielerOptionen.iterator();
        int farbe = sopts.getFarbe();
        boolean belegt = false;
        while (iter.hasNext()) if (((SpielerOptionen) iter.next()).getFarbe() == farbe) belegt = true;
        if (belegt) throw new FarbClashException("Bitte waehlen Sie fuer alle Spieler verschiedene Farben aus!");
        spielerOptionen.add(0, sopts);
    }

    /**
     * Liefert einen Iterator auf die SpielerOptionen
     */
    public Iterator getSpielerOptionenIterator() {
        return new SpielerOptionenIterator();
    }

    /**
     * Leert die Spieleroptionenliste
     */
    public void clearSpielerOptionen() {
        spielerOptionen.clear();
    }

    /**
     * Liefert die Anzahl der gesetzten SpielerOptionen
     */
    public int getSpielerOptionenAnzahl() {
        return spielerOptionen.size();
    }

    /**
     * Konstante, wieviele Spieler maximal moeglich sind. 
     */
    public static final int MAX_SPIELER = 4;

    /**
     * Liste der Spieleroptionen
     */
    private Vector spielerOptionen;

    /**
     * Anzahl der Chips, die zum Gewinnen benoetigt werden 
     */
    private int gewinnChips;

    /**
     * Eine Iteratorklasse zum Durchlaufen der Spieleroptionen
     */
    private class SpielerOptionenIterator implements Iterator {

        public SpielerOptionenIterator() {
            origIter = spielerOptionen.iterator();
        }

        /**
         * gibt es ein weiteres Element?
         * @postconditions return=true, wenn es ein weiteres Element gibt, sonst false 
         */
        public boolean hasNext() {
            return origIter.hasNext();
        }

        /**
         * gibt das naechste Element in der Liste zurueck
         * @postconditions return=das naechste Element 
         */
        public Object next() {
            return origIter.next();
        }

        /**
         * Wirft UnsupportedOperationException, da das Loeschen per Iterator nicht erlaubt werden soll.
         * @postconditions wirft UnsupportedOperationException 
         */
        public void remove() {
            throw new UnsupportedOperationException("Loeschen ist nicht erlaubt!");
        }

        /**
         * Der originale Iterator der SpielerOptionen-Liste 
         */
        private Iterator origIter;
    }
}
