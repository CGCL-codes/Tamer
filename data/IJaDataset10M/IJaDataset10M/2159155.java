package org.acegisecurity;

/**
 * Abstract superclass for all exceptions related an {@link Authentication}
 * object being invalid for whatever reason.
 *
 * @author Ben Alex
 * @version $Id: AuthenticationException.java,v 1.6 2005/11/17 00:55:49 benalex Exp $
 */
public abstract class AuthenticationException extends AcegiSecurityException {

    /**
     * The authentication that related to this exception (may be
     * <code>null</code>)
     */
    private Authentication authentication;

    /**
     * Constructs an <code>AuthenticationException</code> with the specified
     * message and root cause.
     *
     * @param msg the detail message
     * @param t the root cause
     */
    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs an <code>AuthenticationException</code> with the specified
     * message and no root cause.
     *
     * @param msg the detail message
     */
    public AuthenticationException(String msg) {
        super(msg);
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
