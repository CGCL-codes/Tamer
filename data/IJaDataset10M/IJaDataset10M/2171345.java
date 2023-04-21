package edu.cmu.ece.agora.kernel.router;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Util {

    public static InetAddress getGlobalAddress() throws SocketException {
        InetAddress result = null;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        outer: while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                String dotted = addr.getHostAddress();
                if (!(addr instanceof Inet6Address || dotted.startsWith("169.") || dotted.startsWith("127.") || dotted.startsWith("10.") || dotted.startsWith("192.168.") || dotted.startsWith("255.") || dotted.startsWith("0."))) {
                    result = addr;
                    break outer;
                }
            }
        }
        return result;
    }

    public static InetAddress getSiteAddress() throws SocketException {
        InetAddress result = null;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        outer: while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                String dotted = addr.getHostAddress();
                if (!(addr instanceof Inet6Address) && (dotted.startsWith("10.") || dotted.startsWith("192.168."))) {
                    result = addr;
                    break outer;
                }
            }
        }
        return result;
    }
}
