package se.inera.ifv.medcert.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import se.inera.ifv.medcert.core.spi.authentication.MedicalPersonalUser;

public class AuthenticatedUserDetails {

    private static final Logger log = LoggerFactory.getLogger(AuthenticatedUserDetails.class);

    /**
     * Returns the username of an authenticated user.
     * 
     * @return the username if the user is logged in otherwise null
     */
    public static String getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Getting authorized user: {}.", new Object[] { principal });
        if (principal instanceof MedicalPersonalUser) {
            log.debug("Principal is MedicalPersonalUser");
            return ((MedicalPersonalUser) principal).getHsaId();
        } else {
            return null;
        }
    }

    public static String getPrincipalName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Getting authorized user: {}.", new Object[] { principal });
        if (principal instanceof MedicalPersonalUser) {
            log.debug("Principal is MedicalPersonalUser");
            return ((MedicalPersonalUser) principal).getName();
        } else {
            return null;
        }
    }

    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    /**
     * Returns true if the authenticated user has a given role
     * 
     * @param role
     *            the role e.g. "ROLE_USER"
     * @return true if the authenticated user has the given role. Also returns
     *         true if the given role was null or an empty string
     */
    public static boolean hasRole(String role) {
        log.debug("Checking if user has role {}", role);
        if (role == null || role.length() == 0) {
            return true;
        }
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof UserDetails) {
            UserDetails ud = (UserDetails) obj;
            for (GrantedAuthority auth : ud.getAuthorities()) {
                if (role.equals(auth.getAuthority())) {
                    log.debug("User {} has role {}", ud.getUsername(), role);
                    return true;
                }
            }
            log.debug("User {} don't have role {}", ud.getUsername(), role);
            return false;
        } else {
            log.debug("User is not a instance of UserDetails {}", obj.getClass());
            return false;
        }
    }
}
