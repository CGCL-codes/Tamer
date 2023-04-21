package com.yahoo.zookeeper.server.util;

import com.yahoo.zookeeper.server.ZooKeeperServer;

/**
 * Application must implement this interface and register its instance with
 * the {@link ObserverManager}.
 */
public interface ServerObserver {

    /**
     * The server just started.
     * @param server the new fully initialized instance of the server
     */
    public void onStartup(ZooKeeperServer server);

    /**
     * Tne server is about to shutdown.
     * @param server instance of zookeeper server
     */
    public void onShutdown(ZooKeeperServer server);
}
