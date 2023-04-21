package org.acegisecurity.providers.ldap;

import org.acegisecurity.ldap.LdapUserInfo;

/**
 * The strategy interface for locating and authenticating an Ldap user.
 * <p>
 * The LdapAuthenticationProvider calls this interface to authenticate a user
 * and obtain the information for that user from the directory.
 * </p>
 *
 *
 * @author Luke Taylor
 * @version $Id: LdapAuthenticator.java,v 1.3 2006/04/16 14:05:28 luke_t Exp $
 */
public interface LdapAuthenticator {

    /**
     * Authenticates as a user and obtains additional user information
     * from the directory.
     *
     * @param username the user's login name (<em>not</em> their DN).
     * @param password the user's password supplied at login.
     * @return the details of the successfully authenticated user.
     */
    LdapUserInfo authenticate(String username, String password);
}
