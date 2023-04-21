package com.gestioni.adoc.aps.system.services.documento.stato;

public class Stato {

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getDescr() {
        return _descr;
    }

    public void setDescr(String descr) {
        this._descr = descr;
    }

    private int _id;

    private String _descr;

    public static final int LIBERO = 1;

    public static final int CHECKOUT = 2;

    public static final int BLOCCATO = 3;

    public static final int PROTOCOLLATO = 10;
}
