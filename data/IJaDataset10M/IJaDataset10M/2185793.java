package net.sourceforge.pebble.mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * A mock HttpServletRequest implementation.
 *
 * @author    Simon Brown
 */
public class MockHttpServletRequest implements HttpServletRequest {

    private Properties headers = new Properties();

    private HashMap parameters = new HashMap();

    private HashMap attributes = new HashMap();

    private HttpSession session = new MockHttpSession();

    private String contextPath;

    private String requestUri = "";

    private StringBuffer requestUrl = new StringBuffer();

    private RequestDispatcher requestDispatcher;

    private MockPrincipal userPrincipal;

    public String getAuthType() {
        return null;
    }

    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    public long getDateHeader(String name) {
        String value = getHeader(name);
        if (value != null) {
            return Long.parseLong(getHeader(name));
        } else {
            return -1;
        }
    }

    public String getHeader(String s) {
        return headers.getProperty(s);
    }

    public Enumeration getHeaders(String s) {
        return null;
    }

    public Enumeration getHeaderNames() {
        return headers.keys();
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setDateHeader(String name, long value) {
        setHeader(name, "" + value);
    }

    public int getIntHeader(String s) {
        return 0;
    }

    public String getMethod() {
        return null;
    }

    public String getPathInfo() {
        return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getContextPath() {
        return this.contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getQueryString() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public boolean isUserInRole(String s) {
        if (this.userPrincipal != null) {
            return userPrincipal.isUserInRole(s);
        } else {
            return false;
        }
    }

    public Principal getUserPrincipal() {
        return this.userPrincipal;
    }

    public void setUserPrincipal(Principal p) {
        this.userPrincipal = (MockPrincipal) p;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getRequestURI() {
        return this.requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public StringBuffer getRequestURL() {
        return this.requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = new StringBuffer(requestUrl);
    }

    public String getServletPath() {
        return null;
    }

    public HttpSession getSession(boolean b) {
        return session;
    }

    public HttpSession getSession() {
        return session;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    public Enumeration getAttributeNames() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    public void setParameter(String name, String value[]) {
        parameters.put(name, value);
    }

    public String getParameter(String s) {
        return (String) parameters.get(s);
    }

    public Enumeration getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String s) {
        return (String[]) parameters.get(s);
    }

    public Map getParameterMap() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public String getScheme() {
        return null;
    }

    public String getServerName() {
        return null;
    }

    public int getServerPort() {
        return 0;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }

    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration getLocales() {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        this.requestDispatcher = new MockRequestDispatcher(s);
        return this.requestDispatcher;
    }

    public RequestDispatcher getRequestDispatcher() {
        return this.requestDispatcher;
    }

    public String getRealPath(String s) {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public String getLocalName() {
        return null;
    }

    public String getLocalAddr() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }
}
