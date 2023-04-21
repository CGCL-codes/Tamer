package org.ethontos.owlwatcher.exceptions;

public class OntologyNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4257330915121969953L;

    public OntologyNotFoundException() {
    }

    public OntologyNotFoundException(String message) {
        super(message);
    }

    public OntologyNotFoundException(Throwable cause) {
        super(cause);
    }

    public OntologyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
