package TranslationOperations;

import java.util.StringTokenizer;

/**
 *
 * @author gracep
 */
public class getIPAddress {

    public getIPAddress() {
    }

    public String getHostIP(String s1) {
        return s1.substring(0, s1.length() - 5);
    }

    public byte[] getIPBytes(String s1) {
        byte[] ip = new byte[4];
        int tmp = s1.indexOf("//");
        String ipString = s1.substring(tmp + 2);
        int port = ipString.indexOf(":");
        int dash = ipString.indexOf("/");
        if (port > 0) {
            ipString = ipString.substring(0, port);
        } else {
            ipString = ipString.substring(0, dash);
        }
        StringTokenizer st = new StringTokenizer(ipString, ".");
        int i = 0;
        while (st.hasMoreTokens()) {
            ip[i++] = (byte) Integer.parseInt(st.nextToken());
        }
        return ip;
    }
}
