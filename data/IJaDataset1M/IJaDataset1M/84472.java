package com.sun.j2ee.blueprints.catalog.dao;

/**
 * An application exception indicating something has gone wrong
 * 
 */
public class CatalogDAOException extends RuntimeException {

    public CatalogDAOException() {
    }

    public CatalogDAOException(String s) {
        super(s);
    }

    public CatalogDAOException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CatalogDAOException(Throwable cause) {
        super(cause);
    }
}
