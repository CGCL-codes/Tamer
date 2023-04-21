package org.josso.tc60.agent;

import org.apache.catalina.*;
import org.apache.catalina.authenticator.SavedRequest;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.valves.ValveBase;
import org.josso.agent.Lookup;
import org.josso.agent.Constants;
import org.josso.agent.LocalSession;
import org.josso.agent.SingleSignOnEntry;
import org.josso.agent.SSOAgentRequest;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Single Sign-On Agent implementation for Tomcat Catalina.
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version $Id$
 */
public class SSOAgentValve extends ValveBase implements Lifecycle, SessionListener {

    /**
     * The debugging detail level for this component.
     */
    protected int debug = 0;

    /**
     * Descriptive information about this Valve implementation.
     */
    protected static String info = "org.apache.catalina.authenticator.SingleSignOn";

    /**
     * The lifecycle event support for this component.
     */
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);

    /**
     * Component started flag.
     */
    protected boolean started = false;

    private CatalinaSSOAgent _agent;

    /**
     * Catalina Session to Local Session Map.
     */
    HashMap _sessionMap = new HashMap();

    /**
     * Return the debugging detail level.
     */
    public int getDebug() {
        return (this.debug);
    }

    /**
     * Set the debugging detail level.
     *
     * @param debug The new debugging detail level
     */
    public void setDebug(int debug) {
        this.debug = debug;
    }

    public void sessionEvent(SessionEvent event) {
        LocalSession localSession = (LocalSession) _sessionMap.get(event.getSession());
        if (event.getType().equals(Session.SESSION_DESTROYED_EVENT)) localSession.expire();
    }

    /**
     * Add a lifecycle event listener to this component.
     *
     * @param listener The listener to add
     */
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
    }

    /**
     * Get the lifecycle listeners associated with this lifecycle. If this
     * Lifecycle has no listeners registered, a zero-length array is returned.
     */
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }

    /**
     * Remove a lifecycle event listener from this component.
     *
     * @param listener The listener to remove
     */
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycle.removeLifecycleListener(listener);
    }

    /**
     * Prepare for the beginning of active use of the public methods of this
     * component.  This method should be called after <code>configure()</code>,
     * and before any of the public methods of the component are utilized.
     *
     * @throws LifecycleException if this component detects a fatal error
     *                            that prevents this component from being used
     */
    public void start() throws LifecycleException {
        if (started) throw new LifecycleException("Agent already started");
        lifecycle.fireLifecycleEvent(START_EVENT, null);
        started = true;
        try {
            Lookup lookup = Lookup.getInstance();
            lookup.init("josso-agent-config.xml");
            _agent = (CatalinaSSOAgent) lookup.lookupSSOAgent();
            _agent.setDebug(debug);
            _agent.setCatalinaContainer(container);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new LifecycleException("Error starting SSO Agent : " + e.getMessage());
        }
        _agent.start();
        if (debug >= 1) log("Started");
    }

    /**
     * Gracefully terminate the active use of the public methods of this
     * component.  This method should be the last one called on a given
     * instance of this component.
     *
     * @throws LifecycleException if this component detects a fatal error
     *                            that needs to be reported
     */
    public void stop() throws LifecycleException {
        if (!started) throw new LifecycleException("Agent not started");
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;
        _agent.stop();
        if (debug >= 1) log("Stopped");
    }

    /**
     * Return descriptive information about this Valve implementation.
     */
    public String getInfo() {
        return (info);
    }

    /**
     * Perform single-sign-on support processing for this request.
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     *                 in the current processing pipeline
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    public void invoke(Request request, Response response) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) request.getRequest();
        HttpServletResponse hres = (HttpServletResponse) response.getResponse();
        if (debug >= 1) log("Processing : " + hreq.getContextPath());
        try {
            String contextPath = hreq.getContextPath();
            String vhost = hreq.getServerName();
            if ("".equals(contextPath)) contextPath = "/";
            if (!_agent.isPartnerApp(vhost, contextPath)) {
                getNext().invoke(request, response);
                if (debug >= 1) log("Context is not a josso partner app : " + hreq.getContextPath());
                return;
            }
            if (debug >= 1) log("Checking if its a josso_login_request for '" + hreq.getRequestURI() + "'");
            if (hreq.getRequestURI().endsWith(_agent.getJOSSOLoginUri())) {
                if (debug >= 1) log("josso_login_request received for uri '" + hreq.getRequestURI() + "'");
                String loginUrl = _agent.buildLoginUrl(hreq);
                if (debug >= 1) log("Redirecting to login url '" + loginUrl + "'");
                hres.sendRedirect(hres.encodeRedirectURL(loginUrl));
                return;
            }
            if (debug >= 1) log("Checking if its a josso_logout request for '" + hreq.getRequestURI() + "'");
            if (hreq.getRequestURI().endsWith(_agent.getJOSSOLogoutUri())) {
                if (debug >= 1) log("josso_logout request received for uri '" + hreq.getRequestURI() + "'");
                String logoutUrl = _agent.buildLogoutUrl(hreq);
                if (debug >= 1) log("Redirecting to logout url '" + logoutUrl + "'");
                Cookie ssoCookie = _agent.newJossoCookie(request.getContextPath(), "-");
                hres.addCookie(ssoCookie);
                hres.sendRedirect(hres.encodeRedirectURL(logoutUrl));
                return;
            }
            if (debug >= 1) log("Checking for SSO cookie");
            Cookie cookie = null;
            Cookie cookies[] = hreq.getCookies();
            if (cookies == null) cookies = new Cookie[0];
            for (int i = 0; i < cookies.length; i++) {
                if (org.josso.gateway.Constants.JOSSO_SINGLE_SIGN_ON_COOKIE.equals(cookies[i].getName())) {
                    cookie = cookies[i];
                    break;
                }
            }
            Session session = getSession(request, true);
            if (cookie == null) {
                if (_agent.getPartnerAppConfig(vhost, contextPath).isRememberMeEnabled()) {
                    if (debug >= 1) log("SSO cookie is not present, verifying optional login process ");
                    if (hreq.getRequestURI().endsWith(_agent.getJOSSOSecurityCheckUri()) && hreq.getParameter("josso_assertion_id") == null) {
                        if (debug >= 1) log(_agent.getJOSSOSecurityCheckUri() + " received without assertion.  Login Optional Process failed");
                        String requestURI = this.savedRequestURL(session);
                        hres.sendRedirect(hres.encodeRedirectURL(requestURI));
                        return;
                    }
                    if (!hreq.getRequestURI().endsWith(_agent.getJOSSOSecurityCheckUri())) {
                        if (session.getNote("josso.loginOptinal.done") == null) {
                            if (debug >= 1) log("SSO cookie is not present, attempting login optional (remember me)");
                            saveRequest(request, session);
                            session.setNote("josso.loginOptinal.done", Boolean.TRUE);
                            String loginUrl = _agent.buildLoginOptionalUrl(hreq);
                            if (debug >= 1) log("Redirecting to login url '" + loginUrl + "'");
                            hres.sendRedirect(hres.encodeRedirectURL(loginUrl));
                            return;
                        } else {
                            if (debug >= 1) log("SSO cookie is not present but login optional process already done");
                        }
                    }
                }
                if (debug >= 1) log("SSO cookie is not present, checking for outbound relaying");
                if (!(hreq.getRequestURI().endsWith(_agent.getJOSSOSecurityCheckUri()) && hreq.getParameter("josso_assertion_id") != null)) {
                    log("SSO cookie not present and relaying was not requested, skipping");
                    getNext().invoke(request, response);
                    return;
                }
            }
            String[] ignoredWebResources = _agent.getPartnerAppConfig(vhost, contextPath).getIgnoredWebRources();
            if (debug >= 1) log("Found [" + (ignoredWebResources != null ? ignoredWebResources.length + "" : "no") + "] ignored web resources ");
            if (ignoredWebResources != null && ignoredWebResources.length > 0) {
                Realm realm = request.getContext().getRealm();
                SecurityConstraint[] constraints = realm.findSecurityConstraints(request, request.getContext());
                if ((constraints != null)) {
                    for (int i = 0; i < ignoredWebResources.length; i++) {
                        String ignoredWebResource = ignoredWebResources[i];
                        for (int j = 0; j < constraints.length; j++) {
                            SecurityConstraint constraint = constraints[j];
                            if (constraint.findCollection(ignoredWebResource) != null) {
                                if (debug >= 1) log("Not subject to SSO protection :  web-resource-name:" + ignoredWebResource);
                                getNext().invoke(request, response);
                                return;
                            }
                        }
                    }
                }
            }
            String jossoSessionId = (cookie == null) ? null : cookie.getValue();
            if (debug >= 1) log("Session is: " + session);
            LocalSession localSession = new CatalinaLocalSession(session);
            if (debug >= 1) log("Executing agent...");
            _agent.setCatalinaContainer(request.getContext());
            if (debug >= 1) log("Checking if its a josso_security_check for '" + hreq.getRequestURI() + "'");
            if (hreq.getRequestURI().endsWith(_agent.getJOSSOSecurityCheckUri()) && hreq.getParameter("josso_assertion_id") != null) {
                if (debug >= 1) log("josso_security_check received for uri '" + hreq.getRequestURI() + "' assertion id '" + hreq.getParameter("josso_assertion_id"));
                String assertionId = hreq.getParameter(Constants.JOSSO_ASSERTION_ID_PARAMETER);
                CatalinaSSOAgentRequest relayRequest;
                if (debug >= 1) log("Outbound relaying requested for assertion id [" + assertionId + "]");
                relayRequest = new CatalinaSSOAgentRequest(SSOAgentRequest.ACTION_RELAY, null, localSession, assertionId);
                relayRequest.setRequest(hreq);
                relayRequest.setResponse(hres);
                relayRequest.setContext(request.getContext());
                SingleSignOnEntry entry = _agent.processRequest(relayRequest);
                if (entry == null) {
                    if (debug >= 1) log("Outbound relaying failed for assertion id [" + assertionId + "], no Principal found.");
                    throw new RuntimeException("Outbound relaying failed. No Principal found. Verify your SSO Agent Configuration!");
                }
                if (debug >= 1) log("Outbound relaying succesfull for assertion id [" + assertionId + "]");
                if (debug >= 1) log("Assertion id [" + assertionId + "] mapped to SSO session id [" + entry.ssoId + "]");
                cookie = _agent.newJossoCookie(request.getContextPath(), entry.ssoId);
                hres.addCookie(cookie);
                String requestURI = savedRequestURL(session);
                if (requestURI == null) {
                    requestURI = hreq.getRequestURI().substring(0, (hreq.getRequestURI().length() - _agent.getJOSSOSecurityCheckUri().length()));
                    String singlePointOfAccess = _agent.getSinglePointOfAccess();
                    if (singlePointOfAccess != null) {
                        requestURI = singlePointOfAccess + requestURI;
                    } else {
                        String reverseProxyHost = hreq.getHeader(org.josso.gateway.Constants.JOSSO_REVERSE_PROXY_HEADER);
                        if (reverseProxyHost != null) {
                            requestURI = reverseProxyHost + requestURI;
                        }
                    }
                    if (debug >= 1) log("No saved request found, using : '" + requestURI + "'");
                }
                if (debug >= 1) log("Redirecting to original '" + requestURI + "'");
                hres.sendRedirect(hres.encodeRedirectURL(requestURI));
                return;
            }
            CatalinaSSOAgentRequest r;
            log("Creating Security Context for Session [" + session + "]");
            r = new CatalinaSSOAgentRequest(SSOAgentRequest.ACTION_ESTABLISH_SECURITY_CONTEXT, jossoSessionId, localSession);
            r.setRequest(hreq);
            r.setResponse(hres);
            r.setContext(request.getContext());
            SingleSignOnEntry entry = _agent.processRequest(r);
            if (debug >= 1) log("Executed agent.");
            if (_sessionMap.get(localSession.getWrapped()) == null) {
                session.addSessionListener(this);
                _sessionMap.put(session, localSession);
            }
            if (debug >= 1) log("Process request for '" + hreq.getRequestURI() + "'");
            if (entry != null) {
                if (debug >= 1) log("Principal '" + entry.principal + "' has already been authenticated");
                (request).setAuthType(entry.authType);
                (request).setUserPrincipal(entry.principal);
            }
            hreq.setAttribute("org.josso.agent.gateway-login-url", _agent.getGatewayLoginUrl());
            hreq.setAttribute("org.josso.agent.gateway-logout-url", _agent.getGatewayLogoutUrl());
            hreq.setAttribute("org.josso.agent.ssoSessionid", jossoSessionId);
            getNext().invoke(request, response);
        } catch (Throwable t) {
            request.setAttribute(Globals.EXCEPTION_ATTR, t);
            response.setError();
            getNext().invoke(request, response);
            return;
        } finally {
            if (debug >= 1) log("Processed : " + hreq.getContextPath());
        }
    }

    /**
     * Return a String rendering of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("SingleSignOn[");
        sb.append(container != null ? container.getName() : "");
        sb.append("]");
        return (sb.toString());
    }

    /**
     * Return the internal Session that is associated with this HttpRequest,
     * or <code>null</code> if there is no such Session.
     *
     * @param request The HttpRequest we are processing
     */
    protected Session getSession(Request request) {
        return (getSession(request, false));
    }

    /**
     * Return the internal Session that is associated with this HttpRequest,
     * possibly creating a new one if necessary, or <code>null</code> if
     * there is no such session and we did not create one.
     *
     * @param request The HttpRequest we are processing
     * @param create  Should we create a session if needed?
     */
    protected Session getSession(Request request, boolean create) {
        HttpServletRequest hreq = (HttpServletRequest) request.getRequest();
        HttpSession hses = hreq.getSession(create);
        if (debug >= 1) log("getSession() : hses " + hses);
        if (hses == null) return (null);
        Manager manager = request.getContext().getManager();
        if (debug >= 1) log("getSession() : manager is " + manager);
        if (manager == null) return (null); else {
            try {
                return (manager.findSession(hses.getId()));
            } catch (IOException e) {
                return (null);
            }
        }
    }

    /**
     * Log a message on the Logger associated with our Container (if any).
     *
     * @param message Message to be logged
     */
    protected void log(String message) {
        if (_agent != null) _agent.log(message);
    }

    /**
     * Log a message on the Logger associated with our Container (if any).
     *
     * @param message   Message to be logged
     * @param throwable Associated exception
     */
    protected void log(String message, Throwable throwable) {
        if (_agent != null) _agent.log(message, throwable);
    }

    /**
     * Return the request URI (with the corresponding query string, if any)
     * from the saved request so that we can redirect to it.
     *
     * @param session Our current session
     */
    private String savedRequestURL(Session session) {
        SavedRequest saved = (SavedRequest) session.getNote(org.apache.catalina.authenticator.Constants.FORM_REQUEST_NOTE);
        if (saved == null) return (null);
        StringBuffer sb = new StringBuffer(saved.getRequestURI());
        if (saved.getQueryString() != null) {
            sb.append('?');
            sb.append(saved.getQueryString());
        }
        return (sb.toString());
    }

    /**
     * Save the original request information into our session.
     *
     * @param request The request to be saved
     * @param session The session to contain the saved information
     * @throws IOException
     */
    protected void saveRequest(Request request, Session session) throws IOException {
        SavedRequest saved = new SavedRequest();
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) saved.addCookie(cookies[i]);
        }
        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Enumeration values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                String value = (String) values.nextElement();
                saved.addHeader(name, value);
            }
        }
        Enumeration locales = request.getLocales();
        while (locales.hasMoreElements()) {
            Locale locale = (Locale) locales.nextElement();
            saved.addLocale(locale);
        }
        saved.setMethod(request.getMethod());
        saved.setQueryString(request.getQueryString());
        saved.setRequestURI(request.getRequestURI());
        session.setNote(org.apache.catalina.authenticator.Constants.FORM_REQUEST_NOTE, saved);
    }
}
