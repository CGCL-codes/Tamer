package com.dogankaya.edeprem.client.model;

import java.util.Date;

public class Deprem {

    Date tarih;

    String lat;

    String lon;

    Double derinlik;

    Double buyukluk;

    String aciklama;

    public Deprem(Date tarih, String lat, String lon, Double derinlik, Double buyukluk, String aciklama) {
        super();
        this.tarih = tarih;
        this.lat = lat;
        this.lon = lon;
        this.derinlik = derinlik;
        this.buyukluk = buyukluk;
        this.aciklama = aciklama;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Double getDerinlik() {
        return derinlik;
    }

    public void setDerinlik(Double derinlik) {
        this.derinlik = derinlik;
    }

    public Double getBuyukluk() {
        return buyukluk;
    }

    public void setBuyukluk(Double buyukluk) {
        this.buyukluk = buyukluk;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    @Override
    public String toString() {
        String result = "<deprem>";
        result += "<tarih>" + getTarih().toLocaleString() + "<tarih>";
        result += "<lat>" + getLat() + "</lat>";
        result += "<lon>" + getLon() + "</lon>";
        result += "<derinlik>" + getDerinlik() + "</derinlik>";
        result += "<buyukluk>" + getBuyukluk() + "</buyukluk>";
        result += "<aciklama>" + getAciklama() + "</aciklama>";
        result += "</deprem>";
        return super.toString();
    }
}
