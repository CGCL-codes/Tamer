package com.evasion.ejb;

import com.evasion.exception.PersistenceViolationException;
import org.springframework.security.userdetails.UserDetails;

/**
 *
 * @author sebastien.glon
 */
public interface UserDetailsServiceRemote {

    UserDetails loadUserByUsername(String login) throws PersistenceViolationException;
}
