package com.hongbo.cobweb.nmr.api.security;

import java.security.GeneralSecurityException;
import javax.security.auth.Subject;

/**
 * Interface for the authentication service.
 *
 */
public interface AuthenticationService {

    /**
     * Authenticate a user given its name and credentials.
     * Upon sucessfull completion, the subject should be populated
     * with the user known principals, including, but not limited to
     * a UserPrincipal and the GroupPrincipal that this user belongs
     * to.
     *
     * @param subject the subject to populate
     * @param domain the security domain to use
     * @param user the user name
     * @param credentials the user credntials
     * @throws GeneralSecurityException if the user can not be authenticated
     */
    void authenticate(Subject subject, String domain, String user, Object credentials) throws GeneralSecurityException;
}
