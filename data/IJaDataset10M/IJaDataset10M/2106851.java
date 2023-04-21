package org.zoolu.sip.provider;

import org.zoolu.net.IpAddress;

/** ConnectionIdentifier is the reference for active transport connections.
  */
public class ConnectionIdentifier extends Identifier {

    /** Costructs a new ConnectionIdentifier. */
    public ConnectionIdentifier(String protocol, IpAddress remote_ipaddr, int remote_port) {
        super(getId(protocol, remote_ipaddr, remote_port));
    }

    /** Costructs a new ConnectionIdentifier. */
    public ConnectionIdentifier(ConnectionIdentifier conn_id) {
        super(conn_id);
    }

    /** Costructs a new ConnectionIdentifier. */
    public ConnectionIdentifier(String id) {
        super(id);
    }

    /** Costructs a new ConnectionIdentifier. */
    public ConnectionIdentifier(ConnectedTransport conn) {
        super(getId(conn.getProtocol(), conn.getRemoteAddress(), conn.getRemotePort()));
    }

    /** Gets the id. */
    private static String getId(String protocol, IpAddress remote_ipaddr, int remote_port) {
        return protocol + ":" + remote_ipaddr + ":" + remote_port;
    }
}
