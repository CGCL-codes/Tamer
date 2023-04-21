package com.pentagaia.tb.net.impl;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import com.pentagaia.tb.net.api.INetClientInfo;

/**
 * Implementation of client information
 * 
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 */
public class NetClientInfo implements INetClientInfo {

    /** serial version uid */
    private static final long serialVersionUID = 1L;

    /** The host name */
    private String hostName;

    /** The port number */
    private int port;

    /**
     * Constructor
     * 
     * @param address
     */
    public NetClientInfo(final SocketAddress address) {
        if (address instanceof InetSocketAddress) {
            final InetSocketAddress inetAddress = (InetSocketAddress) address;
            this.hostName = inetAddress.getHostName();
            this.port = inetAddress.getPort();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetClientInfo#getHostName()
     */
    public String getHostName() {
        return this.hostName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetClientInfo#getPort()
     */
    public int getPort() {
        return this.port;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (obj instanceof NetClientInfo) {
            final INetClientInfo other = (INetClientInfo) obj;
            final String otherHost = other.getHostName();
            final int otherPort = other.getPort();
            result = (this.hostName == null && otherHost == null) || (this.hostName != null && this.hostName.equals(otherHost));
            result &= this.port == otherPort;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.hostName == null ? this.port : this.hostName.hashCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Client @ " + (this.hostName == null ? "?" : this.hostName) + ":" + this.port;
    }
}
