package org.coos.messaging.jmx;

import java.util.Map;

/**
 * Management Bean interface for COOS instance.
 *
 * @author Robert Bjarum, Tellu AS
 * @since 1.1
 *
 */
public interface PluginMXBean {

    public String getEndpointName();

    public String getEndpointUri();

    public String getEndpointUuid();

    public String getEndpointState();

    public int getStartLevel();

    public boolean isConnected();

    public Map<String, String> getProperties();
}
