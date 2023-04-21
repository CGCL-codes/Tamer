package com.enerjy.analyzer.java.rules.testfiles.T0277;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("all")
public class PTest01 implements Iterator {

    public boolean hasNext() {
        return false;
    }

    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return null;
    }

    public void remove() {
    }
}
