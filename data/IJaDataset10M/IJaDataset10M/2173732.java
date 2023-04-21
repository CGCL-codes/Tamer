package core;

import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Kontakt implements Serializable {

    private String adres, opis, imie;

    private Typ typ;

    private Stan st;

    private boolean nowaWiadomosc = false;

    public Kontakt() {
        adres = "Kontakt";
        opis = "opis";
        st = Stan.NIEOBECNY;
    }

    /**
 * @param adres (stefan@jabberpl.org)
 * @param opis opis np. spie
 * @param typ typ uzywanego protokolu
 * @param imie nazwa wyswietlana kontaktu
 */
    public Kontakt(String adres, String opis, Typ typ, String imie) {
        this.adres = adres;
        this.opis = opis;
        this.typ = typ;
        st = Stan.ROZLACZONY;
        this.imie = imie;
    }

    public Icon getIkona() {
        return st.getIcon();
    }

    public String getAdres() {
        return adres;
    }

    public String getOpis() {
        return opis;
    }

    public String toString() {
        return adres;
    }

    public void setStan(Stan st) {
        this.st = st;
    }

    public Stan getStan() {
        return st;
    }

    public String getImie() {
        return imie;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void newMessage() {
        nowaWiadomosc = true;
    }

    public void readMessage() {
        nowaWiadomosc = false;
    }

    public boolean isNowaWiadomosc() {
        return nowaWiadomosc;
    }
}
