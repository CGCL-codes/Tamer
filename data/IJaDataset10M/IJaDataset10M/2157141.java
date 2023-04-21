package com.cafe.serve.server;

/**
 * @author Asterios Raptis Eine Klasse die ein Drink-Objekt kapseln soll. Also ein Getr�nk in der Getr�nkekarte.
 */
public class Drink implements java.io.Serializable, Cloneable {

    /**
     * Die statische serialVersionUID f�r diese Klasse
     */
    static final long serialVersionUID = -7978489558769667877L;

    private static final int HAUPT_HACK = 1000003;

    /**
     * Die id fuer die DB.
     */
    private int id;

    /**
     * Der Name des Getr�nks.
     */
    private String name;

    /**
     * Die Kategorie des Getr�nkes.
     */
    private String kategory;

    /**
     * Die Gr��e bzw. der Inhalt des Getr�nkes als String.
     */
    private String groesse;

    /**
     * Die Gr��e bzw. der Inhalt des Getr�nkes als Double.F�r die DB.
     */
    private Double doublegroesse;

    /**
     * Der Preis des Getr�nkes als String.
     */
    private String preis;

    /**
     * Der Preis des Getr�nkes als Double. F�r die DB.
     */
    private Double doublepreis;

    /**
     * Die <code>mehrwertSteuer</code> f�r das Getr�nk.
     */
    private int mehrwertSteuer;

    /**
     * Konstruiert ein leeres Drink-Objekt.
     */
    public Drink() {
    }

