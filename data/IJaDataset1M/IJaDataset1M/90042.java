package com.adhocit.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.adhocit.Constants;
import com.adhocit.model.Role;
import com.adhocit.model.User;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This advice is responsible for enforcing security and only allowing administrators
 * to modify users. Users are allowed to modify themselves.
 *
 * @author mraible
 */
public class UserSecurityAdvice implements MethodBeforeAdvice, AfterReturningAdvice {

    /**
     * Default "Access Denied" error message (not i18n-ized).
     */
    public static final String ACCESS_DENIED = "Access Denied: Only administrators are allowed to modify other users.";

    private final Log log = LogFactory.getLog(UserSecurityAdvice.class);

    /**
     * Method to enforce security and only allow administrators to modify users. Regular
     * users are allowed to modify themselves.
     *
     * @param method the name of the method executed
     * @param args the arguments to the method
     * @param target the target class
     * @throws Throwable thrown when args[0] is null or not a User object
     */
    public void before(Method method, Object[] args, Object target) throws Throwable {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx.getAuthentication() != null) {
            Authentication auth = ctx.getAuthentication();
            boolean administrator = false;
            Collection<GrantedAuthority> roles = auth.getAuthorities();
            for (GrantedAuthority role1 : roles) {
                if (role1.getAuthority().equals(Constants.ADMIN_ROLE)) {
                    administrator = true;
                    break;
                }
            }
            User user = (User) args[0];
            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            boolean signupUser = resolver.isAnonymous(auth);
            if (!signupUser) {
                User currentUser = getCurrentUser(auth);
                if (user.getId() != null && !user.getId().equals(currentUser.getId()) && !administrator) {
                    log.warn("Access Denied: '" + currentUser.getUsername() + "' tried to modify '" + user.getUsername() + "'!");
                    throw new AccessDeniedException(ACCESS_DENIED);
                } else if (user.getId() != null && user.getId().equals(currentUser.getId()) && !administrator) {
                    Set<String> userRoles = new HashSet<String>();
                    if (user.getRoles() != null) {
                        for (Object o : user.getRoles()) {
                            Role role = (Role) o;
                            userRoles.add(role.getName());
                        }
                    }
                    Set<String> authorizedRoles = new HashSet<String>();
                    for (GrantedAuthority role : roles) {
                        authorizedRoles.add(role.getAuthority());
                    }
                    if (!CollectionUtils.isEqualCollection(userRoles, authorizedRoles)) {
                        log.warn("Access Denied: '" + currentUser.getUsername() + "' tried to change their role(s)!");
                        throw new AccessDeniedException(ACCESS_DENIED);
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Registering new user '" + user.getUsername() + "'");
                }
            }
        }
    }

    /**
     * After returning, grab the user, check if they've been modified and reset the SecurityContext if they have.
     * @param returnValue the user object
     * @param method the name of the method executed
     * @param args the arguments to the method
     * @param target the target class
     * @throws Throwable thrown when args[0] is null or not a User object
     */
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        User user = (User) args[0];
        if (user.getVersion() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            boolean signupUser = resolver.isAnonymous(auth);
            if (auth != null && !signupUser) {
                User currentUser = getCurrentUser(auth);
                if (currentUser.getId().equals(user.getId())) {
                    auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
    }

    private User getCurrentUser(Authentication auth) {
        User currentUser;
        if (auth.getPrincipal() instanceof UserDetails) {
            currentUser = (User) auth.getPrincipal();
        } else if (auth.getDetails() instanceof UserDetails) {
            currentUser = (User) auth.getDetails();
        } else {
            throw new AccessDeniedException("User not properly authenticated.");
        }
        return currentUser;
    }
}
