package org.jaffa.security;

/**
 *
 * @author  paule
 * @version
 */
public class FakeRequest implements javax.servlet.http.HttpServletRequest {

    String m_name = null;

    /** Creates new FakeRequest */
    public FakeRequest(String name) {
        m_name = name;
    }

    public java.lang.String getContextPath() {
        return null;
    }

    /** @deprecated */
    public java.lang.String getRealPath(java.lang.String str) {
        return null;
    }

    public java.lang.String getRequestedSessionId() {
        return null;
    }

    public java.io.BufferedReader getReader() throws java.io.IOException {
        return null;
    }

    public java.lang.String getAuthType() {
        return null;
    }

    public java.util.Enumeration getHeaders(java.lang.String str) {
        return null;
    }

    public long getDateHeader(java.lang.String str) {
        return 0;
    }

    public javax.servlet.ServletInputStream getInputStream() throws java.io.IOException {
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public java.security.Principal getUserPrincipal() {
        return new FakePrincipal(m_name);
    }

    public void setAttribute(java.lang.String str, java.lang.Object obj) {
    }

    public java.lang.String getPathInfo() {
        return null;
    }

    public java.lang.String getRemoteUser() {
        return null;
    }

    public java.lang.String getHeader(java.lang.String str) {
        return null;
    }

    public java.lang.String getCharacterEncoding() {
        return null;
    }

    public java.lang.String getServerName() {
        return null;
    }

    public java.util.Enumeration getLocales() {
        return null;
    }

    public javax.servlet.http.HttpSession getSession() {
        return null;
    }

    public void removeAttribute(java.lang.String str) {
        return;
    }

    public java.lang.String getContentType() {
        return null;
    }

    public java.lang.String getScheme() {
        return null;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public int getServerPort() {
        return 0;
    }

    public javax.servlet.http.Cookie[] getCookies() {
        return null;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /** @deprecated */
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public java.lang.String getMethod() {
        return null;
    }

    public java.lang.String getParameter(java.lang.String str) {
        return null;
    }

    /** PAUL has Roles CLERK and MANAGER
     *  MAHESH has Role CLERK
     */
    public boolean isUserInRole(java.lang.String str) {
        if (m_name.equals("PAUL")) {
            return (str != null && (str.equals("CLERK") || str.equals("SUPERVISOR") || str.equals("MANAGER")));
        } else if (m_name.equals("MAHESH")) {
            return (str != null && (str.equals("CLERK") || str.equals("SUPERVISOR")));
        } else return false;
    }

    public java.util.Enumeration getParameterNames() {
        return null;
    }

    public java.lang.String getServletPath() {
        return null;
    }

    public java.lang.String getRequestURI() {
        return null;
    }

    public java.lang.String getPathTranslated() {
        return null;
    }

    public java.lang.String[] getParameterValues(java.lang.String str) {
        return null;
    }

    public java.util.Locale getLocale() {
        return null;
    }

    public java.lang.String getProtocol() {
        return null;
    }

    public java.lang.String getRemoteAddr() {
        return null;
    }

    public int getContentLength() {
        return 0;
    }

    public javax.servlet.http.HttpSession getSession(boolean param) {
        return null;
    }

    public java.util.Enumeration getHeaderNames() {
        return null;
    }

    public javax.servlet.RequestDispatcher getRequestDispatcher(java.lang.String str) {
        return null;
    }

    public java.lang.String getRemoteHost() {
        return null;
    }

    public java.lang.String getQueryString() {
        return null;
    }

    public int getIntHeader(java.lang.String str) {
        return 0;
    }

    public java.lang.Object getAttribute(java.lang.String str) {
        return null;
    }

    public java.util.Enumeration getAttributeNames() {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    public java.util.Map getParameterMap() {
        return null;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public void setCharacterEncoding(String str) throws java.io.UnsupportedEncodingException {
    }

    public int getLocalPort() {
        return 8080;
    }

    public String getLocalAddr() {
        return null;
    }

    public String getLocalName() {
        return null;
    }

    public int getRemotePort() {
        return 8080;
    }
}
