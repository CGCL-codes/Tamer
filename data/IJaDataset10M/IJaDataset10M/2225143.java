package org.openremote.controller.protocol.knx.ip;

/**
 * Listener of a <code>IpTunnelClient</code> instance.
 * @see IpTunnelClient
 */
public interface IpTunnelClientListener {

    /**
    * Receive a cEMI telegram from a remote KNX device.
    * @param cemiFrame An array of bytes with incoming telegram.
    */
    void receive(byte[] cemiFrame);

    /**
    * Notify KNX-IP interface new status.
    * @param status Interface status.
    */
    void notifyInterfaceStatus(Status status);

    static enum Status {

        connected, disconnected
    }
}
