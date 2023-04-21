package org.cresques.px;

public class PxSize implements ISize {

    private int pw = 0;

    private int ph = 0;

    public PxSize(int w, int h) {
        this.pw = w;
        this.ph = h;
    }

    public int w() {
        return pw;
    }

    public int w(int w) {
        pw = w;
        return pw;
    }

    public int h() {
        return ph;
    }

    public int h(int h) {
        ph = h;
        return ph;
    }
}
