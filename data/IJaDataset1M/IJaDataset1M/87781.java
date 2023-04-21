package com.cafe.serve.client;

import com.cafe.serve.server.Drink;
import com.cafe.serve.server.DrinkWithQuantity;
import com.cafe.serve.util.Tisch;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Asterios Raptis Diese Klasse speichert eine Bestellung.
 */
public class Bestellung extends Object implements Serializable, Cloneable {

    /**
     * Die statische serialVersionUID f�r diese Klasse.
     */
    static final long serialVersionUID = -6821921330630598716L;

    /**
     * Konstante f�r die Berechnung des hashcode. Inhalt:"1000003"
     */
    private static final int HAUPT_HACK = 1000003;

    /**
     * Hashtable die alle Getr�nke enth�lt.
     */
    private Hashtable allDrinks = null;

    /**
     * Vector f�r das sortieren von den drinks. Nach dem Index wird geordnet.
     */
    private Vector drinkSorter = null;

    /**
     * Diese Variable enth�lt die Anzahl wie oft diese Bestellung gesendet
     * wurde.
     */
    private int gesendet = 0;

    /**
     * Der Name des Tisches von wo die Bestellung ausging.
     */
    private String tisch = null;

    /**
     * Der Name des Tisches von wo die Bestellung ausging als Tisch-Object.
     */
    private Tisch tischTable = null;

    /**
     * Der Name des PDAs von dem die Bestellung gesendet wurde.
     */
    private String bedienung = null;

    /**
     * Die bestell_id ist die id f�r die DB
     */
    private int bestell_id;

    /**
     * Der bestellDatum f�r die DB
     */
    private Timestamp bestellDatum = null;

