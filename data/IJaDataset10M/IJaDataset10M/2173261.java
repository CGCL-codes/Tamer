package org.zoolu.net;

/** A SocketAddress is a pair { address, port }.
  */
public class SocketAddress {

    /** The InetAddress */
    IpAddress ipaddr;

    /** The port */
    int port;

    /** Creates a SocketAddress. */
    public SocketAddress(IpAddress ipaddr, int port) {
        init(ipaddr, port);
    }

    /** Creates a SocketAddress. */
    public SocketAddress(String addr, int port) {
        init(new IpAddress(addr), port);
    }

    /** Creates a SocketAddress. */
    public SocketAddress(String soaddr) {
        String addr = null;
        int port = -1;
        int colon = soaddr.indexOf(':');
        if (colon < 0) addr = soaddr; else {
            addr = soaddr.substring(0, colon);
            try {
                port = Integer.parseInt(soaddr.substring(colon + 1));
            } catch (Exception e) {
            }
        }
        init(new IpAddress(addr), port);
    }

    /** Creates a SocketAddress. */
    public SocketAddress(SocketAddress soaddr) {
        init(soaddr.ipaddr, soaddr.port);
    }

    /** Inits the SocketAddress. */
    private void init(IpAddress ipaddr, int port) {
        this.ipaddr = ipaddr;
        this.port = port;
    }

    /** Gets the host address. */
    public IpAddress getAddress() {
        return ipaddr;
    }

    /** Gets the port. */
    public int getPort() {
        return port;
    }

    /** Makes a copy. */
    public Object clone() {
        return new SocketAddress(this);
    }

    /** Wthether it is equal to Object <i>obj</i>. */
    public boolean equals(Object obj) {
        try {
            SocketAddress saddr = (SocketAddress) obj;
            if (port != saddr.port) return false;
            if (!ipaddr.equals(saddr.ipaddr)) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Gets a String representation of the Object. */
    public String toString() {
        return (ipaddr.toString() + ":" + port);
    }
}
