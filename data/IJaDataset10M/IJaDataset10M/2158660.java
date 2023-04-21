package org.openymsg.network;

import java.util.Properties;

/**
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 * @author S.E. Morris
 */
public class SOCKSConnectionHandler extends DirectConnectionHandler {

    private String socksHost;

    private int socksPort;

    /**
     * CONSTRUCTOR Reads the SOCKS setting from Java properties.
     */
    public SOCKSConnectionHandler() throws IllegalArgumentException {
        socksHost = System.getProperty(NetworkConstants.SOCKS_HOST, "");
        socksPort = Integer.parseInt(System.getProperty(NetworkConstants.SOCKS_PORT, "-1"));
        if (socksHost.length() <= 0 || socksPort <= 0) throw new IllegalArgumentException("Bad SOCKS proxy properties: " + socksHost + ":" + socksPort);
        System.getProperties().put(NetworkConstants.SOCKS_SET, "true");
    }

    /**
     * CONSTRUCTOR Sets specific SOCKS server/port. Note: these settings will be global to all Socket's across the JVM.
     */
    public SOCKSConnectionHandler(String h, int p) {
        socksHost = h;
        socksPort = p;
        Properties pr = System.getProperties();
        pr.put(NetworkConstants.SOCKS_HOST, socksHost);
        pr.put(NetworkConstants.SOCKS_PORT, socksPort + "");
        pr.put(NetworkConstants.SOCKS_SET, "true");
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("SOCKS connection: ").append(socksHost).append(":").append(socksPort);
        return sb.toString();
    }
}
