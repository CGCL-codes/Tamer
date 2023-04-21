package org.xebra.scp.db.peer;

/**
 * An object used to track the hostname and port of a system listening for 
 * SCP events.
 * 
 * @author Rafael Chargel
 * @version $Revision: $
 */
public class EventListener {

    private String host;

    private int port;

    protected EventListener() {
        super();
    }

    protected EventListener(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EventListener other = (EventListener) obj;
        if (host == null) {
            if (other.host != null) return false;
        } else if (!host.equals(other.host)) return false;
        if (port != other.port) return false;
        return true;
    }
}
