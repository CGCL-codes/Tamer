package com.sitescape.team.module.ldap;

import javax.naming.NamingException;
import com.sitescape.team.domain.NoUserByTheNameException;

/**
 * @author Janet McCann
 *
 */
public interface LdapModule {

    public boolean testAccess(String operation);

    public LdapConfig getLdapConfig();

    public void setLdapConfig(LdapConfig config);

    public void syncAll() throws NamingException;

    public void syncUser(Long userId) throws NoUserByTheNameException, NamingException;
}
