package org.openuss.security.acegi.ldap;

import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.dao.UserCache;
import org.openuss.security.ldap.LdapConfigurationService;

/**
 * @author Peter Schuh
 * @author Juergen de Braaf
 *
 */
public interface ConfigurableLdapAuthenticationProvider extends AuthenticationProvider {

    /**
	 * Retrieves <LdapServerConfiguration>s from an <code>LdapConfigurationService</code> and instantiates necessary objects 
	 * for a LDAP-based authentication. 
	 */
    public void reconfigure();

    public LdapConfigurationService getLdapConfigurationService();

    public void setLdapConfigurationService(LdapConfigurationService ldapConfigurationService);

    public UserCache getUserCache();

    public void setUserCache(UserCache userCache);

    /**
	 * Optional default role to be assigned to an LDAP user.
	 */
    public String getDefaultRole();

    public void setDefaultRole(String defaultRole);

    /**
	 * Optional default role prefix to be assigned to an LDAP user.
	 */
    public String getDefaultRolePrefix();

    public void setDefaultRolePrefix(String defaultRole);
}
