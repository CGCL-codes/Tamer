package oxygen.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oxygen.util.StringUtils;

public abstract class ServletInteractionContext implements WebInteractionContext {

    private static String COOKIE_USERNAME_NAME = "oxygen.wiki.5.username";

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected ServletContext sctx;

    protected boolean multipart;

    protected WebFileUpload webfileupload0;

    protected ServletInteractionContext(ServletContext sctx0, HttpServletRequest request0, HttpServletResponse response0) throws Exception {
        request = request0;
        response = response0;
        sctx = sctx0;
        request.getSession(true);
        String s = request.getContentType();
        multipart = ("post".equals(request.getMethod().toLowerCase()) && s != null && s.toLowerCase().startsWith(WebConstants.MULTIPART_PREFIX));
    }

    public boolean isMultipartContent() {
        return multipart;
    }

    public File[] getUploadedFiles() throws Exception {
        return (multipart ? webfileupload().getFiles() : null);
    }

    public String getSessionId() {
        return request.getSession(true).getId();
    }

    public Object getSessionAttribute(String key) {
        return request.getSession(true).getAttribute(key);
    }

    public void setSessionAttribute(String key, Object value) {
        request.getSession(true).setAttribute(key, value);
    }

    public void invalidateSession() {
        request.getSession(true).invalidate();
    }

    public PrintWriter getWriter() throws Exception {
        return response.getWriter();
    }

    public String getParameter(String string) throws Exception {
        return (multipart ? webfileupload().getParameter(string) : request.getParameter(string));
    }

    public Object getAttribute(String key) {
        return request.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        request.setAttribute(key, value);
    }

    public String getHeader(String string) {
        return request.getHeader(string);
    }

    public String[] getParameterValues(String string) throws Exception {
        return (multipart ? webfileupload().getParameterValues(string) : request.getParameterValues(string));
    }

    public Enumeration getParameterNames() throws Exception {
        return (multipart ? webfileupload().getParameterNames() : request.getParameterNames());
    }

    public String getContextPath() {
        return request.getContextPath();
    }

    public Locale getLocale() {
        return request.getLocale();
    }

    public void sendRedirect(String s) throws Exception {
        response.sendRedirect(s);
    }

    public void include(String jsp) throws Exception {
        request.getRequestDispatcher(jsp).include(request, response);
    }

    /**
   * Gets the username, searching in the following order
   * - from cookie
   * - from log-in username (container managed)
   * - return remote IP address
   */
    public String getUserName() throws Exception {
        String username = null;
        Cookie cookie = getCookie(COOKIE_USERNAME_NAME);
        if (cookie != null) {
            username = cookie.getValue();
        }
        if (StringUtils.isBlank(username)) {
            username = request.getRemoteUser();
        }
        if (StringUtils.isBlank(username)) {
            username = request.getRemoteAddr();
        }
        return username;
    }

    /**
   * Sets the preferred username in the cookie
   * @param req
   * @param resp
   * @param username
   */
    public void setUserName(String username) throws Exception {
        Cookie cookie = new Cookie(COOKIE_USERNAME_NAME, username);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.MAX_VALUE / 10);
        response.addCookie(cookie);
    }

    /**
   * Remove the cookie representing the preferred user name
   * @param req
   * @param resp
   */
    public void removeUserName() throws Exception {
        Cookie cookie = new Cookie(COOKIE_USERNAME_NAME, "blah");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void removeSessionAttribute(String key) {
        request.getSession(true).removeAttribute(key);
    }

    public OutputStream getOutputStream() throws Exception {
        return response.getOutputStream();
    }

    public void setContentType(String mimetype) {
        response.setContentType(mimetype);
    }

    public void setHeader(String string, String string2) {
        response.setHeader(string, string2);
    }

    public String getMimeType(String name) {
        return sctx.getMimeType(name);
    }

    public String encodeURL(String s, boolean redirectURL) {
        return (redirectURL ? response.encodeRedirectURL(s) : response.encodeURL(s));
    }

    public int getContentLength() {
        return request.getContentLength();
    }

    public InputStream getInputStream() throws IOException {
        return request.getInputStream();
    }

    public boolean allowOnlyHTMLFragments() {
        return false;
    }

    public Map getParameterMap() throws Exception {
        return (multipart ? webfileupload().getParameterMap() : request.getParameterMap());
    }

    public boolean isUserInRole(String group) throws Exception {
        return request.isUserInRole(group);
    }

    public String getRemoteAddress() {
        return request.getRemoteAddr();
    }

    public boolean isCommitted() {
        return response.isCommitted();
    }

    private Cookie getCookie(String cookieName) {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookieName.equals(cookies[i].getName())) {
                    cookie = cookies[i];
                    break;
                }
            }
        }
        return cookie;
    }

    private static String getCookieInfo(Cookie cookie) {
        return " Name: " + cookie.getName() + " Value: " + cookie.getValue() + " Domain: " + cookie.getDomain() + " Path: " + cookie.getPath() + " MaxAge: " + cookie.getMaxAge();
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public URL getResource(String s) throws Exception {
        return sctx.getResource(s);
    }

    protected long getMaxUploadSize() {
        return Long.MAX_VALUE;
    }

    private WebFileUpload webfileupload() throws Exception {
        if (webfileupload0 == null) {
            webfileupload0 = new WebFileUpload(this, getMaxUploadSize(), request.getQueryString());
        }
        return webfileupload0;
    }

    public String getBaseURL() {
        String s = null;
        if (s == null) {
            s = (request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
        }
        return s;
    }
}
