package com.hardcode.gdbms.engine.customQuery;

/**
 * If there is an error in the Custom Query execution
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class QueryException extends Exception {

    /**
     *
     */
    public QueryException() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param message
     */
    public QueryException(String message) {
        super(message);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message
     * @param cause
     */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cause
     */
    public QueryException(Throwable cause) {
        super(cause);
    }
}
