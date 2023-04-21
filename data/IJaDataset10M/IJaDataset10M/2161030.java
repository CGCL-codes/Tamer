package com.sourceforge.oraclewicket.app.exception;

/**
 * This is thrown when operations have no work to do.
 * For example, when a form is submitted but no changes have been specified.
 *
 * @author Andrew Hall
 *
 */
public class NothingToDoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public NothingToDoException() {
        super();
    }
}
