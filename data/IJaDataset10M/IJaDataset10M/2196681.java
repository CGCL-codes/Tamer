package com.hp.hpl.deli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
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

public class MockHttpServletRequest implements HttpServletRequest {

    private Vector<String> headers = null;

    private Map<String, Vector<String>> headerMap = null;

    public MockHttpServletRequest(String[] values) {
        headerMap = new HashMap<String, Vector<String>>();
        headers = new Vector<String>();
        for (String header : values) {
            headers.add(header);
            String headerName = header.substring(0, header.indexOf(":"));
            String value = header.substring(header.indexOf(":") + 1, header.length());
            System.out.println("header: " + headerName + ", value: " + value);
            Vector<String> v = headerMap.containsKey(headerName) ? headerMap.get(headerName) : new Vector<String>();
            v.add(value);
            headerMap.put(headerName, v);
        }
    }

    public Enumeration<String> getHeaders() {
        return headers.elements();
    }

    public String getAuthType() {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public Cookie[] getCookies() {
        return null;
    }

    public long getDateHeader(String arg0) {
        return 0;
    }

    public String getHeader(String arg0) {
        return headerMap.containsKey(arg0) ? headerMap.get(arg0).firstElement() : null;
    }

    public Enumeration<String> getHeaderNames() {
        Vector<String> v = new Vector<String>();
        for (String header : headers) {
            v.add(header.substring(0, header.indexOf(":")));
        }
        return v.elements();
    }

    public Enumeration<String> getHeaders(String arg0) {
        return headerMap.containsKey(arg0) ? headerMap.get(arg0).elements() : null;
    }

    public int getIntHeader(String arg0) {
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

    public String getQueryString() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public String getRequestURI() {
        return null;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getServletPath() {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public HttpSession getSession(boolean arg0) {
        return null;
    }

    public Principal getUserPrincipal() {
        return null;
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

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isUserInRole(String arg0) {
        return false;
    }

    public Object getAttribute(String arg0) {
        return null;
    }

    public Enumeration<String> getAttributeNames() {
        return null;
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

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public String getLocalAddr() {
        return null;
    }

    public String getLocalName() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration<String> getLocales() {
        return null;
    }

    public String getParameter(String arg0) {
        return null;
    }

    public Map getParameterMap() {
        return null;
    }

    public Enumeration<String> getParameterNames() {
        return null;
    }

    public String[] getParameterValues(String arg0) {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRealPath(String arg0) {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public RequestDispatcher getRequestDispatcher(String arg0) {
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

    public boolean isSecure() {
        return false;
    }

    public void removeAttribute(String arg0) {
    }

    public void setAttribute(String arg0, Object arg1) {
    }

    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
    }
}
