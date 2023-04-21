package org.dspace.uri;

/**
 * Exception class to indicate that there was a problem with some general
 * part of the Identifier mechanism.  This may further wrap deeper
 * exceptions from this module.
 *
 * @author Richard Jones
 */
public class IdentifierException extends Exception {

    public IdentifierException() {
        super();
    }

    public IdentifierException(String s) {
        super(s);
    }

    public IdentifierException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IdentifierException(Throwable throwable) {
        super(throwable);
    }
}
