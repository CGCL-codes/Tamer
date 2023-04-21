package net.sf.freehost3270.client;

import java.io.Serializable;

/**
 * Holds information about a destination terminal server host.
 *
 * @since 0.1
 */
public class Host implements Serializable {

    private String friendlyName;

    private String hostName;

    private boolean isDefault;

    private int port;

    public Host(String hostName, int port, String friendlyName) {
        this.hostName = hostName;
        this.port = port;
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }
}
