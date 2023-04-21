package com.bradrydzewski.tinyreport.jdbc;

/**
 * Represents a database access exception.
 *
 * @author Brad Rydzewski
 */
public class DataAccessException extends RuntimeException {

    /**
     * Eclipse-generated Serial Version UID.
     */
    private static final long serialVersionUID = -280556433786906915L;

    /**
     * Creates a <code>DataAccessException</code> with the specified
     * <code>msg</code> message and the root <code>cause</code>.
     *
     * @param msg A textual description of the exception
     * @param cause The root cause of the exception
     */
    public DataAccessException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
