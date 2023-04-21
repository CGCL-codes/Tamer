package TwoClientGameConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A small library file used for getting a host address.
 * @author Jace Ferguson
 * @filename TCGGeneralLib
 */
public class TCGGeneralLib {

    public static String getHostIPAddress() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    public static String getHostIPAddress(InetAddress host) {
        return host.getHostAddress();
    }
}
