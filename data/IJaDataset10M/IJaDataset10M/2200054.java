package com.worldware.servlet;

import javax.servlet.*;
import java.util.*;
import com.worldware.misc.*;

public class wwServletContext implements ServletContext {

    Hashtable m_attributes = new Hashtable();

    Hashtable m_initParams = new Hashtable();

    String m_contextName;

    public wwServletContext(String contextName) {
        m_contextName = contextName;
    }

    /**    Returns the servlet of the specified name, or null if not found. */
    public Servlet getServlet(String servletName) {
        return null;
    }

    public java.util.Enumeration getAttributeNames() {
        return m_attributes.keys();
    }

    /**  Returns the value of the named attribute of the network service, or null if the attribute does not exist. */
    public Object getAttribute(String name) {
        return m_attributes.get(name);
    }

    public java.util.Enumeration getInitParameterNames() {
        return m_initParams.keys();
    }

    public java.lang.String getInitParameter(java.lang.String name) {
        return (String) m_initParams.get(name);
    }

    public ServletContext getContext(java.lang.String uripath) {
        return null;
    }

    /**     Returns the mime type of the specified file, or null if not known. */
    public String getMimeType(String file) {
        return "text/html";
    }

    /**     Applies alias rules to the specified virtual path and returns the corresponding real path. */
    public String getRealPath(String path) {
        return null;
    }

    /**     Returns the name and version of the network service under which the servlet is running. 
	*/
    public String getServerInfo() {
        return "trellis, version alpha " + DateStamp.buildDate();
    }

    /**  Returns an enumeration of the Servlet object names in this server. */
    public Enumeration getServletNames() {
        return new EmptyEnum();
    }

    /**      Returns an enumeration of the Servlet objects in this server. Deprecated. */
    public Enumeration getServlets() {
        return new EmptyEnum();
    }

    public void setAttribute(String name, Object object) {
        m_attributes.put(name, object);
    }

    public void removeAttribute(java.lang.String name) {
        m_attributes.remove(name);
    }

    /** 
     Write the stacktrace and the given message string to the servlet log file. 
	*/
    public void log(Exception e, String msg) {
        Log.log(Log.MASK_ALWAYS, "Servlet: " + msg);
        Log.log(Log.MASK_ALWAYS, "Servlet: " + e);
    }

    /** 
		Write the stacktrace and the given message string to the servlet log file. 
	*/
    public void log(String msg, Throwable t) {
        Log.log(Log.MASK_ALWAYS, "Servlet: " + msg);
        Log.log(Log.MASK_ALWAYS, "Servlet: " + t);
    }

    /**      Writes the given message string to the servlet log file. */
    public void log(String msg) {
        Log.log(Log.MASK_ALWAYS, "Servlet: " + msg);
    }

    public int getMajorVersion() {
        return 2;
    }

    public int getMinorVersion() {
        return 3;
    }

    /** Not supported */
    public java.util.Set getResourcePaths(java.lang.String path) {
        return null;
    }

    /** Not supported */
    public java.net.URL getResource(java.lang.String path) {
        return null;
    }

    public java.io.InputStream getResourceAsStream(java.lang.String path) {
        return null;
    }

    /** Not supported, or used. Stub to allow things to compile */
    public RequestDispatcher getRequestDispatcher(java.lang.String path) {
        return null;
    }

    /** Not supported, or used. Stub to allow things to compile */
    public RequestDispatcher getNamedDispatcher(java.lang.String name) {
        return null;
    }

    public String getServletContextName() {
        return m_contextName;
    }
}
