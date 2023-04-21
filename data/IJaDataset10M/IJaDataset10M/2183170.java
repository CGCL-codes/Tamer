package net.caimito.tapestry.sesame;

import java.util.HashMap;
import java.util.Map;
import net.caimito.tapestry.sesame.providers.AuthenticationProvider;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;

public class Authentication {

    protected static final String SESAME_ORGINAL_PAGE_REQUESTED = "net.caimito.tapestry.sesame.originalPageRequested";

    public static final String SESAME_LOGIN_PAGE_NAME = "net.caimito.tapestry.sesame.loginPageName";

    private RequestGlobals requestGlobals;

    private AuthenticationProvider authenticationProvider;

    private RememberMe rememberMe;

    private Map<String, String> configuration = new HashMap<String, String>();

    public Authentication(RequestGlobals requestGlobals, RememberMe rememberMe, AuthenticationProvider provider, Map<String, String> contributions) {
        this.requestGlobals = requestGlobals;
        this.authenticationProvider = provider;
        this.rememberMe = rememberMe;
        configuration.putAll(contributions);
    }

    public String getLoginPageName() {
        return configuration.get(SESAME_LOGIN_PAGE_NAME);
    }

    /**
	 * Authenticates based on username and password and returns the page the unauthenticated user
	 * tried to access before she was redirected to the login page.
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws AuthenticationException 
	 */
    public Object authenticate(String username, String password) throws AuthenticationException {
        Session session = requestGlobals.getRequest().getSession(true);
        if (session != null) {
            try {
                AuthenticationToken authenticationToken = authenticationProvider.authenticate(username, password);
                session.setAttribute(SecurityChecker.AUTHENTICATION_TOKEN, authenticationToken);
                rememberMe.rememberMe(username, password);
                return session.getAttribute(SESAME_ORGINAL_PAGE_REQUESTED);
            } catch (AuthenticationException e) {
                throw new AuthenticationException(e);
            }
        } else throw new AuthenticationException("Could not open the HTTP session. Cannot store authentication token into session.");
    }
}
