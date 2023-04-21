package org.jazzteam.edu.lang.interfaces;

public class Bus implements Auto {

    private String marka;

    private int kol_pas;

    private int kolesa;

    public Bus() {
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public void setKol_pas(int kol_pas) {
        this.kol_pas = kol_pas;
    }

    public void setKolesa(int kolesa) {
        this.kolesa = kolesa;
    }

    @Override
    public String getMark() {
        return marka;
    }

    @Override
    public int getPassengers(int n) {
        n = kol_pas;
        return n;
    }

    @Override
    public int getWheels(int n) {
        n = kolesa;
        return n;
    }
}
