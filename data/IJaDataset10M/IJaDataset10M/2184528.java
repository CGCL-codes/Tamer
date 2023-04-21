package com.memoire.yapod;

import java.util.Enumeration;
import java.util.Vector;

public class YapodCountQuery extends YapodAbstractQuery {

    public YapodCountQuery(YapodQuery _previous) {
        super(_previous);
    }

    protected final Enumeration query(Enumeration e) {
        int n = 0;
        while (e.hasMoreElements()) {
            n++;
            e.nextElement();
        }
        Vector r = new Vector(1);
        r.addElement(new Integer(n));
        return r.elements();
    }

    public String toString() {
        return "count(" + getPrevious() + ")";
    }
}
