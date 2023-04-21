package com.ibm.wala.model.javax.servlet;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import com.ibm.wala.model.SyntheticFactory;

public class ServletRequest implements javax.servlet.ServletRequest {

    private final java.util.Hashtable<String, Object> values = new java.util.Hashtable<String, Object>();

    private java.util.Hashtable<String, String> parameters;

    private String encoding = "iso88591-1";

    private void initParameters() {
        parameters = new java.util.Hashtable<String, String>();
        parameters.put(getInputString(), getInputString());
    }

    /**
   * The semantics of this are bogus ... be careful to hijack this.
   */
    protected static String getInputString() {
        return "some input string";
    }

    /**
   * The semantics of this are bogus ... be careful to hijack this.
   */
    public ServletRequest() {
    }

    public Object getAttribute(String name) {
        if (name.length() > 5) {
            return values.get(name);
        } else {
            return SyntheticFactory.getObject();
        }
    }

    public java.util.Enumeration<String> getAttributeNames() {
        return values.keys();
    }

    public java.lang.String getCharacterEncoding() {
        return encoding;
    }

    public int getContentLength() {
        return -1;
    }

    public java.lang.String getContentType() {
        return getInputString();
    }

    public javax.servlet.ServletInputStream getInputStream() {
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                String s = getInputString();
                int n = s.charAt(0);
                return n;
            }
        };
    }

    public java.util.Locale getLocale() {
        return java.util.Locale.getDefault();
    }

    @SuppressWarnings("unchecked")
    public java.util.Enumeration getLocales() {
        return new java.util.Enumeration() {

            private boolean done = false;

            public boolean hasMoreElements() {
                return !done;
            }

            public Object nextElement() {
                done = true;
                return getLocale();
            }
        };
    }

    public String getParameter(String name) {
        return getInputString();
    }

    public java.util.Map<String, String> getParameterMap() {
        initParameters();
        return parameters;
    }

    public java.util.Enumeration<String> getParameterNames() {
        initParameters();
        return parameters.keys();
    }

    public String[] getParameterValues(String name) {
        initParameters();
        return new String[] { parameters.get(name) };
    }

    public java.lang.String getProtocol() {
        return getInputString();
    }

    public java.io.BufferedReader getReader() {
        return null;
    }

    public String getRealPath(String path) {
        return path;
    }

    public String getRemoteAddr() {
        return "0.0.0.0";
    }

    public String getRemoteHost() {
        return "remotehost";
    }

    public javax.servlet.RequestDispatcher getRequestDispatcher(String path) {
        return new RequestDispatcher();
    }

    public String getScheme() {
        return getInputString();
    }

    public String getServerName() {
        return "localhost.localdomain";
    }

    public int getServerPort() {
        return 80;
    }

    public boolean isSecure() {
        return false;
    }

    public void removeAttribute(String name) {
        values.remove(name);
    }

    public void setAttribute(String name, Object o) {
        values.put(name, o);
    }

    public void setCharacterEncoding(String env) {
        encoding = env;
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

    public int getRemotePort() {
        return 0;
    }
}
