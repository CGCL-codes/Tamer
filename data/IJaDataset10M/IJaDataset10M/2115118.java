package org.avis.tools;

import java.util.Enumeration;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Utility to dump network addresses to console for debugging.
 * 
 * @author Matthew Phillips
 */
public class DumpHostAddresses {

    public static void main(String[] args) throws IOException {
        System.out.println("local host name: " + InetAddress.getLocalHost());
        for (Enumeration<NetworkInterface> i = NetworkInterface.getNetworkInterfaces(); i.hasMoreElements(); ) {
            NetworkInterface ni = i.nextElement();
            for (Enumeration<InetAddress> j = ni.getInetAddresses(); j.hasMoreElements(); ) {
                InetAddress address = j.nextElement();
                System.out.println("-------");
                System.out.println("host name: " + address.getCanonicalHostName());
                System.out.println("loopback: " + address.isLoopbackAddress());
                System.out.println("link local: " + address.isLinkLocalAddress());
                System.out.println("multicast: " + address.isMulticastAddress());
                System.out.println("site local: " + address.isSiteLocalAddress());
            }
        }
    }
}
