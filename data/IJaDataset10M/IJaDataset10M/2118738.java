package prest.web.access.auth.form;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prest.core.RedirectException;
import prest.core.SendErrorException;
import prest.core.io.Environment;
import prest.core.io.Request;
import prest.web.access.auth.User;

/**
 * Example of an access manager using sesson to store the {@link User}
 * instances. Extends this class to provide some aditional functionality or to
 * change default values of URLs used to redirect the user in the case of denied
 * access or unlogged user.
 *
 * @author Daniel Buchta
 * @author Peter Rybar
 *
 */
public class AccessManager {

    /** name, to which the URL of the action with denied access is bound */
    private static final String SESSION_DENIED_ACCESS_URL_KEY = "PREST_DENIED_ACCESS_URL";

    /** name, to which the login referer is bound */
    private static final String SESSION_LOGIN_REFERER_KEY = "PREST_LOGIN_REFERER";

    /** name, to which the logged user is bound */
    private static final String SESSION_USER_KEY = "PREST_USER";

    /**
	 * The instance getter
	 *
	 * @return the instance
	 */
    public static AccessManager getInstance() {
        return instance;
    }

    /** url to redirect to when user is not logged in */
    private String noLoginRedirect;

    /** url to redirect to when user can't access required service */
    private String accessDeniedRedirect;

    private static final AccessManager instance;

    static {
        String noLoginRedirect = "login";
        String accessDeniedRedirect = "denied";
        instance = new AccessManager(accessDeniedRedirect, noLoginRedirect);
    }

    /**
	 * Constructor
	 *
	 * @param accessDeniedRedirect
	 *            URL to redirect when user hasn't access to some action
	 * @param noLoginRedirect
	 *            URL to redirect when no user is logged in
	 */
    protected AccessManager(String accessDeniedRedirect, String noLoginRedirect) {
        this.accessDeniedRedirect = accessDeniedRedirect;
        this.noLoginRedirect = noLoginRedirect;
    }

    /**
	 * Checks, if user stored in sessin has some of the requited roles
	 *
	 * @param roles
	 *            roles required to access some action (user must have at least
	 *            one of them)
	 * @param redirect
	 *            redirect to the appropriate URL in case of no login or access
	 *            denied?
	 * @throws RedirectException
	 *             the redirecting exception ( in case of no login or access
	 *             denied)
	 * @throws SendErrorException
	 *             exception thrown when the redirection cannot be done
	 */
    public void checkAccess(String[] roles, boolean redirect) throws RedirectException, SendErrorException {
        User user = getLoggedUser();
        CheckAccessResult checkAccessResult = CheckAccessResult.getAccessResult(user, roles);
        switch(checkAccessResult) {
            case ACCESS_GRANTED:
                break;
            case NO_LOGIN:
                handleNoLogin(redirect);
                break;
            case ACCESS_DENIED:
                denyAccess(redirect);
                break;
            default:
                String msg = "Unexpected checkAccessResult value: " + checkAccessResult;
                throw new AssertionError(msg);
        }
    }

    /**
	 * Redirects to URL which has been hit before login
	 *
	 * @throws RedirectException
	 *             the redirecting exception
	 */
    public void followLoginReferer() throws RedirectException {
        String loginReferer = getLoginReferer();
        if (loginReferer != null) {
            HttpSession session = Environment.get().getRequest().getSession();
            session.removeAttribute(SESSION_LOGIN_REFERER_KEY);
            throw new RedirectException(loginReferer);
        }
    }

    /**
	 * The accessDeniedRedirect getter
	 *
	 * @return the accessDeniedRedirect
	 */
    public String getAccessDeniedRedirect() {
        return this.accessDeniedRedirect;
    }

    /**
	 * Retrieve user stored in session or <code>null</code>, if user is not in
	 * session
	 *
	 * @return user stored in session or <code>null</code>, if no such user
	 *         exists
	 */
    public User getLoggedUser() {
        HttpSession session = Environment.get().getRequest().getSession();
        User user = (User) session.getAttribute(SESSION_USER_KEY);
        return user;
    }

