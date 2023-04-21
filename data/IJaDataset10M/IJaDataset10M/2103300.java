package com.naildrivin5.inlaw;

public class DatabaseException extends RuntimeException {

    public DatabaseException() {
        super();
    }

    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(String s, Throwable t) {
        super(s, t);
    }

    public DatabaseException(Throwable t) {
        super(t);
    }
}
