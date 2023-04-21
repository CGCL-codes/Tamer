package org.knopflerfish.util.metatype;

import java.util.*;

public class AE {

    String adref;

    Vector values;

    public AE(String adref) {
        this.adref = adref;
        values = new Vector();
    }

    public void addValue(String value) {
        values.add(value);
    }
}
