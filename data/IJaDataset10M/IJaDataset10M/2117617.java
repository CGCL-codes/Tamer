package orproject.helpers;

import java.math.BigDecimal;
import java.util.Vector;

/**
 * Container für alle Ungleichungen eines Simplex-Tableaus
 * @author Pförd
 */
public class DSGleichungsSystem extends Vector<DSUngleichung> {

    /**
	 * Container für die Ueberschriften der Spalte
	 */
    Vector<String> spaltenBezeichnung;

    /**
	 * Container für den Eckpunkt der Gleichung
	 */
    Vector<BigDecimal> eckpunkt;

    /**
	 * Pruefvariable, ob Spaltenbezeichnungen gesetzt sind
	 */
    private boolean isSet;

    private boolean isMultiple;

    /**
	 * Konstruktor der Datenstruktur
	 */
    public DSGleichungsSystem() {
        eckpunkt = new Vector<BigDecimal>();
        spaltenBezeichnung = new Vector<String>();
        isMultiple = false;
    }

    /**
	 * es wird gesetzt, ob es eine Verzweigug (mehrere c = 0) gibt
         *
	 * @param multi setzt isMultiple
	 */
    public void setMultiple(boolean multi) {
        isMultiple = multi;
    }

    /**
	 * es wird abgefragt, ob es eine Verzweigug (mehrere c = 0) gibt
         *
	 * @return Abfrage, ob es eine Verzweigung gibt
	 */
    public boolean getMultiple() {
        return isMultiple;
    }

    /**
	 * Setzt den Eckpunkt des Gleichungssystems
	 *
	 */
    public void setEckpunkt() {
        if (this.eckpunkt.size() < 1) {
            for (int i = 0; i < this.get(0).getKoeffizienten().length; i++) {
                eckpunkt.add(new BigDecimal(0.0));
            }
        }
        for (int i = 0; i < this.get(0).getKoeffizienten().length; i++) {
            boolean gesetzt = false;
            for (int j = 0; j < this.size() - 1; j++) {
                if (this.get(j).zeilenBezeichnung.equals("x" + (i + 1))) {
                    eckpunkt.set(i, this.get(j).getErgebnis());
                    gesetzt = true;
                }
            }
            if (!gesetzt) {
                eckpunkt.set(i, new BigDecimal(0));
            }
        }
        BigDecimal tmpEckpunkt = new BigDecimal(-1);
        tmpEckpunkt = tmpEckpunkt.multiply(this.get(this.size() - 1).getErgebnis());
        if (this.eckpunkt.size() < this.get(0).getKoeffizienten().length + 1) {
            eckpunkt.add(tmpEckpunkt);
        } else {
            eckpunkt.set(this.eckpunkt.size() - 1, tmpEckpunkt);
        }
    }

    /**
	 * Gibt den Eckpunkt zurueck
	 * das letzte Element im eckpunkt ist das Maximum
         * 
	 * @return tEckpunkt einzelne Punkte des Eckpunkts
	 */
    public BigDecimal[] getEckpunkt() {
        BigDecimal[] tEckpunkt = new BigDecimal[eckpunkt.size()];
        for (int i = 0; i < tEckpunkt.length; i++) {
            tEckpunkt[i] = eckpunkt.elementAt(i);
        }
        return tEckpunkt;
    }

    /**
	 * Setzt die Beschreibung der Spalte (x1, z1,...)
         *
         * @param i Stelle die ausgegeben werden soll
	 * @param z Ueberschrift der Spalte
	 */
    public void setSpaltenBezeichnung(int i, String z) {
        spaltenBezeichnung.set(i, z);
    }

    /**
	 * Gibt die Beschreibung der Spalte (x1, z1,...) aus
	 *
         * @param i Stelle die ausgegeben werden soll
	 * @return gibt die Beschreibung der Spalte aus
	 */
    public String getSpaltenBezeichnung(int i) {
        return spaltenBezeichnung.get(i);
    }

    /**
	 * Gibt die Beschreibung der Zeile (x1, z1,...) aus
	 *
         * @param i Stelle die ausgegeben werden soll
	 * @return gibt die Beschreibung der Zeile aus
	 */
    public String getZeilenBezeichnung(int i) {
        return this.get(i).getZeilenBezeichnung();
    }

    /**
	 * Gibt die Beschreibung der Spalten (x1, z1,...) aus
	 *
	 * @return gibt die Beschreibungen aller Spalten aus
	 */
    public String[] getSpaltenBezeichnungen() {
        String[] sz = new String[this.get(0).getKoeffizienten().length];
        for (int i = 0; i < spaltenBezeichnung.size(); i++) {
            sz[i] = spaltenBezeichnung.elementAt(i);
        }
        return sz;
    }

    /**
	 * Gibt die Beschreibung der Zeilen (x1, z1,...) aus
	 *
	 * @return gibt die Beschreibungen aller Zeilen aus
	 */
    public String[] getZeilenBezeichnungen() {
        String[] zz = new String[this.size() - 1];
        for (int i = 0; i < zz.length; i++) {
            zz[i] = this.getZeilenBezeichnung(i);
        }
        return zz;
    }

    /**
	 * Fügt eine neue Spaltenbezeichnung ein
	 *
         * @param z Bezeichnung für die neue Spalte
	 */
    public void addSpaltenBezeichnung(String z) {
        spaltenBezeichnung.add(z);
    }

    /**
	 * Gibt die Variable isSet aus
	 *
         * @return isSet gibt an, ob Spaltenbezeichnungen gesetzt wurden
	 */
    public boolean getIsSet() {
        return isSet;
    }

    /**
	 * Aendert den Status von isSet
	 *
         * @param b wenn true: Spaltenbezeichnungne gesetzt
	 */
    public void setIsSet(boolean b) {
        isSet = b;
    }

    @Override
    public synchronized Object clone() {
        DSGleichungsSystem tmp = new DSGleichungsSystem();
        tmp = (DSGleichungsSystem) super.clone();
        int i = 0;
        for (DSUngleichung item : this) {
            tmp.set(i, (DSUngleichung) item.clone());
            i++;
        }
        return tmp;
    }
}
