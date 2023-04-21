package uk.co.westhawk.snmp.net;

import java.io.*;

/**
 * This is a holder class that associates the incoming packet stream
 * with the remote port it came from.
 *
 * @since 4_14
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.4 $ $Date: 2006/02/09 14:14:50 $
 */
public class StreamPortItem {

    static final String version_id = "@(#)$Id: StreamPortItem.java,v 1.4 2006/02/09 14:14:50 birgit Exp $ Copyright Westhawk Ltd";

    private String hostAddress;

    private int port;

    private ByteArrayInputStream stream;

    /**
 * Constructor.
 *
 * @param address The host address
 * @param newPort The remote port number 
 * @param in      The incoming message
 */
    public StreamPortItem(String address, int newPort, ByteArrayInputStream in) {
        hostAddress = address;
        port = newPort;
        stream = in;
    }

    /**
 * Returns the host addres where the message came from.
 *
 * @return The host address
 */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
 * Returns the remote port where the message came from.
 *
 * @return The remote port number
 */
    public int getHostPort() {
        return port;
    }

    /**
 * Returns incoming message (or a copy of it).
 *
 * @return The message
 */
    public ByteArrayInputStream getStream() {
        return stream;
    }

    /**
 * Returns the string representation.
 *
 * @return The string
 */
    public String toString() {
        StringBuffer buffer = new StringBuffer(getClass().getName());
        buffer.append("[");
        buffer.append("hostAddress=").append(hostAddress);
        buffer.append(", hostPort=").append(port);
        buffer.append(", #bytes=").append(stream.available());
        buffer.append("]");
        return buffer.toString();
    }
}
