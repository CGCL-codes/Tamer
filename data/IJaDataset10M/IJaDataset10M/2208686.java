package org.schnelln.core.basic;

import java.io.Serializable;

/**
 * This class provides a simple implementation of a object "Card".<br>
 * A card has the follow internal variables:
 * <ul>
 * <li>kartenBezeichnung:String - the common name of the card</li>
 * <li>kartenWert:int - the value of the card</li>
 * <li>kartenFarbe:Farbe - the color of the card</li>
 * </ul>
 * 
 * This implementation provides a standard constructor, inherit objects via a
 * parameter injection.
 * 
 * @version 1.0
 * @category basic
 * 
 * @author Tischler Lukas [tischler.lukas_at_gmail.com]
 * 
 */
public class Karte implements Comparable<Karte>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Die Bezeichnung der Karte, kann auch leerer String sein
	 */
    private String kartenBezeichnung = "";

    /**
	 * Der Wert der Karte. Je nach Implementierung k&ouml;nnen dies
	 * unterschiedliche Werte sein.
	 */
    private int kartenWert = -1;

    /**
	 * Diese Farbe der Karte. Je nach Implementierung variieren diese.
	 */
    private Farbe kartenFarbe = null;

    /** The owner. Default is <code>-1</code> */
    private int owner = -1;

    /**
	 * Der Default-Konstruktor f&uuml;r eventuelle Injectons.
	 */
    public Karte() {
    }

    /**
	 * Ein Konstruktor, welcher eine Karte mit Kartenwert und Farbe instanziert.
	 * 
	 * @param wert
	 *            Der Wert der Karte
	 * @param f
	 *            Die Farbe der Karte
	 */
    public Karte(int wert, Farbe f) {
        this.kartenWert = wert;
        this.kartenFarbe = f;
    }

    /**
	 * Diese Methode retourniert die Bezeichnung der Karte
	 * 
	 * @return Die Bezeichnung der Karte
	 */
    public String getKartenBezeichnung() {
        return kartenBezeichnung;
    }

    /**
	 * Diese Methode setzt die Bezeichnung der Karte
	 * 
	 * @param kartenBezeichnung
	 *            Die neue Bezeichnung der Karte
	 */
    public void setKartenBezeichnung(String kartenBezeichnung) {
        this.kartenBezeichnung = kartenBezeichnung;
    }

    /**
	 * Diese Methode retourniert die Farbe der Karte
	 * 
	 * @return Die Farbe der Karte
	 */
    public Farbe getKartenFarbe() {
        return kartenFarbe;
    }

    /**
	 * Diese Methode setzt die Farbe der Karte
	 * 
	 * @param kartenFarbe
	 *            Die neue Farbe der Karte
	 */
    public void setKartenFarbe(Farbe kartenFarbe) {
        this.kartenFarbe = kartenFarbe;
    }

    /**
	 * Diese Methode retourniert den Wert der Karte
	 * 
	 * @return Der Wert der Karte
	 */
    public int getKartenWert() {
        return kartenWert;
    }

    /**
	 * Diese Methode setzt den Wert der Karte neu
	 * 
	 * @param kartenWert
	 *            Der neue Wert der Karte
	 */
    public void setKartenWert(int kartenWert) {
        this.kartenWert = kartenWert;
    }

    /**
	 * Diese Methode von der Schnittstelle Comparable vergleicht zwei Karten.<br>
	 * Sind die Karten von derselben Farbe, werden die Werte verglichen, falls
	 * this kleiner als that ist, wird -1 retourniert, falls this
	 * gr&ouml;&szlig;er als that ist wird 1 retourniert, anderfalls 0.<br>
	 * Sind die Farben nicht gleich, wird angenommen, dass this
	 * gr&ouml;&szlig;er als that ist.
	 * 
	 * @param o
	 * 
	 * @return 0 falls die Karten dieselben sind, -1 falls this kleiner als o
	 *         ist, ansonsten 1.
	 */
    public int compareTo(Karte o) {
        int result = 0;
        if (kartenFarbe.equals(o.kartenFarbe)) {
            if (kartenWert < o.kartenWert) result = -1; else if (kartenWert > o.kartenWert) result = 1;
        } else {
            result = 1;
        }
        return result;
    }

    /**
	 * Diese Methode &uuml;berschreibt die Methode hashCode und bewirkt, dass
	 * die internen Werte als Grundlage zur Berechnung des hashCodes dienen.
	 * Sind somit alle Variablen zweier Objekte intern mit demselben Wert, wird
	 * auch hashCode dieselben Ergebnisse zur&uuml;ckliefern
	 * 
	 * @see java.lang.Object#hashCode()
	 * @return Den HashCode eines Objektes
	 */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((kartenFarbe == null) ? 0 : kartenFarbe.hashCode());
        result = PRIME * result + kartenWert;
        return result;
    }

    /**
	 * Diese Methode &uuml;berschreibt die Methode equals und bewirkt, dass die
	 * internen Werte miteinander verglichen werden. Als Resultat wird true
	 * zur&uuml;ckgeliefert, genau dann wenn die internen Werte
	 * &uuml;bereinstimmen.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 *            Das Objekt das mit diesem verglichen werden soll
	 * @return true, falls alle internen Werte &uuml;bereinstimmen, false
	 *         ansonsten
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Karte other = (Karte) obj;
        if (kartenFarbe == null) {
            if (other.kartenFarbe != null) return false;
        } else if (!kartenFarbe.equals(other.kartenFarbe)) return false;
        if (kartenWert != other.kartenWert) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Value: " + this.kartenWert + ", Color: " + this.kartenFarbe + ", Name: " + this.kartenBezeichnung;
    }

    public Karte clone() {
        Karte k = new Karte(this.kartenWert, this.kartenFarbe);
        k.setKartenBezeichnung(this.kartenBezeichnung);
        return k;
    }

    /**
	 * Gets the owner.
	 * 
	 * @return the owner
	 */
    public int getOwner() {
        return owner;
    }

    /**
	 * Sets the owner.
	 * 
	 * @param owner
	 *            the owner to set
	 */
    public void setOwner(int owner) {
        this.owner = owner;
    }
}
