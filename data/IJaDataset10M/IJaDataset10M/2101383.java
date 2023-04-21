package com.eaio.geoscope.whois;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class connects to a WHOIS server and requests data, given an IP
 * address.
 * 
 * @author <a href="mailto:ss@eaio.com">Sinisa Stevanovic</a>
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: WHOISLookup.java,v 1.2 2004/06/30 14:55:54 sh0gun Exp $
 */
public final class WHOISLookup {

    /**
  * No instance needed.
  */
    private WHOISLookup() {
    }

    /**
  * Establishes a connection to the WHOIS server and returns information about
  * the IP address.
  * 
  * @param server the WHOIS server (like whois.ripe.net)
  * @param ip the IP address
  * @return a String array
  * @throws IOException
  */
    public static String[] lookup(String server, String ip) throws IOException {
        Socket conn = null;
        PrintStream whoisOut = null;
        BufferedReader input = null;
        try {
            ArrayList alist = new ArrayList();
            conn = new Socket(server, 43);
            conn.setSoTimeout(5 * 1000);
            whoisOut = new PrintStream(conn.getOutputStream());
            input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            whoisOut.println(ip);
            String line = input.readLine();
            while (line != null) {
                alist.add(line);
                line = input.readLine();
            }
            return (String[]) alist.toArray(new String[alist.size()]);
        } finally {
            try {
                input.close();
            } catch (Exception ex) {
            }
            try {
                whoisOut.close();
            } catch (Exception ex) {
            }
            try {
                conn.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
  * Establishes a connection to the WHOIS server and returns information about
  * the IP address.
  * 
  * @param server the WHOIS server (like whois.ripe.net)
  * @param ip the IP address
  * @return a String array
  * @throws IOException
  */
    public static String[] lookup(String server, InetAddress ipAddress) throws IOException {
        return lookup(server, ipAddress.getHostAddress());
    }
}
