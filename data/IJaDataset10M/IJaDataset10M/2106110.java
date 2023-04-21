package com.jguild.devportal.util;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;

public class SecurityHelper {

    public static boolean hasRole(final String role) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (final GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            if (grantedAuthority.getAuthority().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}
