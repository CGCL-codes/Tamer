package com.c2b2.ipoint.business.security;

import com.c2b2.ipoint.model.User;

/**
 * This is the interface required for a Portal authenticator.<br>
 * The authenticator must return a valid User as defined in the Portal database
 * as this is associated with many actions on the Portal. If Users are authenticated
 * with an external authentication provider then the Portal User database should be synchronised 
 * with this repository either on demand or periodically.
 */
public interface Authenticator {

    /**
   * Authenticates a user 
   * @param username
   * @param password
   * @return The authenticated user in the iPoint Portal database or null if authentication failed
   * @thorws SecurityException authenticators should throw security exceptions
   */
    public User authenticate(String username, String password) throws SecurityException;
}
