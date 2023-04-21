package de.grogra.persistence;

public class PersistenceException extends RuntimeException {

    public PersistenceException(Throwable t) {
        super(t);
    }

    public PersistenceException(String msg) {
        super(msg);
    }
}
