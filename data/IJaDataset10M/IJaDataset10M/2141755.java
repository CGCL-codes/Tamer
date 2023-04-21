package org.wicketopia.spring.security.predicate;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketopia.context.Context;
import org.wicketopia.context.ContextPredicate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 1.0
 */
public class RequiredRolesPredicate implements ContextPredicate {

    private Set<String> roles;

    public RequiredRolesPredicate(String... roles) {
        this.roles = new HashSet<String>(Arrays.asList(roles));
    }

    @Override
    public boolean evaluate(Context context) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null || securityContext.getAuthentication().getAuthorities() == null) {
            return false;
        }
        for (GrantedAuthority authority : securityContext.getAuthentication().getAuthorities()) {
            if (roles.contains(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
