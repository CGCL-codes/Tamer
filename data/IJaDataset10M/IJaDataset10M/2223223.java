package org.xmatthew.spy2servers.jmx;

/**
 * @author Matthew Xie
 *
 */
public interface ComponentViewMBean {

    public String getName();

    public String getStartupDate();

    public void start();

    public void stop();

    public int getStatus();

    public String getStatusName();

    public String getComponentProperties();

    public int getMessageCount();
}
