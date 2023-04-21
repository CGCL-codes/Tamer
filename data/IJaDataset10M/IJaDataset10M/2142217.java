package edu.vt.middleware.ldap.jaas;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import edu.vt.middleware.ldap.SearchRequest;
import edu.vt.middleware.ldap.auth.AuthenticationRequest;
import edu.vt.middleware.ldap.auth.Authenticator;

/**
 * Login module for testing configuration properties.
 *
 * @author  Middleware Services
 * @version  $Revision: 2079 $ $Date: 2011-08-23 09:44:41 -0400 (Tue, 23 Aug 2011) $
 */
public class PropsLoginModule extends AbstractLoginModule {

    /** Factory for creating authenticators with JAAS options. */
    private AuthenticatorFactory authenticatorFactory;

    /** Factory for creating role resolvers with JAAS options. */
    private RoleResolverFactory roleResolverFactory;

    /** Authenticator to load properties for. */
    private Authenticator auth;

    /** Authentication request to load properties for. */
    private AuthenticationRequest authRequest;

    /** Role resolver to load properties for. */
    private RoleResolver roleResolver;

    /** Search request to load properties for. */
    private SearchRequest searchRequest;

    /** {@inheritDoc} */
    @Override
    public void initialize(final Subject subject, final CallbackHandler callbackHandler, final Map<String, ?> sharedState, final Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);
        authenticatorFactory = new PropertiesAuthenticatorFactory();
        auth = authenticatorFactory.createAuthenticator(options);
        authRequest = authenticatorFactory.createAuthenticationRequest(options);
        roleResolverFactory = new PropertiesRoleResolverFactory();
        roleResolver = roleResolverFactory.createRoleResolver(options);
        searchRequest = roleResolverFactory.createSearchRequest(options);
    }

    /** {@inheritDoc} */
    @Override
    public boolean login() throws LoginException {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean commit() throws LoginException {
        subject.getPublicCredentials().add(auth);
        subject.getPublicCredentials().add(authRequest);
        subject.getPublicCredentials().add(roleResolver);
        subject.getPublicCredentials().add(searchRequest);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean abort() {
        loginSuccess = false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean logout() {
        return true;
    }
}
