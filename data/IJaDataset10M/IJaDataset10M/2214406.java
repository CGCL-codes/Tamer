package ipmss.services.common;

import javax.inject.Named;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The Class ContextManager.
 */
@Named
public class ContextManager {

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public static String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().toString();
    }

    /**
     * Gets the user authentication.
     *
     * @return the user authentication
     */
    public static Authentication getUserAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
