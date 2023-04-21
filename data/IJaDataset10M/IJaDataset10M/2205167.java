package org.personalsmartspace.pss_sm_dbc.impl;

public final class AssertionException extends DbcException {

    private static final long serialVersionUID = 1L;

    /**
     * construct a new DbcException
     */
    public AssertionException() {
        super();
    }

    /**
     * construct a new DBCException
     * 
     * @param message
     *            to be associated with exception
     */
    public AssertionException(String message) {
        super(message);
    }
}
