package be.vanvlerken.bert.packetdistributor.common;

import java.net.InetAddress;

/**
 * Describes a Traffic Endpoint
 */
public interface ITrafficEndpoint {

    public static final int UNKNOWN = -1;

    public static final int UDP = 0;

    public static final int TCP = 1;

    public abstract InetAddress getIpAddress();

    public abstract int getPort();

    public abstract int getProtocol();
}
