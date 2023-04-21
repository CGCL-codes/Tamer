package org.bing.engine.utility.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkHelper {

    /**
     * Returns the IP of the getaway from netstat
     * 
     * @return A string representing the IP
     */
    public static String getGateway() {
        String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        String exIP = "(?:" + _255 + "\\.){3}" + _255;
        Pattern pat = Pattern.compile("^\\s*(?:0\\.0\\.0\\.0\\s*){1,2}(" + exIP + ").*");
        Process proc;
        try {
            proc = Runtime.getRuntime().exec("netstat -rn");
            InputStream inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                Matcher m = pat.matcher(line);
                if (m.matches()) {
                    return m.group(1);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws SocketException {
        System.out.println(NetworkHelper.getGateway());
        System.out.println("-----------------------------------");
        Enumeration<NetworkInterface> ins = NetworkInterface.getNetworkInterfaces();
        while (ins.hasMoreElements()) {
            NetworkInterface in = ins.nextElement();
            System.out.println(">>> " + in);
            System.out.println("-----------------------------------");
        }
    }
}
