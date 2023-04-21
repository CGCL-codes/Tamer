package com.jalarbee.core.generic.dao;

public class GenericDaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public GenericDaoException() {
    }

    public GenericDaoException(String arg0) {
        super(arg0);
    }

    public GenericDaoException(Throwable arg0) {
        super(arg0);
    }

    public GenericDaoException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
