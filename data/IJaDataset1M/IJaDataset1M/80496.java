package objektuak;

import java.util.*;

public class Txartela {

    private int id;

    private Calendar gaituData;

    private Calendar desgaituData;

    private Erabiltzailea erabiltzailea;

    private Vector<SarbideEskaera> sarbideEskaerak;

    public Txartela(int txartelId, Erabiltzailea txartelErabiltzailea, Calendar txGaituData) {
        this.id = txartelId;
        this.gaituData = txGaituData;
        this.erabiltzailea = txartelErabiltzailea;
        this.erabiltzailea.setTxartela(this);
        this.sarbideEskaerak = new Vector<SarbideEskaera>();
    }

    public int getId() {
        return id;
    }

    public Calendar getGaituData() {
        return gaituData;
    }

    public Calendar getDesgaituData() {
        return this.desgaituData;
    }

    public Erabiltzailea getErabiltzailea() {
        return this.erabiltzailea;
    }

    public Vector<SarbideEskaera> getSarbideEskaerak() {
        return sarbideEskaerak;
    }

    public void addSarbideEskaerak(SarbideEskaera txartelSarbideEskaera) {
        sarbideEskaerak.add(txartelSarbideEskaera);
    }
}
