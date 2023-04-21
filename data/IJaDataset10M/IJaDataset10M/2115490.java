package org.scohen.juploadr.util;

import java.util.Iterator;

public abstract class UnmodifiableIterator implements Iterator {

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove from this iterator");
    }
}
