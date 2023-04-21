package org.jsecurity.ri.authc;

import org.jsecurity.authz.AuthorizationContext;

/**
 * <p>The authorization context binder is responsible for binding an
 * {@link org.jsecurity.authz.AuthorizationContext} object after authentication takes place
 * to the application so that it can be retrieved on later requests.  For example, the
 * binder could bind the context to a thread local, HTTP session, static variable, etc.</p>
 *
 * @since 0.1
 * @author Jeremy Haile
 */
public interface AuthorizationContextBinder {

    /**
     * Binds the authorization context to the application so that it is accessible to future access.
     * @param context the authorization context to bind.
     */
    void bindAuthorizationContext(AuthorizationContext context);
}
