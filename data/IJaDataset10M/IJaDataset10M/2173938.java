package org.tripcom.query.client.sir.sparql.core;

import org.tripcom.query.client.sir.sparql.SPARQLException;

/**
 * <code>QueryException</code> represents an InferenceException that can be
 * encountered when querying errors occurr during an SPARQL querying process.
 * 
 * @author timowest
 */
public class QueryException extends SPARQLException {

    /**
     * 
     */
    private static final long serialVersionUID = -6285019351093283817L;

    /**
     * Create a new QueryException instance
     */
    public QueryException() {
        super();
    }

    /**
     * Create a new QueryException instance
     * 
     * @param message
     */
    public QueryException(String message) {
        super(message);
    }

    /**
     * Create a new QueryException instance
     * 
     * @param cause
     */
    public QueryException(Throwable cause) {
        super(cause);
    }

    /**
     * Create a new QueryException instance
     * 
     * @param message
     * @param cause
     */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
