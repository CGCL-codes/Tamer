package org.tuckey.web.testhelper;

import org.tuckey.web.filters.urlrewrite.UrlRewriteFilterTest;
import org.tuckey.web.filters.urlrewrite.utils.Log;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author Paul Tuckey
 * @version $Revision: 1 $ $Date: 2006-08-01 21:40:28 +1200 (Tue, 01 Aug 2006) $
 */
public class MockServletContext implements ServletContext {

    private static Log log = Log.getLog(MockServletContext.class);

    private final Hashtable attributes = new Hashtable();

    public ServletContext getContext(String s) {
        return new MockServletContext();
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public int getMajorVersion() {
        return 0;
    }

    public int getMinorVersion() {
        return 0;
    }

    public String getMimeType(String s) {
        return null;
    }

    public Set getResourcePaths(String s) {
        return null;
    }

    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String s) {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }

    public Servlet getServlet(String s) throws ServletException {
        return null;
    }

    public Enumeration getServlets() {
        return null;
    }

    public Enumeration getServletNames() {
        return null;
    }

    public void log(String s) {
    }

    public void log(Exception e, String s) {
    }

    public void log(String s, Throwable throwable) {
    }

    public String getRealPath(String s) {
        URL url = UrlRewriteFilterTest.class.getResource("conf-test1.xml");
        if (url == null) {
            log.error("could not get base path for comparison");
            return null;
        } else {
            String basePath = url.getFile();
            log.debug("TEST ONLY using base path of " + basePath);
            if (basePath.endsWith("conf-test1.xml")) basePath = basePath.substring(0, basePath.length() - "conf-test1.xml".length());
            if (basePath.endsWith("/")) basePath = basePath.substring(0, basePath.length() - 1);
            return basePath + (s == null ? "" : s);
        }
    }

    public String getServerInfo() {
        return null;
    }

    public String getInitParameter(String s) {
        return null;
    }

    public Enumeration getInitParameterNames() {
        return null;
    }

    public Enumeration getAttributeNames() {
        return null;
    }

    public void setAttribute(String name, Object value) {
        if (value != null) {
            this.attributes.put(name, value);
        } else {
            this.attributes.remove(name);
        }
    }

    public void removeAttribute(String s) {
    }

    public String getServletContextName() {
        return null;
    }
}
