package com.poltman.dspace.db.exceptions;

/**
 * 
 * @author z.ciok@poltman.com
 * 
 */
public class RepositoryException extends Exception {

    private static final long serialVersionUID = -7151101975433407208L;

    String exception;

    public RepositoryException() {
        super();
        this.exception = "unknown";
    }

    public RepositoryException(String exception) {
        super(exception);
        this.exception = exception;
    }

    public RepositoryException(String exception, RepositoryException dae) {
        super(exception, dae);
        this.exception = exception;
    }

    public String getError() {
        return this.exception;
    }
}
