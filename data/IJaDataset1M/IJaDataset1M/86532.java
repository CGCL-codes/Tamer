package org.openremote.controller.protocol.bus;

import java.net.InetSocketAddress;

/**
 * A message used by <code>DatagramSocketPhysicalBus</code>.
 * 
 * @see DatagramSocketPhysicalBus
 * @see Message
 */
public class DatagramSocketMessage extends Message {

    private InetSocketAddress destAddr;

    /**
    * Constructor.
    * 
    * @param destAddr
    *           Destination address.
    * @param content
    *           Message content.
    */
    public DatagramSocketMessage(InetSocketAddress destAddr, byte[] content) {
        super(content);
        this.destAddr = destAddr;
    }

    /**
    * Get message destination address
    * 
    * @return Destination address as a <code>InetSocketAddress</code>.
    */
    public InetSocketAddress getDestAddr() {
        return this.destAddr;
    }
}
