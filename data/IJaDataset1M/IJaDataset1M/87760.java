package org.kablink.teaming.module.authentication;

import java.util.List;
import java.util.Set;
import org.kablink.teaming.domain.AuthenticationConfig;
import org.kablink.teaming.domain.LdapConnectionConfig;
import org.kablink.teaming.domain.Principal;

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

    public AuthenticationConfig getAuthenticationConfigForZone(Long zoneId);

    public void setAuthenticationConfig(AuthenticationConfig authConfig);

    public void setAuthenticationConfigForZone(Long zoneId, AuthenticationConfig authConfig);

    public List<LdapConnectionConfig> getLdapConnectionConfigs(Long zoneId);

    public List<LdapConnectionConfig> getLdapConnectionConfigs();

    public void setLdapConnectionConfigs(List<LdapConnectionConfig> configs);

    public Set<String> getMappedAttributes(Principal principal);
}
