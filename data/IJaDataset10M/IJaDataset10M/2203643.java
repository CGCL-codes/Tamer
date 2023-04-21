package org.regola.security.cas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.savedrequest.SavedRequest;
import org.acegisecurity.util.PortResolver;
import org.acegisecurity.util.PortResolverImpl;

public class CasProxyFilter extends AbstractProcessingFilter {

    /**
	 * Used to identify a CAS request for a stateful user agent, such as a web
	 * browser.
	 */
    public static final String CAS_STATEFUL_IDENTIFIER = "_cas_stateful_";

    /**
	 * Used to identify a CAS request for a stateless user agent, such as a
	 * remoting protocol client (eg Hessian, Burlap, SOAP etc). Results in a
	 * more aggressive caching strategy being used, as the absence of a
	 * <code>HttpSession</code> will result in a new authentication attempt on
	 * every request.
	 */
    public static final String CAS_STATELESS_IDENTIFIER = "_cas_stateless_";

    public static final String TICKET_ATTRIBUTE_NAME = "org.regola.security.cas.ticket.attribute";

    private PortResolver portResolver = new PortResolverImpl();

    private Collection<String> urlPatterns;

    public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
        String username = CAS_STATEFUL_IDENTIFIER;
        String password = request.getParameter("ticket");
        if (password == null) {
            for (Enumeration e = request.getAttributeNames(); e.hasMoreElements(); ) {
                String name = e.nextElement().toString();
                if (name.endsWith(TICKET_ATTRIBUTE_NAME)) password = (String) request.getAttribute(name);
            }
        }
        if (password == null) {
            password = (String) request.getAttribute(TICKET_ATTRIBUTE_NAME);
        }
        if (password == null) {
            password = "";
        }
        SavedRequest savedRequest = new SavedRequest(request, portResolver);
        request.getSession().setAttribute(AbstractProcessingFilter.ACEGI_SAVED_REQUEST_KEY, savedRequest);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        authRequest.setDetails(authenticationDetailsSource.buildDetails((HttpServletRequest) request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return match(request, urlPatterns);
    }

    private boolean match(ServletRequest req, Collection<String> patterns) {
        String url = ((HttpServletRequest) req).getRequestURI();
        logger.debug("verifico se " + url + " corrisponde");
        for (String pattern : patterns) {
            try {
                if (url.matches(pattern)) return true;
            } catch (PatternSyntaxException e) {
                logger.error("Pattern non valido " + e.getPattern());
            }
        }
        return false;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("Can only process HttpServletRequest");
        }
        if (!(response instanceof HttpServletResponse)) {
            throw new ServletException("Can only process HttpServletResponse");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (requiresAuthentication(httpRequest, httpResponse)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Request is to process authentication");
            }
            Authentication authResult;
            try {
                onPreAuthentication(httpRequest, httpResponse);
                authResult = attemptAuthentication(httpRequest);
            } catch (AuthenticationException failed) {
                unsuccessfulAuthentication(httpRequest, httpResponse, failed);
                return;
            }
            successfulAuthentication(httpRequest, httpResponse, authResult);
        }
        chain.doFilter(request, response);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success: " + authResult.toString());
        }
        SecurityContextHolder.getContext().setAuthentication(authResult);
        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain the following Authentication: '" + authResult + "'");
        }
        onSuccessfulAuthentication(request, response, authResult);
        getRememberMeServices().loginSuccess(request, response, authResult);
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
    }

    /**
	 * This filter by default responds to
	 * <code>/j_acegi_cas_security_check</code>.
	 * 
	 * @return the default
	 */
    public String getDefaultFilterProcessesUrl() {
        return "/j_acegi_cas_security_check";
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    public void initFilterProcessesUrl() {
        urlPatterns = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(getFilterProcessesUrl(), ",");
        while (st.hasMoreTokens()) {
            urlPatterns.add(st.nextToken());
        }
    }
}
