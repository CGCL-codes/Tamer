package net.solarnetwork.central.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implementation of {@link UserDetailsService} for X.509 authenticated nodes.
 * 
 * @author matt
 * @version $Revision: 1270 $
 */
public class NodeUserDetailsService implements UserDetailsService {

    private static final Collection<GrantedAuthority> AUTHORITIES = getAuthorities();

    private static Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(1);
        authorities.add(new GrantedAuthorityImpl("ROLE_NODE"));
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        Long id = Long.valueOf(username);
        return new AuthenticatedNode(id, AUTHORITIES);
    }
}
