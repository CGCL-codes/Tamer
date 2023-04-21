package saadadb.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/** * @version $Id: HostAddress.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 * @author laurent
 * 06/2011: IP V6 address filtering
 */
public class HostAddress {

    public static String getCanonicalHostname() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                Enumeration<InetAddress> e2 = ((NetworkInterface) e.nextElement()).getInetAddresses();
                while (e2.hasMoreElements()) {
                    InetAddress ip = (InetAddress) e2.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.isLinkLocalAddress()) {
                        String retour = ip.getCanonicalHostName();
                        if (retour.indexOf(":") == -1) {
                            return retour;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return "localhost";
    }

    public static void main(String[] args) {
        System.out.println(HostAddress.getCanonicalHostname());
    }
}
