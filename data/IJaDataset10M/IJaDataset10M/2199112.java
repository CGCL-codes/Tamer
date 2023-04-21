package org.mulgara.mrg;

/**
 * Indicates an internal error in the RDF library.
 */
public class RdfException extends Exception {

    private static final long serialVersionUID = 423957789781892991L;

    public RdfException() {
    }

    public RdfException(String msg) {
        super(msg);
    }

    public RdfException(Throwable cause) {
        super(cause);
    }

    public RdfException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