    /**
	 * The noLoginRedirect getter
	 *
	 * @return the noLoginRedirect
	 */
    public String getNoLoginRedirect() {
        return this.noLoginRedirect;
    }

    /**
     * The accessDeniedRedirect setter
     *
     * @param accessDeniedRedirect the accessDeniedRedirect to set
     */
    public void setAccessDeniedRedirect(String accessDeniedRedirect) {
        this.accessDeniedRedirect = accessDeniedRedirect;
    }

    /**
	 * Sets the specified user in session
	 *
	 * @param user
	 */
    public void setLoggedUser(User user) {
        HttpSession session = Environment.get().getRequest().getSession();
        session.setAttribute(SESSION_USER_KEY, user);
    }

    /**
     * The noLoginRedirect setter
     *
     * @param noLoginRedirect the noLoginRedirect to set
     */
    public void setNoLoginRedirect(String noLoginRedirect) {
        this.noLoginRedirect = noLoginRedirect;
    }

    /** Removes user from session */
    public void unsetLoggedUser() {
        setLoggedUser(null);
    }

    /**
	 * Returns URL from the original action
	 *
	 * @return URL from the original action
	 */
    protected String getLoginReferer() {
        HttpSession session = Environment.get().getRequest().getSession();
        String loginReferer = (String) session.getAttribute(SESSION_LOGIN_REFERER_KEY);
        return loginReferer;
    }

    /**
	 * This method is called if user hasn't access to some action
	 *
	 * @param redirect
	 *            redirect to the appropriate URL in case of no login or access
	 *            denied?
	 * @throws SendErrorException
	 *             if no URL handling denied access is specified or redirection
	 *             is forbidden
	 * @throws RedirectException
	 *             the exception redirecting to the specified URL
	 */
    private void denyAccess(boolean redirect) throws SendErrorException, RedirectException {
        if (!redirect || this.accessDeniedRedirect == null) {
            throw new SendErrorException(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized, access denied");
        } else {
            Request request = Environment.get().getRequest();
            request.getSession().setAttribute(SESSION_DENIED_ACCESS_URL_KEY, request.getRequestURL().toString());
            throw new RedirectException(getAccessDeniedRedirect());
        }
    }

    /**
	 * This action is called when no user is logged in when trying to access a
	 * protected action
	 *
	 * @param redirect
	 *            redirect to the appropriate URL in case of no login or access
	 *            denied?
	 * @throws RedirectException
	 *             the exception redirecting to the specified URL
	 * @throws SendErrorException
	 *             if no URL handling login is specified or redirection is
	 *             forbidden
	 */
    private void handleNoLogin(boolean redirect) throws RedirectException, SendErrorException {
        if (!redirect || this.noLoginRedirect == null) {
            throw new SendErrorException(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized, not logged in");
        } else {
            Request request = Environment.get().getRequest();
            request.getSession().setAttribute(SESSION_LOGIN_REFERER_KEY, request.getRequestURL().toString());
            throw new RedirectException(getNoLoginRedirect());
        }
    }
}

/**
 * This enumeration encapsulates result of checking access
 *
 * @author Daniel Buchta
 *
 */
enum CheckAccessResult {

    ACCESS_GRANTED, NO_LOGIN, ACCESS_DENIED;

    /**
	 * Checks, if the specified user has access
	 *
	 * @param user
	 *            user to check
	 * @param roles
	 *            set of roles
	 * @return appropriate {@link CheckAccessResult} instance
	 */
    public static CheckAccessResult getAccessResult(User user, String[] roles) {
        CheckAccessResult result;
        if (user == null) {
            result = NO_LOGIN;
        } else {
            if (!hasRole(user, roles)) {
                result = ACCESS_DENIED;
            } else {
                result = ACCESS_GRANTED;
            }
        }
        return result;
    }

    /**
	 * Checks if user has (at least) one of the specified access roles
	 *
	 * @param user
	 *            user to check
	 * @param accessRoles
	 *            set of roles
	 * @return true if user has (at least) one of the specified access roles
	 */
    private static boolean hasRole(User user, String[] accessRoles) {
        boolean result = false;
        for (String role : accessRoles) {
            result = result || user.hasRole(role);
        }
        return result;
    }
}
