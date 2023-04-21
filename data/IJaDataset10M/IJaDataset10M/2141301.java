package org.livetribe.net;

import java.net.InetAddress;

/**
 * @version $Revision: 1.1 $ $Date: 2005/01/25 21:06:24 $
 */
public final class ICMPEchoRequestPacket extends ICMPPacket {

    /**
     * Constructs a datagram packet for sending packets of length
     * <code>length</code> with offset <code>ioffset</code>to the
     * specified port number on the specified host. The
     * <code>length</code> argument must be less than or equal to
     * <code>buf.length</code>.
     *
     * @param address the destination address.
     * @see java.net.InetAddress
     */
    public ICMPEchoRequestPacket(InetAddress address) {
        super((byte) 0, (byte) 0, address);
    }
}
