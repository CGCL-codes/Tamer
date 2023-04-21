package org.mortbay.jetty.ant.utils;

import org.mortbay.jetty.handler.ContextHandlerCollection;

public interface WebApplicationProxy {

    public Object getProxiedObject();

    /**
     * Starts this web application context.
     */
    public void start();

    /**
     * Stops this web application context.
     */
    public void stop();

    /**
     * Creates a new Jetty web application context from this object.
     * 
     * @param contexts collection of context this application should be added
     *            to.
     */
    public void createApplicationContext(ContextHandlerCollection contexts);
}
