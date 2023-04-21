package net.sf.wgfa.exceptions;

/**
 * @author blair
 *
 */
public abstract class WgfaOntologyException extends WgfaException {

    public WgfaOntologyException(String message) {
        super(message);
    }

    public WgfaOntologyException(String message, Throwable cause) {
        super(message, cause);
    }
}
