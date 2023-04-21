package org.josso;

import org.josso.auth.Authenticator;
import org.josso.gateway.SSOWebConfiguration;
import org.josso.gateway.SecurityDomainMatcher;
import org.josso.gateway.assertion.AssertionManager;
import org.josso.gateway.audit.SSOAuditManager;
import org.josso.gateway.event.SSOEventManager;
import org.josso.gateway.identity.service.SSOIdentityManager;
import org.josso.gateway.identity.service.SSOIdentityProvider;
import org.josso.gateway.protocol.SSOProtocolManager;
import org.josso.gateway.session.service.SSOSessionManager;
import org.josso.selfservices.password.PasswordManagementService;
import java.util.List;

/**
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version CVS $Id: SecurityDomain.java 574 2008-08-01 21:14:27Z sgonzalez $
 */
public interface SecurityDomain {

    /**
     * Getter for this domain's Identity Manager instance.
     */
    SSOIdentityManager getIdentityManager();

    /**
     * Setter for this domain's Identity Manager instance.
     */
    void setIdentityManager(SSOIdentityManager im);

    /**
     * Getter for this domain's Session Manager instance.
     */
    SSOSessionManager getSessionManager();

    /**
     * Setter for this domain's Session Manager instance.
     */
    void setSessionManager(SSOSessionManager sm);

    /**
     * Getter for this domain's Authenticator instance.
     */
    Authenticator getAuthenticator();

    /**
     * Setter for this domain's Authenticator instance.
     */
    void setAuthenticator(Authenticator a);

    /**
     * Getter for this domain's Audit Manager Instance
     */
    SSOAuditManager getAuditManager();

    /**
     * Setter for this domain's Audit Manager Instance
     */
    void setAuditManager(SSOAuditManager am);

    /**
     * Getter for this domain's Event Manager instance.
     */
    SSOEventManager getEventManager();

    /**
     * Setter for this domain's Event Manager instance.
     */
    void setEventManager(SSOEventManager em);

    /**
     * Getter for this domain's name
     */
    String getName();

    /**
     * Getter for this domain's type
     */
    String getType();

    /**
     * Setter for this domain's name
     */
    void setName(String name);

    /**
     * Getter for this domain's Protocol Manager Instance
     */
    SSOProtocolManager getProtocolManager();

    /**
     * Setter for this domain's Protocol Manager Instance
     */
    void setProtocolManager(SSOProtocolManager pm);

    SSOWebConfiguration getSSOWebConfiguration();

    void setSSOWebConfiguration(SSOWebConfiguration ssoWebConfiguration);

    AssertionManager getAssertionManager();

    void setAssertionManager(AssertionManager assertionManager);

    SSOIdentityProvider getIdentityProvider();

    void setIdentityProvider(SSOIdentityProvider ssoIdentityProvider);

    List<SecurityDomainMatcher> getMatchers();

    void setMatchers(List<SecurityDomainMatcher> matcher);

    PasswordManagementService getPasswordManager();

    void setPasswordManager(PasswordManagementService pwdSvc);
}
