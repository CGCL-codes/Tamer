package org.fb4j.impl;

import org.fb4j.client.ClientException;

/**
 * 
 * @author Gino Miceli
 */
public class FqlQueryParseException extends ClientException {

    private static final long serialVersionUID = 1L;

    private String fql;

    FqlQueryParseException(String message, String fql) {
        super(message + ": " + fql);
        this.fql = fql;
    }

    public String getFql() {
        return fql;
    }
}
