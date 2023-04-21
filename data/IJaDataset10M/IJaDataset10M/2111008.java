package com.mycila.testing.plugins.jetty;

import org.eclipse.jetty.server.Server;

/**
 * The server lifecycle listener which allow customization of the {@link Server} configuration.
 */
public interface ServerLifeCycleListener extends WebappLifeCycleListener {

    /**
     * Handler method which will be called before the {@code Server} starts.
     * 
     * @param server
     *            the server which will start.
     */
    void beforeServerStart(Server server);

    /**
     * Handler method which will be called after the {@code Server} started.
     * 
     * @param server
     *            the server which is started.
     */
    void afterServerStart(Server server);

    /**
     * Handler method which will be called before the {@code Server} stops.
     * 
     * @param server
     *            the server which will stop.
     */
    void beforeServerStop(Server server);

    /**
     * Handler method which will be called after the {@code Server} stopped.
     * 
     * @param server
     *            the server which is stopped.
     */
    void afterServerStop(Server server);
}