    public Drink(String id, String name, String kategory, String groesse, String preis, String mwst) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.kategory = kategory;
        this.groesse = groesse;
        this.doublegroesse = Double.valueOf(groesse);
        this.preis = preis;
        this.doublepreis = Double.valueOf(preis);
        this.mehrwertSteuer = Integer.parseInt(mwst);
    }

    public Drink(String name, String kategory, String groesse, String preis, String mwst) {
        this.name = name;
        this.kategory = kategory;
        this.groesse = groesse;
        this.doublegroesse = Double.valueOf(groesse);
        this.preis = preis;
        this.doublepreis = Double.valueOf(preis);
        this.mehrwertSteuer = Integer.parseInt(mwst);
    }

    /**
     * Konstuiert ein Drink-Objekt mit Namen, Kategorie, Gr��e und den Preis.
     *
     * @param n Der Name des Getr�nks.
     * @param k Die Kategorie des Getr�nkes.
     * @param g Die Gr��e bzw. der Inhalt des Getr�nkes.
     * @param p Der Preis des Getr�nkes.
     */
    public Drink(String n, String k, String g, String p) {
        name = n;
        kategory = k;
        groesse = g;
        preis = p;
    }

    /**
     * Konstuiert ein Drink-Objekt mit Namen, Kategorie und der Gr��e.
     *
     * @param n Der Name des Getr�nks.
     * @param k Die Kategorie des Getr�nkes.
     * @param g Die Gr��e bzw. der Inhalt des Getr�nkes.
     */
    public Drink(String n, String k, String g) {
        name = n;
        kategory = k;
        groesse = g;
    }

    /**
     * Konstuiert ein Drink-Objekt mit Namen und der Kategorie.
     *
     * @param n Der Name des Getr�nks.
     * @param k Die Kategorie des Getr�nkes.
     */
    public Drink(String n, String k) {
        name = n;
        kategory = k;
    }

    /**
     * Konstuiert ein Drink-Objekt mit den Namen.
     *
     * @param n Der Name des Getr�nks.
     */
    public Drink(String n) {
        name = n;
    }

    /**
     * Gibt die id von dem Drink-objektes zur�ck.
     *
     * @return Die id des Getr�nkes.
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt den Namen des Drink-Objektes zur�ck.
     *
     * @return Den Namen des Getr�nkes.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Kategorie des Drink-Objektes zur�ck.
     *
     * @return Die Kategorie des Getr�nkes.
     */
    public String getKategory() {
        return kategory;
    }

    /**
     * Gibt die Gr��e des Drink-Objektes zur�ck.
     *
     * @return Die Gr��e des Getr�nkes.
     */
    public String getGroesse() {
        return groesse;
    }

    /**
     * Gibt den Preis des Drink-Objektes zur�ck.
     *
     * @return Der Preis des Getr�nkes.
     */
    public String getPreis() {
        return preis;
    }

    /**
     * Setzt die id des Drink-objektes.
     *
     * @param i Die id die gesetzt werden soll.
     */
    public void setId(int i) {
        id = i;
    }

    /**
     * Setzt den Namen des Drink-Objektes.
     *
     * @param n Der Name der gesetzt werden soll.
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * Setzt die Kategorie des Drink-Objektes.
     *
     * @param k Die Kategorie die gesetzt werden soll.
     */
    public void setKategory(String k) {
        kategory = k;
    }

    /**
     * Setzt die Gr��e des Drink-Objektes.
     *
     * @param g Die Gr��e die gesetzt werden soll.
     */
    public void setGroesse(String g) {
        groesse = g;
    }

    /**
     * Setzt den Preis des Drink-Objektes.
     *
     * @param p Der Preis der gesetzt werden soll.
     */
    public void setPreis(String p) {
        preis = p;
    }

    /**
     * @return Returns the mehrwertSteuer.
     */
    public int getMehrwertSteuer() {
        return mehrwertSteuer;
    }

    /**
     * @param mehrwertSteuer The mehrwertSteuer to set.
     */
    public void setMehrwertSteuer(int mehrwertSteuer) {
        this.mehrwertSteuer = mehrwertSteuer;
    }

    /**
     * @return Returns the doublegroesse.
     */
    public Double getDoublegroesse() {
        return doublegroesse;
    }

    /**
     * @param doublegroesse The doublegroesse to set.
     */
    public void setDoublegroesse(Double doublegroesse) {
        this.doublegroesse = doublegroesse;
    }

    /**
     * @return Returns the doublepreis.
     */
    public Double getDoublepreis() {
        return doublepreis;
    }

    /**
     * @param doublepreis The doublepreis to set.
     */
    public void setDoublepreis(Double doublepreis) {
        this.doublepreis = doublepreis;
    }

    public Object clone() throws CloneNotSupportedException {
        Drink copy = new Drink();
        copy.id = this.id;
        copy.name = this.name;
        copy.kategory = this.kategory;
        copy.groesse = this.groesse;
        copy.doublegroesse = this.doublegroesse;
        copy.preis = this.preis;
        copy.doublepreis = this.doublepreis;
        copy.mehrwertSteuer = this.mehrwertSteuer;
        return copy;
    }

    /**
     * Diese Methode �berschreibt die geerbte equals()-methode von Object.
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Drink)) {
            return false;
        }
        Drink other = (Drink) o;
        if (this.id != other.id) {
            return false;
        }
        if (!name.equals(other.getName())) {
            return false;
        }
        if (!kategory.equals(other.getKategory())) {
            return false;
        }
        if (!groesse.equals(other.getGroesse())) {
            return false;
        }
        if (!preis.equals(other.getPreis())) {
            return false;
        }
        return true;
    }

    /**
     * Das ueberscheibt die Methode hashCode() von Object
     */
    public int hashCode() {
        int ergebnis = 0;
        ergebnis = (HAUPT_HACK * ergebnis) + id;
        ergebnis = (HAUPT_HACK * ergebnis) + name.hashCode();
        ergebnis = (HAUPT_HACK * ergebnis) + kategory.hashCode();
        ergebnis = (HAUPT_HACK * ergebnis) + groesse.hashCode();
        if (doublegroesse != null) {
            ergebnis = (HAUPT_HACK * ergebnis) + doublegroesse.hashCode();
        }
        ergebnis = (HAUPT_HACK * ergebnis) + preis.hashCode();
        if (doublepreis != null) {
            ergebnis = (HAUPT_HACK * ergebnis) + doublepreis.hashCode();
        }
        ergebnis = (HAUPT_HACK * ergebnis) + mehrwertSteuer;
        return ergebnis;
    }

    public Object[] drinkToObjectArray() {
        Object[] rowData = { new Integer(this.id), this.name, this.kategory, new Double(this.groesse), new Double(this.preis), new Integer(this.mehrwertSteuer) };
        return rowData;
    }

    /**
     * Diese Methode �berschreibt die geerbte toString()-Methode von Object.
     */
    public String toString() {
        return "ID: " + getId() + " Name: " + getName() + " Kategorie: " + getKategory() + " Inhalt: " + getGroesse() + " Preis: " + getPreis() + "Mehrwertsteuer: " + this.getMehrwertSteuer();
    }
}
