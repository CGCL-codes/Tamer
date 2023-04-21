package org.knopflerfish.service.um.useradmin;

import org.osgi.service.useradmin.User;

/**
 * Interface for for a user name/password authentication session. First, a user
 * name and a password should be supplied, then it is possible to authenticate
 * the user or to get an Authorization object.
 * 
 * @author Gatespace AB
 * @version $Revision: 1.1.1.1 $
 */
public interface PasswdSession {

    /**
     * Set the user's user name.
     * 
     * @param username
     *            the username
     */
    void setUsername(String username);

    /**
     * Set the user's password.
     * 
     * @param password
     *            the password
     */
    void setPassword(String password);

    /**
     * Attempts to authenticate. Useful if authorization information is not
     * required, see {@link #getAuthorization}.
     * 
     * @return the user, if a user admin service is available and there is a
     *         user with the supplied user name and the password matched.
     *         Otherwise null.
     * @exception IllegalStateException
     *                if called before a user name and a password have been
     *                supplied or if the user admin service is no longer
     *                available.
     */
    User authenticate() throws IllegalStateException;

    /**
     * Attempts to authenticate and authorize the user.
     * 
     * @return authorization information, or null if authentication failed.
     * @exception IllegalStateException
     *                if called before a user name and a password have been
     *                supplied or if the user admin service is no longer
     *                available.
     */
    ContextualAuthorization getAuthorization() throws IllegalStateException;
}
