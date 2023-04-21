package org.jsecurity.ri.authz.support;

import org.jsecurity.authc.module.AuthenticationInfo;
import org.jsecurity.authz.AuthorizationContext;
import org.jsecurity.ri.authz.AuthorizationContextFactory;
import java.security.Principal;
import java.util.Collection;

/**
 * Abstract implementation of the <tt>AuthorizationContextFactory</tt> interface that
 * ensures the given <tt>AuthenticationInfo</tt> is valid.
 *
 * @since 0.1
 * @author Les Hazlewood
 * @author Jeremy Haile
 */
public abstract class AbstractAuthorizationContextFactory implements AuthorizationContextFactory {

    public AbstractAuthorizationContextFactory() {
    }

    public AuthorizationContext createAuthorizationContext(AuthenticationInfo info) {
        Collection<Principal> principals = info.getPrincipals();
        if (principals == null || principals.size() < 1) {
            String msg = "AuthenticationInfo parameter must return at least one, non-null principal.";
            throw new IllegalArgumentException(msg);
        }
        return onCreateAuthorizationContext(info);
    }

    protected abstract AuthorizationContext onCreateAuthorizationContext(AuthenticationInfo info);
}
