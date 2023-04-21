package com.memoire.yapod;

import java.util.Enumeration;
import java.util.Vector;

public class YapodMaxQuery extends YapodAbstractQuery {

    public YapodMaxQuery(YapodQuery _previous) {
        super(_previous);
    }

    protected final Enumeration query(Enumeration e) {
        Number max_double = null;
        String max_string = null;
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o instanceof Number) {
                Number v = (Number) o;
                if (max_double == null) max_double = v; else if (v.doubleValue() > max_double.doubleValue()) max_double = v;
            }
            if (o instanceof String) {
                String v = (String) o;
                if (max_string == null) max_string = v; else if (max_string.compareTo(v) < 0) max_string = v;
            }
        }
        Vector r = new Vector(1);
        if ((max_string != null) && (max_double != null)) ; else if (max_string != null) r.addElement(max_string); else r.addElement(max_double);
        return r.elements();
    }

    public String toString() {
        return "max(" + getPrevious() + ")";
    }
}
