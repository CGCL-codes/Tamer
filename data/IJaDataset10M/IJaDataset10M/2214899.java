package org.zoolu.net;

import java.net.InetAddress;

/** IpAddress is an IP address.
  */
public class IpAddress {

    /** The host address/name */
    String address;

    /** The InetAddress */
    InetAddress inet_address;

    /** Creates an IpAddress */
    IpAddress(InetAddress iaddress) {
        init(null, iaddress);
    }

    /** Inits the IpAddress */
    private void init(String address, InetAddress iaddress) {
        this.address = address;
        this.inet_address = iaddress;
    }

    /** Gets the InetAddress */
    InetAddress getInetAddress() {
        if (inet_address == null) try {
            inet_address = InetAddress.getByName(address);
        } catch (java.net.UnknownHostException e) {
        }
        return inet_address;
    }

    /** Creates an IpAddress */
    public IpAddress(String address) {
        init(address, null);
    }

    /** Creates an IpAddress */
    public IpAddress(IpAddress ipaddr) {
        init(ipaddr.address, ipaddr.inet_address);
    }

    /** Makes a copy */
    public Object clone() {
        return new IpAddress(this);
    }

    /** Wthether it is equal to Object <i>obj</i> */
    public boolean equals(Object obj) {
        try {
            IpAddress ipaddr = (IpAddress) obj;
            if (!toString().equals(ipaddr.toString())) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Gets a String representation of the Object */
    public String toString() {
        if (address == null && inet_address != null) address = inet_address.getHostAddress();
        return address;
    }

    /** Gets the IpAddress for a given fully-qualified host name. */
    public static IpAddress getByName(String host_addr) throws java.net.UnknownHostException {
        InetAddress iaddr = InetAddress.getByName(host_addr);
        return new IpAddress(iaddr);
    }

    /** Detects the default IP address of this host. */
    public static IpAddress getLocalHostAddress() {
        try {
            return new IpAddress(java.net.InetAddress.getLocalHost());
        } catch (java.net.UnknownHostException e) {
            return new IpAddress("127.0.0.1");
        }
    }
}
