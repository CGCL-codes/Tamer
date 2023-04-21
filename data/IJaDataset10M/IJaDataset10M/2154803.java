package edu.holycross.security.oraoink;

import java.io.*;
import java.net.*;
import java.io.IOException;
import org.apache.commons.net.telnet.TelnetClient;

/**
 * This is class connects to a Cisco Router and runs a 'show arp' looking
 * for a given IP Address's MAC Address.  It then returns the mac-address.
 * This has been tested on a router blade for the catalyst 6500 series.
 */
public class MacAddressFinder {

    private String ipAddress;

    private String[] send;

    private String host;

    private int port;

    private String password;

    private String routerPrompt;

    /**
     * Sets the ip address if the default constructor is used
     */
    public void setIpAddress(String ipAddress) {
        ipAddress = this.ipAddress;
        String[] thissend = { "password", "en", "password", "show arp | include " + ipAddress };
        send = thissend;
    }

    /**
     * Returns the String of the IP Address 
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Method to set the host address of the router. 
     */
    public void setHost(String host) {
        host = this.host;
    }

    /**
     * Method to get the host address of the router.
     */
    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        port = this.port;
    }

    public int getPort() {
        return port;
    }

    public MacAddressFinder(String ipaddr, String connhost, int connport, String pass, String prompt) {
        host = connhost;
        port = connport;
        password = pass;
        routerPrompt = prompt;
        ipAddress = ipaddr.substring(1);
        String[] thissend = { pass, "show arp | include " + ipAddress };
        send = thissend;
    }

    public MacAddressFinder() {
    }

    public String getMacAddress() {
        String[] match = { "assword:", routerPrompt, "ARPA" };
        TelnetClient telnet;
        telnet = new TelnetClient();
        try {
            telnet.connect(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(telnet.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(telnet.getOutputStream()));
        int inline;
        boolean justLogged = false;
        StringBuffer x = new StringBuffer();
        int count = 0;
        String stringNeeded = "";
        try {
            while ((inline = in.read()) > -1) {
                if (count > 5) {
                    break;
                }
                char i = (char) inline;
                x.append(i);
                int stringLength = x.length();
                int sizeOfSearch = match[count].length();
                int subtract = stringLength - sizeOfSearch;
                if (stringLength > 8) {
                    String newString = x.substring(subtract);
                    if (newString.equals(match[count])) {
                        if (match[count].equals("ARPA")) {
                            int backstart = 20;
                            int backend = 6;
                            stringNeeded = x.substring(stringLength - backstart, stringLength - backend);
                            break;
                        }
                        out.write(send[count] + "\r\n");
                        out.flush();
                        count++;
                    } else if (match[count].equals("ARPA")) {
                        String thistestString = x.substring(x.length() - 5);
                        if (thistestString.equals("MSFC3")) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.exit(1);
        }
        try {
            telnet.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        if (stringNeeded.length() < 1) {
            return "";
        } else {
            return stringNeeded;
        }
    }
}
