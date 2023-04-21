package org.libreplan.web.security;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.libreplan.business.users.entities.UserRole;
import org.libreplan.web.users.bootstrap.MandatoryUser;
import org.libreplan.web.users.services.CustomUser;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;

/**
 * Utility methods for security tasks.
 *
 * @author Fernando Bellas Permuy <fbellas@udc.es>
 * @author Jacobo Aragunde Perez <jaragunde@igalia.com>
 * @author Cristina Alvarino Perez <cristina.alvarino@comtecsf.es>
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static final boolean isUserInRole(UserRole role) {
        return Executions.getCurrent().isUserInRole(role.name());
    }

    public static final String getSessionUserLoginName() {
        HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return MandatoryUser.USER.getLoginName();
        }
        return principal.getName();
    }

    /**
     * @return <code>null</code> if not user is logged
     */
    public static final CustomUser getLoggedUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof CustomUser) {
            return (CustomUser) authentication.getPrincipal();
        }
        return null;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
