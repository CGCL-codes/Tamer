package org.pandcorps.core.img;

public class FinPancolor extends Pancolor {

    public FinPancolor(final short[] rgb) {
        super(rgb);
    }

    public FinPancolor(final short r, final short g, final short b, final short a) {
        super(r, g, b, a);
    }

    public final void setR(final short r) {
        throw new UnsupportedOperationException();
    }

    public final void setG(final short g) {
        throw new UnsupportedOperationException();
    }

    public final void setB(final short b) {
        throw new UnsupportedOperationException();
    }

    public final void setA(final short a) {
        throw new UnsupportedOperationException();
    }
}
