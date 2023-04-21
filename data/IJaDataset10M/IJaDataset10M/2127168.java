package de.qnerd.rpgBot.gameobjects;

/**
 * @author Sven Kuhnert
 *
 * 
 * 
 */
public class Waffe {

    private int multiPower = 0;

    private int multiDex = 0;

    private String gattung = "";

    private String name = "";

    public String toString() {
        return "[" + name + "]" + "[" + gattung + "]" + "[" + multiDex + "]" + "[" + multiPower + "]";
    }

    public String getGattung() {
        return gattung;
    }

    public void setGattung(String gattung) {
        this.gattung = gattung;
    }

    public int getMultiDex() {
        return multiDex;
    }

    public void setMultiDex(int multiDex) {
        this.multiDex = multiDex;
    }

    public int getMultiPower() {
        return multiPower;
    }

    public void setMultiPower(int multiPower) {
        this.multiPower = multiPower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
