package ch.rgw.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author Gerry
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NetTool {

    static final String Version = "1.0.0";

    public static java.util.ArrayList IPs = new java.util.ArrayList();

    public static String hostname;

    static {
        Enumeration nis = null;
        ;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) ias.nextElement();
                    String ip = ia.getHostAddress();
                    hostname = ia.getHostName();
                    IPs.add(ip);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
