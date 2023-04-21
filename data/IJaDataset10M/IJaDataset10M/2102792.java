package org.apache.harmony.luni.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * This class provides is used to pass the information required in an ip_mreq or
 * ip6_mreq structure to java natives. We don't have accessor methods as it is
 * more straight forward in the natives to simply access the fields directly
 */
final class GenericIPMreq {

    private InetAddress multiaddr;

    @SuppressWarnings("unused")
    private InetAddress interfaceAddr;

    @SuppressWarnings("unused")
    private boolean isIPV6Address;

    @SuppressWarnings("unused")
    private int interfaceIdx;

    /**
     * This constructor is used to create an instance of the object
     * 
     * @param addr multicast address to join/leave
     * 
     */
    GenericIPMreq(InetAddress addr) {
        multiaddr = addr;
        interfaceAddr = null;
        interfaceIdx = 0;
        init();
    }

    /**
     * This constructor is used to create an instance of the object
     * 
     * @param addr multicast address to join/leave
     * @param netInterface the NetworkInterface object identifying the interface
     *        on which to join/leave
     */
    GenericIPMreq(InetAddress addr, NetworkInterface netInterface) {
        multiaddr = addr;
        if (null != netInterface) {
            interfaceAddr = null;
            Enumeration<InetAddress> theAddresses = netInterface.getInetAddresses();
            if ((addr instanceof Inet4Address) && (theAddresses != null)) {
                boolean found = false;
                while ((theAddresses.hasMoreElements()) && (found != true)) {
                    InetAddress theAddress = theAddresses.nextElement();
                    if (theAddress instanceof Inet4Address) {
                        interfaceAddr = theAddress;
                        found = true;
                    }
                }
            }
        } else {
            interfaceIdx = 0;
            interfaceAddr = null;
        }
        init();
    }

    /**
     * This method does any required initialization for the constructors
     */
    private void init() {
        isIPV6Address = ((multiaddr != null) && (multiaddr instanceof Inet6Address));
    }
}
