package com.nexirius.framework.trend;

import java.util.Enumeration;
import java.util.Vector;

public class LabelPositionVector extends Vector {

    Enumeration e = elements();

    public LabelPosition first() {
        e = elements();
        return next();
    }

    public LabelPosition next() {
        if (e.hasMoreElements()) {
            return (LabelPosition) e.nextElement();
        }
        return null;
    }

    public void append(LabelPosition p) {
        addElement(p);
    }
}
