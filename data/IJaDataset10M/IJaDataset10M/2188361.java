package vunit;

/**
 * die Klasse SecondBadMtst dient zu Mtstzwecken
 */
public class SecondBadMtst {

    /**
   * eindeutiger SecondBadMtst-Key
   */
    public int coKey;

    /**
   * eindeutiger SecondBadMtst-Name
   */
    public String name;

    /** 
   * Konstruktor mit Moeglichkeit zur Vorbelegung der Attribute
   */
    public SecondBadMtst(int coKey, String name) {
        this.coKey = coKey;
        this.name = name;
    }

    /**
   * kein leerer Konstruktor !!
   */
    public SecondBadMtst() {
    }

    /**
   * gibt die String-Darstellung eines
   * Fixtures zurueck
   */
    public String toString() {
        return "[" + coKey + "," + name + "]";
    }

    /**
   * fiexture stuerzt bei Berechnung ab
   */
    public Object mobjbadAbsturz() {
        int a = 0;
        if (1 / a == 1) a = 1;
        return null;
    }

    /**
   * FixtureName und Methodenname stimmen nicht ueberein
   */
    public Object mobjallesOk() {
        return new SecondBadMtst(77, "SBT");
    }
}
