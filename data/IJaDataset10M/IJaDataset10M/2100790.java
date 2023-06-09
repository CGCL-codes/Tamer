package net.sf.jpasecurity.security.authentication;

import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import net.sf.jpasecurity.configuration.AuthenticationProvider;

/**
 * This is an implementation of the {@link AuthenticationProvider} interface
 * that uses a thread-local variable to store the authentication information.
 * @author Arne Limburg
 */
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private static ThreadLocal<Object> principal = new ThreadLocal<Object>();

    private static ThreadLocal<Collection<?>> roles = new ThreadLocal<Collection<?>>();

    /**
     * Sets the current authenticated principal to the specified principal, assigning the specified roles.
     * @param principal the principal
     * @param roles the roles
     */
    public void authenticate(Object principal, Object... roles) {
        authenticate(principal, Arrays.asList(roles));
    }

    /**
     * Sets the current authenticated principal to the specified principal, assigning the specified roles.
     * @param principal the principal
     * @param roles the roles
     */
    public void authenticate(Object principal, Collection<?> roles) {
        DefaultAuthenticationProvider.principal.set(principal);
        DefaultAuthenticationProvider.roles.set(roles);
    }

    /**
     * Clears the current authenticated principal and its roles.
     */
    public void unauthenticate() {
        DefaultAuthenticationProvider.principal.remove();
        DefaultAuthenticationProvider.roles.remove();
    }

    public Object getPrincipal() {
        return principal.get();
    }

    public Collection<?> getRoles() {
        Collection<?> result = roles.get();
        return result != null ? result : Collections.emptySet();
    }

    public static <R> R runAs(Object principal, Collection<?> roles, PrivilegedExceptionAction<R> action) throws Exception {
        DefaultAuthenticationProvider authenticationProvider = new DefaultAuthenticationProvider();
        Object currentPrincipal = authenticationProvider.getPrincipal();
        Collection<?> currentRoles = authenticationProvider.getRoles();
        try {
            authenticationProvider.authenticate(principal, roles);
            return action.run();
        } finally {
            authenticationProvider.authenticate(currentPrincipal, currentRoles);
        }
    }

    public static <R> R runAs(Object principal, Collection<?> roles, PrivilegedAction<R> action) {
        DefaultAuthenticationProvider authenticationProvider = new DefaultAuthenticationProvider();
        Object currentPrincipal = authenticationProvider.getPrincipal();
        Collection<?> currentRoles = authenticationProvider.getRoles();
        try {
            authenticationProvider.authenticate(principal, roles);
            return action.run();
        } finally {
            authenticationProvider.authenticate(currentPrincipal, currentRoles);
        }
    }
}
