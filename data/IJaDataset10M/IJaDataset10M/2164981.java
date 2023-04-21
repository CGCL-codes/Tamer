package bfpl.bean;

import base.util.ModelTime;

public class ZuglaufEintrag implements Comparable<ZuglaufEintrag> {

    public ZuglaufEintrag(final Zuglaufstelle zst, final String gl, final ModelTime an, final ModelTime ab, final boolean durch) {
        setZuglaufstelle(zst);
        setGleis(gl);
        setAn(an);
        setAb(ab);
        setDurchfahrt(durch);
    }

    public int compareTo(final ZuglaufEintrag o) {
        ModelTime t1 = getAn() != null ? getAn() : getAb();
        ModelTime t2 = o.getAn() != null ? o.getAn() : o.getAb();
        int d = t2.compareTo(t1);
        return d;
    }

    public String getGleis() {
        return gleis;
    }

    public void setGleis(final String g) {
        gleis = g;
    }

    public ModelTime getAn() {
        return an;
    }

    public void setAn(final ModelTime an) {
        this.an = an;
    }

    public ModelTime getAb() {
        return ab;
    }

    public void setAb(final ModelTime ab) {
        this.ab = ab;
    }

    public Zuglaufstelle getZuglaufstelle() {
        return zuglaufstelle;
    }

    public void setZuglaufstelle(final Zuglaufstelle zuglaufstelle) {
        this.zuglaufstelle = zuglaufstelle;
    }

    public boolean isDurchfahrt() {
        return durchfahrt;
    }

    public void setDurchfahrt(final boolean durchfahrt) {
        this.durchfahrt = durchfahrt;
    }

    @Override
    public String toString() {
        return "(" + (durchfahrt ? "-->" : an) + "-" + ab + "; " + gleis + ")";
    }

    private Zuglaufstelle zuglaufstelle;

    private String gleis;

    private ModelTime an;

    private ModelTime ab;

    private boolean durchfahrt;
}
