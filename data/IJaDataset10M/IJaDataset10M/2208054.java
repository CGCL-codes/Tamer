package ssgpp.sozluk;

import ch.alari.sacre.Token;

/**
 *
 * @author Onur Derin <oderin at users.sourceforge.net>
 */
public class Baslik extends Token {

    private String baslik = null;

    /**
     * relative path, e.g. show.asp?t=...
     */
    private String baslikAdresi = null;

    public Baslik(String baslik, String baslikAdresi) {
        super(Token.DATA);
        this.baslik = baslik;
        this.baslikAdresi = baslikAdresi;
    }

    public Baslik(int type) {
        super(type);
    }

    public String getBaslik() {
        return baslik;
    }

    public String getBaslikAdresi() {
        return baslikAdresi;
    }

    @Override
    public String toString() {
        return "[Baslik: " + baslik + " " + baslikAdresi + "]";
    }

    @Override
    public boolean equals(Object b) {
        if (this == b) return true; else if (b == null) return false; else if (b instanceof Baslik && ((Baslik) b).getBaslik().equals(baslik)) return true; else return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.baslik != null ? this.baslik.hashCode() : 0);
        hash = 89 * hash + (this.baslikAdresi != null ? this.baslikAdresi.hashCode() : 0);
        return hash;
    }
}
