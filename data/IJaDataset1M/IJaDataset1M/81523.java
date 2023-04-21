package org.shortbrain.maven.jetty;

import org.mortbay.jetty.webapp.WebAppContext;

/**
 * JettyPluginServer
 * 
 * 
 * Type to hide differences in API for different versions
 * of Jetty for Server class.
 *
 */
public interface JettyPluginServer extends Proxy {

    public void setRequestLog(Object requestLog);

    public Object getRequestLog();

    public void setConnectors(Object[] connectors) throws Exception;

    public Object[] getConnectors();

    public void setUserRealms(Object[] realms) throws Exception;

    public Object[] getUserRealms();

    public void configureHandlers() throws Exception;

    public void addWebApplication(WebAppContext webapp) throws Exception;

    public void start() throws Exception;

    public Object createDefaultConnector(String port) throws Exception;

    public void join() throws Exception;
}
