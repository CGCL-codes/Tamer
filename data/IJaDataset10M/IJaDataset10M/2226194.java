package com.enerjy.analyzer.java.rules.testfiles.T0277;

import java.util.NoSuchElementException;

public class PTest11 {

    public boolean hasNext() {
        return false;
    }

    public Object next() {
        Inner.checkThrow();
        return null;
    }

    public void remove() {
    }

    public static class Inner {

        static void checkThrow() {
            throw new NoSuchElementException();
        }
    }
}
