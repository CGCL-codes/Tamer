package com.yahoo.zookeeper.server.util;

import com.yahoo.zookeeper.server.ServerCnxn;

/**
 * Application must implement this interface and register its instance with
 * the {@link ObserverManager}.
 */
public interface ConnectionObserver {

    /**
     * A new client connection started
     * @param sc the new connection instance
     */
    public void onNew(ServerCnxn sc);

    /**
     * A client connected closed
     * @param sc the connection instance
     */
    public void onClose(ServerCnxn sc);
}
