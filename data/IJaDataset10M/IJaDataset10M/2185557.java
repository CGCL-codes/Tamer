package org.ibex.util;

public class IntHash extends Hash {

    public IntHash() {
        super();
    }

    public int intGet(Object k1) {
        Integer key = (Integer) get(k1, null);
        return key != null ? key.intValue() : -1;
    }

    public void put(Object k1, int v) {
        put(k1, new Integer(v));
    }

    public IntHash(int initialcapacity, int loadFactor) {
        super(initialcapacity, loadFactor);
    }
}
