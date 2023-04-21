package it.schedesoftware.old;

import it.schedesoftware.old.Quota;

/**
 *
 * @author  andrea
 */
public class Quota {

    int count;

    long position;

    long positionq;

    char[] importo = new char[15];

    char[] giorno = new char[2];

    char[] mese = new char[2];

    char[] anno = new char[4];

    String varie;

    boolean state = false;

    /** Creates a new instance of Quota */
    public Quota() {
        state = false;
    }

    public Quota(String varie) {
        this.varie = varie;
        count = 0;
        state = true;
    }

    public Quota(Quota q) {
        this.count = q.count;
        this.position = q.position;
        this.positionq = q.positionq;
        this.importo = q.importo;
        this.giorno = q.giorno;
        this.mese = q.mese;
        this.anno = q.anno;
        state = false;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPosition(long p) {
        this.position = p;
    }

    public void setPositionq(long p) {
        this.positionq = p;
    }

    public void setGiorno(char[] c) {
        this.giorno = c;
    }

    public void setMese(char[] c) {
        this.mese = c;
    }

    public void setAnno(char[] c) {
        this.anno = c;
    }

    public void setImporto(char[] c) {
        this.importo = c;
    }

    public void setGiorno(String c) {
        this.giorno = charLenght(c, 2);
    }

    public void setMese(String c) {
        this.mese = charLenght(c, 2);
    }

    public void setAnno(String c) {
        this.anno = charLenght(c, 4);
    }

    public void setImporto(String c) {
        this.importo = charLenght(c, 15);
    }

    public int getCount() {
        return count;
    }

    public long getPosition() {
        return position;
    }

    public long getPositionq() {
        return positionq;
    }

    public char[] getGiorno() {
        return giorno;
    }

    public char[] getMese() {
        return mese;
    }

    public char[] getAnno() {
        return anno;
    }

    public char[] getImporto() {
        return importo;
    }

    public String getGiornoS() {
        String s = "";
        for (int i = 0; i < 2; i++) s = s + giorno[i];
        return s;
    }

    public String getMeseS() {
        String s = "";
        for (int i = 0; i < 2; i++) s = s + mese[i];
        return s;
    }

    public String getAnnoS() {
        String s = "";
        for (int i = 0; i < 4; i++) s = s + anno[i];
        return s;
    }

    public String getImportoS() {
        String s = "";
        for (int i = 0; i < 15; i++) s = s + importo[i];
        return s;
    }

    public char[] charLenght(String s, int l) {
        char[] c = new char[l];
        int i, len;
        char[] st = s.toCharArray();
        len = s.length();
        for (i = 0; i < l && i < len; i++) c[i] = st[i];
        if (i < l) for (; i < l; i++) c[i] = ' ';
        return c;
    }

    public String toString() {
        if (state) return varie;
        String s = "";
        for (int i = 0; i < 2; i++) s = s + giorno[i];
        s = s + "/";
        for (int i = 0; i < 2; i++) s = s + mese[i];
        s = s + "/";
        for (int i = 0; i < 4; i++) s = s + anno[i];
        s = s + "  -  ";
        for (int i = 0; i < 15; i++) s = s + importo[i];
        return s;
    }
}
