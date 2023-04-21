package org.fcrepo.server.errors;

/**
 * Superclass for initialization-related exceptions.
 * 
 * @author Chris Wilper
 */
public class InitializationException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an InitializationException.
     * 
     * @param message
     *        An informative message explaining what happened and (possibly) how
     *        to fix it.
     */
    public InitializationException(String message) {
        super(null, message, null, null, null);
    }

    public InitializationException(String message, Throwable cause) {
        super(null, message, null, null, cause);
    }

    public InitializationException(String bundleName, String code, String[] replacements, String[] details, Throwable cause) {
        super(bundleName, code, replacements, details, cause);
    }
}
