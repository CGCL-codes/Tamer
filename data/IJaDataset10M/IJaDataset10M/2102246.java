package com.sitescape.team.module.authentication;

import java.util.List;
import javax.naming.NamingException;
import com.sitescape.team.domain.AuthenticationConfig;
import com.sitescape.team.domain.LdapConnectionConfig;
import com.sitescape.team.domain.NoUserByTheNameException;
import com.sitescape.team.domain.ZoneInfo;

/**
 * @author Janet McCann
 *
 */
public interface AuthenticationModule {

    public enum AuthenticationOperation {

        manageAuthentication
    }

    public boolean testAccess(AuthenticationOperation operation);

    public AuthenticationConfig getAuthenticationConfig();

    public List<LdapConnectionConfig> getLdapConnectionConfigs(Long zoneId);

    public List<LdapConnectionConfig> getLdapConnectionConfigs();

    public void setLdapConnectionConfigs(List<LdapConnectionConfig> configs);
}
