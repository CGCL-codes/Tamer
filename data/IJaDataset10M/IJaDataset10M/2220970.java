package com.outbrain.pajamasproxy.memcached.monitor;

import java.net.SocketAddress;
import java.util.Collection;

/**
 * A JMX MBean for interacting with a cache cluster (client).
 * 
 * @author Eran Harel
 */
public interface CacheClusterMBean {

    /**
   * @return The currently available servers, in the proxy client eyes.
   */
    public Collection<SocketAddress> getAvailableServers();

    /**
   * @return The currently unavailable servers, in the proxy client eyes.
   */
    public Collection<SocketAddress> getUnavailableServers();
}
