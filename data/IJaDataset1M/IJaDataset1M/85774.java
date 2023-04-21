package com.f2ms.exception;

public class DAOException extends Exception {

    private static final long serialVersionUID = 4339815980212282996L;

    public DAOException() {
    }

    public DAOException(String s) {
        super(s);
    }

    public DAOException(Exception e) {
        super(e);
    }
}
