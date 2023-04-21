package com.vircon.util;

public class Utility<T> {

    public T nullIf(T... values) {
        T rc = null;
        for (T T : values) {
            if (T != null) {
                rc = T;
                break;
            }
        }
        return rc;
    }
}
