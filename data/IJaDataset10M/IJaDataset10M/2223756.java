package org.wsml.reasoner.ext.sql;

/**
 * A generic exception which is thrown if a syntactic problem with the
 * WSML-Flight-A query is found.
 * 
 * @author Florian Fischer, florian.fischer@deri.at
 */
public class QueryFormatException extends Exception {

    private static final long serialVersionUID = -236976076905856132L;

    public QueryFormatException(String message) {
        super(message);
    }
}
