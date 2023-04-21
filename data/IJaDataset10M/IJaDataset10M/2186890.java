package org.sac.browse.auth;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GAEAuthenticator implements AppAuthenticator {

    private UserService userService = null;

    private Principal userPrincipal = null;

    private String loginURL = null;

    /**
	 * Tries to access GAE's UserService and throws exception if this is not found.
	 * @throws NoClassDefFoundError
	 */
    public GAEAuthenticator() throws NoClassDefFoundError {
        userService = UserServiceFactory.getUserService();
        if (userService != null) {
            System.out.println("userService:" + userService);
            userService.getCurrentUser();
        } else {
            throw new NoClassDefFoundError();
        }
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        String thisURL = request.getRequestURI();
        userPrincipal = request.getUserPrincipal();
        loginURL = userService.createLoginURL(thisURL);
        if (userPrincipal != null) {
            return true;
        }
        return false;
    }

    public String getLoginURL() {
        return loginURL;
    }

    public String getUserName() {
        return userPrincipal.getName();
    }
}
