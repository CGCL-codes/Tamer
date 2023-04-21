package rene.lister;

import java.awt.Color;

public class StringElement implements Element {

    public String S;

    public Color C;

    public StringElement(final String s, final Color c) {
        S = s;
        C = c;
    }

    public StringElement(final String s) {
        this(s, null);
    }

    public String getElementString() {
        return S;
    }

    public String getElementString(final int state) {
        return S;
    }

    public Color getElementColor() {
        return C;
    }
}
