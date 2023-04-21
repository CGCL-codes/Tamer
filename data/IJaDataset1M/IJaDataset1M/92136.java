package com.nexirius.print;

import java.util.Enumeration;
import java.util.Vector;

public class FixPrintableList {

    private Vector v = new Vector();

    private Enumeration e = null;

    public FixPrintable first() {
        e = v.elements();
        return next();
    }

    public FixPrintable next() {
        if (e != null && e.hasMoreElements()) {
            return (FixPrintable) e.nextElement();
        }
        e = null;
        return null;
    }

    public void append(FixPrintable t) {
        v.addElement(t);
    }

    public int getSize() {
        return v.size();
    }
}
