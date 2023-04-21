package com.c2b2.ipoint.model;

public class PersistentModelException extends Exception {

    public PersistentModelException(String msg, Throwable t) {
        super(msg, t);
    }

    public PersistentModelException(String msg) {
        super(msg);
    }

    public PersistentModelException(Throwable t) {
        super(t);
    }
}
