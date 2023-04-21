package fr.ign.cogit.geoxygene.example;

import fr.ign.cogit.geoxygene.feature.FT_Feature;

/**
 * Exemple de classe permettant de stocker une geometrie, et differents
 * attributs. La geometrie est heritee de FT_Feature. La table correspondante
 * est definie dans le script "resultat.sql". Le mapping correspondant est
 * defini le fichier "repository_resultat.xml".
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 * 
 */
public class Resultat extends FT_Feature {

    protected double double1;

    public double getDouble1() {
        return this.double1;
    }

    public void setDouble1(double theDouble) {
        this.double1 = theDouble;
    }

    protected int int1;

    public int getInt1() {
        return this.int1;
    }

    public void setInt1(int theInt) {
        this.int1 = theInt;
    }

    protected String string1;

    public String getString1() {
        return this.string1;
    }

    public void setString1(String theString) {
        this.string1 = theString;
    }

    protected boolean boolean1;

    public boolean getBoolean1() {
        return this.boolean1;
    }

    public void setBoolean1(boolean theBoolean) {
        this.boolean1 = theBoolean;
    }
}
