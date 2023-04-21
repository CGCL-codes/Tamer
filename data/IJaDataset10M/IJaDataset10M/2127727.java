package edu.yale.its.tp.cas.client;

/**
 * Exception representing some failure of the CAS authentication process.
 * Might be a bad CAS ticket, a bad connection to the CAS server, or
 * really any other problem - so long as CAS authentication failed, this is what you should get.
 * @author andrew.petro@yale.edu
 */
public class CASAuthenticationException extends Exception {

    /**
     * @param string
     */
    public CASAuthenticationException(String string) {
        super(string);
    }

    /**
     * @param message
     * @param e
     */
    public CASAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
