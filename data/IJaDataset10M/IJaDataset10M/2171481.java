package ca.qc.adinfo.rouge.util;

import java.net.InetAddress;

public class NetUtils {

    public static String getHostname() {
        try {
            InetAddress localMachine = InetAddress.getLocalHost();
            return localMachine.getHostName();
        } catch (java.net.UnknownHostException uhe) {
            return "Unknown Hostname";
        }
    }
}
