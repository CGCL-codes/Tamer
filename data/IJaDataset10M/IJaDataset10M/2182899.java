package org.vrspace.neurogrid;

import org.vrspace.util.*;

/**
A Predicate.
*/
public class Predicate extends NGObject {

    public static final String[] _indexUnique = new String[] { "predicate" };

    public String predicate;

    public Predicate() {
    }

    public Predicate(String predicate) {
        this.predicate = predicate;
    }

    public boolean equals(Predicate p) {
        boolean ret = (p != null && p.predicate != null && p.predicate.equals(this.predicate));
        return ret;
    }

    public NGObject next() {
        Predicate ret = (Predicate) this.clone();
        ret.predicate = this.predicate + "\0";
        return ret;
    }
}
