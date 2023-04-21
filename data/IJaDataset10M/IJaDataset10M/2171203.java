package org.nakedobjects.webapp;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nakedobjects.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.webapp.auth.AuthenticationSessionLookupStrategy;
import org.nakedobjects.webapp.auth.AuthenticationSessionLookupStrategyDefault;

public class NakedObjectsSessionFilter implements Filter {

    /**
     * Init parameter key to lookup implementation of {@link AuthenticationSessionLookupStrategy}.
     */
    public static final String AUTHENTICATION_SESSION_LOOKUP_STRATEGY_KEY = "authenticationSessionLookupStrategy";

    /**
     * Default value for {@link #AUTHENTICATION_SESSION_LOOKUP_STRATEGY_KEY} if not specified.
     */
    public static final String AUTHENTICATION_SESSION_LOOKUP_STRATEGY_DEFAULT = AuthenticationSessionLookupStrategyDefault.class.getName();

    /**
     * Init parameter key for (typically, a logon) page to redirect to if the {@link AuthenticationSession}
     * cannot be found or is invalid.
     */
    public static final String LOGON_PAGE_KEY = "logonPage";

    private AuthenticationSessionLookupStrategy authSessionLookupStrategy;

    private String redirectResourceIfNoSession;

    public void init(FilterConfig config) throws ServletException {
        lookupAuthenticationSessionLookupStrategy(config);
        lookupRedirectIfNoSessionKey(config);
    }

    private void lookupAuthenticationSessionLookupStrategy(FilterConfig config) {
        String authLookupStrategyClassName = config.getInitParameter(AUTHENTICATION_SESSION_LOOKUP_STRATEGY_KEY);
        if (authLookupStrategyClassName == null) {
            authLookupStrategyClassName = AUTHENTICATION_SESSION_LOOKUP_STRATEGY_DEFAULT;
        }
        authSessionLookupStrategy = (AuthenticationSessionLookupStrategy) InstanceFactory.createInstance(authLookupStrategyClassName);
    }

    private void lookupRedirectIfNoSessionKey(FilterConfig config) {
        redirectResourceIfNoSession = config.getInitParameter(LOGON_PAGE_KEY);
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        AuthenticationSession authSession = authSessionLookupStrategy.lookup(request, response);
        if (!isValid(authSession)) {
            if (redirectResourceIfNoSession != null && !redirectResourceIfNoSession.equals(httpRequest.getServletPath())) {
                httpResponse.sendRedirect(redirectResourceIfNoSession);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            authSessionLookupStrategy.bind(request, response, authSession);
            NakedObjectsContext.openSession(authSession);
            chain.doFilter(request, response);
            NakedObjectsContext.closeSession();
        }
    }

    private boolean isValid(AuthenticationSession authSession) {
        return authSession != null && getAuthenticationManager().isSessionValid(authSession);
    }

    private static AuthenticationManager getAuthenticationManager() {
        return NakedObjectsContext.getAuthenticationManager();
    }
}
