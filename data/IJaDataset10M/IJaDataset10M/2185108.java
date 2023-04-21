package de.qnerd.rpgBot.gameobjects;

/**
 * @author Sven Kuhnert
 *
 * 
 * 
 */
public class Zauber {

    private int multiMagic = 0;

    private int manaKosten = 0;

    private String gattung = "";

    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "[" + name + "]" + "[" + gattung + "]" + "[" + manaKosten + "]" + "[" + multiMagic + "]";
    }

    public int getMultiMagic() {
        return multiMagic;
    }

    public int getManaCost() {
        return manaKosten;
    }

    public String getGattung() {
        return gattung;
    }

    public int getManaKosten() {
        return manaKosten;
    }

    public void setManaKosten(int manaKosten) {
        this.manaKosten = manaKosten;
    }

    public void setGattung(String gattung) {
        this.gattung = gattung;
    }

    public void setMultiMagic(int multiMagic) {
        this.multiMagic = multiMagic;
    }
}