    /**
     * @param allDrinks
     * @param drinkSorter
     * @param gesendet
     * @param tisch
     * @param tischTable
     * @param bedienung
     */
    public Bestellung(Hashtable allDrinks, Vector drinkSorter, int gesendet, String tisch, Tisch tischTable, String bedienung, int bestell_id) {
        this.allDrinks = allDrinks;
        this.drinkSorter = drinkSorter;
        this.gesendet = gesendet;
        this.tisch = tisch;
        this.tischTable = tischTable;
        this.bedienung = bedienung;
        this.bestell_id = bestell_id;
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @param allDrinks
     * @param drinkSorter
     * @param gesendet
     * @param tisch
     * @param bedienung
     */
    public Bestellung(Hashtable allDrinks, Vector drinkSorter, int gesendet, String tisch, String bedienung) {
        this.allDrinks = allDrinks;
        this.drinkSorter = drinkSorter;
        this.gesendet = gesendet;
        this.tisch = tisch;
        this.bedienung = bedienung;
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Konstruiert eine Bestellung.
     */
    public Bestellung() {
        this.allDrinks = new Hashtable();
        this.drinkSorter = new Vector();
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Konstruiert eine Bestellung mit dem Tisch und der Bedienung.
     *
     * @param t.
     *            Der Tisch von wo die Bestellung ausgeht.
     * @param b.
     *            Der PDA von dem die Bestellung abgeschikt wird.
     */
    public Bestellung(String t, String b) {
        this.allDrinks = new Hashtable();
        this.drinkSorter = new Vector();
        this.tisch = t;
        this.tischTable = new Tisch(tisch);
        this.bedienung = b;
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Konstruiert eine Bestellung mit den Getr�nken, dem Tisch und der
     * Bedienung.
     *
     * @param ad.
     *            Die Getr�nke in der Bestellung.
     * @param t.
     *            Der Tisch von wo die Bestellung ausgeht.
     * @param b.
     *            Der PDA von dem die Bestellung abgeschikt wird.
     */
    public Bestellung(Hashtable ad, String t, String b) {
        this.allDrinks = new Hashtable();
        this.drinkSorter = new Vector();
        this.allDrinks = ad;
        this.tisch = t;
        this.tischTable = new Tisch(tisch);
        this.bedienung = b;
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Konstruiert eine Bestellung mit der Anzahl der Sendungen von der
     * Bestellung, dem Tisch und der Bedienung.
     *
     * @param g.
     *            Die Anzahl der Sendungen von der Bestellung.
     * @param t.
     *            Der Tisch von wo die Bestellung ausgeht.
     * @param b.
     *            Der PDA von dem die Bestellung abgeschikt wird.
     */
    public Bestellung(int g, String t, String b) {
        this.allDrinks = new Hashtable();
        this.gesendet = g;
        this.tisch = t;
        this.tischTable = new Tisch(tisch);
        this.bedienung = b;
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Konstruiert eine Bestellung mit den Getr�nken, mit der Anzahl der
     * Sendungen von der Bestellung, dem Tisch und der Bedienung.
     *
     * @param ad.
     *            Die Getr�nke in der Bestellung.
     * @param g.
     *            Die Anzahl der Sendungen von der Bestellung.
     * @param t.
     *            Der Tisch von wo die Bestellung ausgeht.
     * @param b.
     *            Der PDA von dem die Bestellung abgeschikt wird.
     */
    public Bestellung(Hashtable ad, int g, String t, String b) {
        this.allDrinks = new Hashtable();
        this.allDrinks = ad;
        this.gesendet = g;
        this.tisch = t;
        this.tischTable = new Tisch(tisch);
        this.bedienung = b;
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @param tisch
     */
    public Bestellung(Tisch tisch) {
        this.allDrinks = new Hashtable();
        this.tisch = tisch.getTisch();
        this.tischTable = tisch;
        this.bestellDatum = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Gibt alle Getr�nke der Bestellung zur�ck.
     *
     * @return Hashtable die alle Getr�nke der Bestellung enth�lt
     */
    public Hashtable getAllDrinks() {
        return allDrinks;
    }

    /**
     * Gibt die Anzahl wie oft die Bestellung gesendet wurde.
     *
     * @return Wie oft wurde die Bestellung gesendet.
     */
    public int getGesendet() {
        return gesendet;
    }

    public boolean isGesendet() {
        if (gesendet == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmpty() {
        return allDrinks.isEmpty();
    }

    /**
     * Gibt den Namen des Tisches zur�ck von dem die Bestellung ausging.
     *
     * @return Von welchen Tisch die Bestellung ist.
     */
    public String getTisch() {
        return tisch;
    }

    /**
     * Gibt den Namen des PDAs von dem die Bestellung gesendet wurde.
     *
     * @return Von welchen PDA ist gesendet worden.
     */
    public String getBedienung() {
        return bedienung;
    }

    /**
     * Setzt wie oft die Bestellung abgesendet wurde.
     *
     * @param g.
     *            Setzt wie oft die Bestellung abgesendet wurde.
     */
    public void setGesendet(int g) {
        gesendet = g;
    }

    /**
     * Setzt den Tisch von wo die Bestellung ausgegangen ist.
     *
     * @param t.
     *            Setzt den Tisch von wo die Bestellung ausgegangen ist.
     */
    public void setTisch(String t) {
        tisch = t;
    }

    /**
     * Setzt den PDA von wo die Bestellung abgeschikt wurde.
     *
     * @param b.
     *            Setzt den PDA von wo die Bestellung abgeschikt wurde.
     */
    public void setBedienung(String b) {
        bedienung = b;
    }

    /**
     * Diese Methode wird jedes mal aufgerufen wenn die Bestellung gesendet
     * wird. Die gesendet-Variablen wird aufaddiert.
     */
    public void addGesendet() {
        gesendet++;
    }

    /**
     * Diese Methode schaut ob ein Getr�nk in der Bestellung schon existiert.
     *
     * @param d.
     *            Das Getr�nk wo kontrolliert werden soll.
     * @return true wenn das Getr�nk in der Bestellung existiert false wenn
     *         nicht.
     */
    public boolean containsDrink(Drink d) {
        return allDrinks.containsKey(d);
    }

    /**
     * Diese Methode f�gt das Getr�nk in der Bestellung hinzu.
     *
     * @param d .
     *            Das Getr�nk das hinzugef�gt werden soll.
     */
    public void addDrink(Drink d) {
        Integer alteAnzahl;
        if (allDrinks.containsKey(d)) {
            alteAnzahl = (Integer) allDrinks.get(d);
            int neueAnzahl = alteAnzahl.intValue();
            neueAnzahl = neueAnzahl + 1;
            allDrinks.put(d, new Integer(neueAnzahl));
        } else {
            allDrinks.put(d, new Integer(1));
            drinkSorter.insertElementAt(d, drinkSorter.size());
        }
    }

    public int getDrinkAnzahl(Drink d) {
        int intToReturn = 0;
        if (allDrinks.containsKey(d)) {
            Integer drinkAnzahl = (Integer) allDrinks.get(d);
            intToReturn = drinkAnzahl.intValue();
        }
        return intToReturn;
    }

    /**
     * Method getMaxDigitsFromAnzahl Zum Formatierungszwecken bei der
     * Rechnungserstellung
     */
    public int getMaxDigitsFromAnzahl() {
        int stellen = 0;
        Enumeration e = allDrinks.elements();
        while (e.hasMoreElements()) {
            Integer anzahl = (Integer) e.nextElement();
            String sAnzahl = anzahl.toString().trim();
            int dieseAnzahl = sAnzahl.length();
            if (stellen < dieseAnzahl) {
                stellen = dieseAnzahl;
            }
        }
        return stellen;
    }

    /**
     * Method getMaxDigitsFromPrice Zum Formatierungszwecken bei der
     * Rechnungserstellung
     */
    public int getMaxDigitsFromPrice() {
        int stellen = 0;
        NumberFormat form = null;
        form = NumberFormat.getInstance();
        form.setMinimumFractionDigits(2);
        Enumeration e = keys();
        while (e.hasMoreElements()) {
            Drink drink = (Drink) e.nextElement();
            Integer anzahl = (Integer) allDrinks.get(drink);
            Double preis = Double.valueOf(drink.getPreis());
            double summe = anzahl.intValue() * preis.doubleValue();
            String formatedPrice = form.format(summe);
            int dieseAnzahl = formatedPrice.trim().length();
            if (stellen < dieseAnzahl) {
                stellen = dieseAnzahl;
            }
        }
        return stellen;
    }

    public Enumeration keys() {
        return allDrinks.keys();
    }

    /**
     * Diese Methode entfernt ein Getr�nk aus der Bestellung.
     *
     * @param g.
     *            Das Getr�nk das entfernt werden soll.
     */
    public void removeDrink(Drink g) {
        Integer alteAnzahl;
        alteAnzahl = (Integer) allDrinks.get(g);
        int anzahl = alteAnzahl.intValue();
        if (anzahl == 1) {
            allDrinks.remove(g);
            drinkSorter.removeElement(g);
        } else {
            anzahl = anzahl - 1;
            allDrinks.put(g, new Integer(anzahl));
        }
    }

    /**
     * Diese Methode konvertiert die Bestellung zu einem Stringarray.
     *
     * @return Das Array das alle Getr�nke enth�lt.
     */
    public String[] convertToStringArray() {
        Enumeration e = allDrinks.keys();
        String[] alleGetraenke = new String[allDrinks.size()];
        for (int i = 0; e.hasMoreElements(); i++) {
            Drink d = (Drink) e.nextElement();
            int index = drinkSorter.indexOf(d);
            String alias = d.getName();
            Integer menge = (Integer) allDrinks.get(d);
            if (menge.intValue() < 10) {
                alias = menge.toString() + "   " + alias;
            } else {
                alias = menge.toString() + " " + alias;
            }
            alleGetraenke[index] = alias;
        }
        return alleGetraenke;
    }

    /**
     * Method findNewAddedDrinks Findet alle neuen Getr�nke die in der neuen
     * Bestellung hinzugef�gt wurden.
     */
    public static Hashtable findNewAddedDrinks(Bestellung alt, Bestellung neu) {
        Hashtable dazugekommeneDrinks = new Hashtable();
        Enumeration enumerationNewOrder = neu.keys();
        for (int i = 0; enumerationNewOrder.hasMoreElements(); i++) {
            Drink drink = (Drink) enumerationNewOrder.nextElement();
            int quantityFromNewOrder = neu.getDrinkAnzahl(drink);
            if (alt.containsDrink(drink)) {
                int quantityFromOldOrder = alt.getDrinkAnzahl(drink);
                if (quantityFromOldOrder < quantityFromNewOrder) {
                    int differenz = quantityFromNewOrder - quantityFromOldOrder;
                    dazugekommeneDrinks.put(drink, new Integer(differenz));
                }
            } else {
                System.out.println("findNewAddedDrinks:else: " + drink.getName());
                dazugekommeneDrinks.put(drink, new Integer(neu.getDrinkAnzahl(drink)));
            }
        }
        System.out.println("dazugekommene Drinks:" + dazugekommeneDrinks);
        return dazugekommeneDrinks;
    }

    /**
     * Method findRemovedDrinks. Findet alle entfernten Drinks die von der alten
     * Bestellung entfernt worden sind.
     */
    public static Hashtable findRemovedDrinks(Bestellung alt, Bestellung neu) {
        Hashtable entfernteDrinks = new Hashtable();
        Enumeration enumerationNewOrder = neu.keys();
        for (int i = 0; enumerationNewOrder.hasMoreElements(); i++) {
            Drink drink = (Drink) enumerationNewOrder.nextElement();
            int quantityFromNewOrder = neu.getDrinkAnzahl(drink);
            if (alt.containsDrink(drink)) {
                int quantityFromOldOrder = alt.getDrinkAnzahl(drink);
                if (quantityFromOldOrder > quantityFromNewOrder) {
                    int differenz = quantityFromOldOrder - quantityFromNewOrder;
                    entfernteDrinks.put(drink, new Integer(differenz));
                }
            }
        }
        Enumeration enumerationOldOrder = alt.keys();
        for (int i = 0; enumerationOldOrder.hasMoreElements(); i++) {
            Drink drink = (Drink) enumerationOldOrder.nextElement();
            if (!neu.containsDrink(drink)) {
                entfernteDrinks.put(drink, new Integer(alt.getDrinkAnzahl(drink)));
            } else {
            }
        }
        System.out.println("entfernteDrinks:" + entfernteDrinks);
        return entfernteDrinks;
    }

    /**
     * Method findEqualDrinks Findet alle Drinks die gleich geblieben sind. Also
     * die Drinks die sich nicht ver�ndert haben.
     */
    public static Hashtable findEqualDrinks(Bestellung alt, Bestellung neu) {
        Hashtable gleichgebliebeneDrinks = new Hashtable();
        Enumeration enumerationNewOrder = neu.keys();
        for (int i = 0; enumerationNewOrder.hasMoreElements(); i++) {
            Drink drink = (Drink) enumerationNewOrder.nextElement();
            int quantityFromNewOrder = neu.getDrinkAnzahl(drink);
            if (alt.containsDrink(drink)) {
                int quantityFromOldOrder = alt.getDrinkAnzahl(drink);
                if (quantityFromOldOrder == quantityFromNewOrder) {
                    gleichgebliebeneDrinks.put(drink, new Integer(quantityFromNewOrder));
                }
            }
        }
        System.out.println("gleichgebliebeneDrinks:" + gleichgebliebeneDrinks);
        return gleichgebliebeneDrinks;
    }

    public Vector toVector() {
        Vector vectorAllDrinks = new Vector(allDrinks.size());
        Object[] alleGetraenke = new Object[allDrinks.size()];
        Enumeration e = allDrinks.keys();
        for (int i = 0; e.hasMoreElements(); i++) {
            Drink drink = (Drink) e.nextElement();
            Integer anzahl = (Integer) allDrinks.get(drink);
            DrinkWithQuantity drinkWithQuantity = new DrinkWithQuantity(drink, anzahl);
            int index = drinkSorter.indexOf(drink);
            alleGetraenke[index] = drinkWithQuantity;
        }
        for (int i = 0; i < alleGetraenke.length; i++) {
            vectorAllDrinks.insertElementAt(alleGetraenke[i], i);
        }
        return vectorAllDrinks;
    }

    public int size() {
        return allDrinks.size();
    }

    public Tisch getTischTable() {
        return tischTable;
    }

    public Vector getDrinkSorter() {
        return drinkSorter;
    }

    /**
     * Das ueberscheibt die Methode toString() von Object
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer();
        returnString.append("Bestellung vom tisch: ");
        returnString.append(tisch);
        returnString.append(". Die Getr�nke der Bestellung sind:\n");
        Enumeration e = allDrinks.keys();
        int i = 1;
        while (e.hasMoreElements()) {
            Drink d = (Drink) e.nextElement();
            Integer anzahl = (Integer) allDrinks.get(d);
            returnString.append(i + "." + d.getName() + " Anzahl: " + anzahl.toString() + "\n");
            i++;
        }
        return returnString.toString();
    }

    /**
     * Das ueberscheibt die Methode equals() von Object
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Bestellung)) {
            return false;
        }
        Bestellung andereBestellung = (Bestellung) obj;
        if (!this.tisch.equals(andereBestellung.tisch)) {
            return false;
        }
        if (andereBestellung.allDrinks.size() != this.allDrinks.size()) {
            return false;
        } else {
            Enumeration eThis = this.allDrinks.keys();
            while (eThis.hasMoreElements()) {
                Drink d = (Drink) eThis.nextElement();
                if (andereBestellung.allDrinks.containsKey(d)) {
                    Integer andereAnzahl = (Integer) andereBestellung.allDrinks.get(d);
                    Integer dieseAnzahl = (Integer) this.allDrinks.get(d);
                    if (andereAnzahl.intValue() != dieseAnzahl.intValue()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Das ueberscheibt die Methode hashCode() von Object
     */
    public int hashCode() {
        int ergebnis = 0;
        ergebnis = (HAUPT_HACK * ergebnis) + this.allDrinks.hashCode();
        ergebnis = (HAUPT_HACK * ergebnis) + this.drinkSorter.hashCode();
        ergebnis = (HAUPT_HACK * ergebnis) + this.gesendet;
        ergebnis = (HAUPT_HACK * ergebnis) + this.tisch.hashCode();
        ergebnis = (HAUPT_HACK * ergebnis) + this.bedienung.hashCode();
        ergebnis = (HAUPT_HACK * ergebnis) + this.tischTable.hashCode();
        ergebnis = (HAUPT_HACK * ergebnis) + this.bestell_id;
        ergebnis = (HAUPT_HACK * ergebnis) + this.bestellDatum.hashCode();
        return ergebnis;
    }

    /**
     * Das ueberscheibt die Methode clone() von Object
     */
    public Object clone() throws CloneNotSupportedException {
        Bestellung copy = (Bestellung) super.clone();
        Hashtable clonedAllDrinks = (Hashtable) copy.allDrinks.clone();
        Vector clonedDrinkSorter = (Vector) copy.getDrinkSorter().clone();
        int clonedGesendet = copy.getGesendet();
        String tisch = copy.getTisch();
        int clonedBestell_id = copy.getBestell_id();
        Timestamp clonedTimestamp = (Timestamp) copy.bestellDatum;
        StringBuffer sbTisch = null;
        if (tisch != null) {
            sbTisch = new StringBuffer(tisch);
        } else {
            sbTisch = new StringBuffer();
        }
        tisch = sbTisch.toString();
        Tisch clonedTisch = copy.getTischTable();
        String bedienung = getBedienung();
        StringBuffer sbBedienung = null;
        if (bedienung != null) {
            sbBedienung = new StringBuffer(getBedienung());
        } else {
            sbBedienung = new StringBuffer();
        }
        bedienung = sbBedienung.toString();
        copy = new Bestellung(clonedAllDrinks, clonedDrinkSorter, clonedGesendet, tisch, clonedTisch, bedienung, clonedBestell_id);
        return copy;
    }

    public int getBestell_id() {
        return bestell_id;
    }

    public void setBestell_id(int bestell_id) {
        this.bestell_id = bestell_id;
    }

    public Timestamp getBestellDatum() {
        return bestellDatum;
    }
}
