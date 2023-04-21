package org.nodevision.portal.wrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.nodevision.portal.utils.Constants;

public class NVHttpServletRequestInclude implements HttpServletRequest {

    private HashMap params = new HashMap();

    private NVHttpServletRequest request;

    private String contextPath;

    private HttpSession session;

    private String query;

    private boolean isValid;

    private String requestedURI;

    private String servletPath;

    private String pathInfo;

    public NVHttpServletRequestInclude(NVHttpServletRequest request) {
        this.request = request;
        Enumeration en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String paramname = (String) en.nextElement();
            if (!Constants.getPortalParams().contains(paramname)) {
                params.put(paramname, request.getParameterValues(paramname));
            }
        }
        String query;
        query = request.getQueryString();
        if (null != query) {
            Vector portalparams = Constants.getPortalParams();
            for (int z = 0; z < portalparams.size(); z++) {
                query = query.replaceAll("(&)?" + portalparams.get(z).toString() + "=([^&])*(&)?", "");
            }
        }
        contextPath = request.getContextPath();
        isValid = request.isRequestedSessionIdValid();
    }

    public Object getAttribute(String attr) {
        return request.getAttribute(attr);
    }

    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    public String getAuthType() {
        return request.getAuthType();
    }

    public String getCharacterEncoding() {
        return null;
    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public long getDateHeader(String header) {
        return request.getDateHeader(header);
    }

    public String getHeader(String header) {
        return request.getHeader(header);
    }

    public Enumeration getHeaderNames() {
        return request.getHeaderNames();
    }

    public Enumeration getHeaders(String headers) {
        return request.getHeaders(headers);
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public int getIntHeader(String header) {
        return request.getIntHeader(header);
    }

    public String getLocalAddr() {
        return request.getLocalAddr();
    }

    public Locale getLocale() {
        Locale locale = (Locale) request.request.getSession(true).getAttribute(Constants.PORTAL_LOCALE);
        return locale;
    }

    public Enumeration getLocales() {
        Locale locale = (Locale) request.request.getSession(true).getAttribute(Constants.PORTAL_LOCALE);
        Vector locales = new Vector();
        locales.add(locale);
        return Collections.enumeration(locales);
    }

    public String getLocalName() {
        return request.getLocalName();
    }

    public int getLocalPort() {
        return request.getLocalPort();
    }

    public String getMethod() {
        return "GET";
    }

    public String getParameter(String key) {
        if (params.get(key) instanceof String[]) {
            String[] temp = (String[]) params.get(key);
            return temp[0];
        } else {
            return (String) params.get(key);
        }
    }

    public Map getParameterMap() {
        return params;
    }

    public Enumeration getParameterNames() {
        Enumeration en = Collections.enumeration(params.keySet());
        return en;
    }

    public String[] getParameterValues(String key) {
        if (params.get(key) instanceof String[]) {
            String[] temp = (String[]) params.get(key);
            return temp;
        } else {
            return new String[] { (String) params.get(key) };
        }
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public String getPathTranslated() {
        return request.getPathTranslated();
    }

    public String getProtocol() {
        return null;
    }

    public String getQueryString() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRealPath(String path) {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public int getRemotePort() {
        return request.getRemotePort();
    }

    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    public RequestDispatcher getRequestDispatcher(String target) {
        return request.getRequestDispatcher(target);
    }

    public String getRequestedSessionId() {
        return request.getRequestedSessionId();
    }

    public String getRequestURI() {
        return requestedURI;
    }

    public void setRequestedURI(String requestedURI) {
        this.requestedURI = requestedURI;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getScheme() {
        return request.getScheme();
    }

    public String getServerName() {
        return request.getServerName();
    }

    public int getServerPort() {
        return request.getServerPort();
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public HttpSession getSession() {
        if (null == session) {
            return request.getSession();
        }
        return session;
    }

    public HttpSession getSession(boolean create) {
        if (null == session) {
            return request.getSession(create);
        }
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return request.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return request.isRequestedSessionIdFromUrl();
    }

    public boolean isRequestedSessionIdFromURL() {
        return request.isRequestedSessionIdFromURL();
    }

    public void setIsRequestedSessionIdValid(boolean valid) {
        isValid = valid;
    }

    public boolean isRequestedSessionIdValid() {
        return isValid;
    }

    public boolean isSecure() {
        return request.isSecure();
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    public void removeAttribute(String attr) {
        request.removeAttribute(attr);
    }

    public void setAttribute(String attr, Object value) {
        request.setAttribute(attr, value);
    }

    public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
    }

    public void emptyParameters() {
        params = new HashMap();
    }

    public void setParameters(HashMap params) {
        this.params = params;
    }
}
