package com.yahoo.zookeeper.jmx.server.quorum;

/**
 * A quorum server MBean.
 */
public interface ServerMXBean {

    /**
     * @return name of the server MBean
     */
    public String getName();

    /**
     * @return the start time the server 
     */
    public String getStartTime();
}
