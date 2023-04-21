package pl.wcislo.sbql4j.java.test.model.firma;

import java.util.ArrayList;
import java.util.List;

public class Firma {

    private String nazwa;

    private List<Adres> adres = new ArrayList<Adres>();

    private List<Zatrudnienie> zatrudnia = new ArrayList<Zatrudnienie>();

    public Firma(String nazwa) {
        super();
        this.nazwa = nazwa;
    }

    public Firma() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Adres> getAdres() {
        return adres;
    }

    public void setAdres(List<Adres> adresySiedzib) {
        this.adres = adresySiedzib;
    }

    public List<Zatrudnienie> getZatrudnia() {
        return zatrudnia;
    }

    public void setZatrudnia(List<Zatrudnienie> zatrudnia) {
        this.zatrudnia = zatrudnia;
    }
}
