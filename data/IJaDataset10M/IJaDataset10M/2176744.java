package com.billing.vncviewer;

import java.applet.Applet;
import java.io.IOException;
import java.net.Socket;

public class HTTPConnectSocketFactory implements SocketFactory {

    public Socket createSocket(String host, int port, Applet applet) throws IOException {
        return createSocket(host, port, applet.getParameter("PROXYHOST1"), applet.getParameter("PROXYPORT1"));
    }

    public Socket createSocket(String host, int port, String[] args) throws IOException {
        return createSocket(host, port, readArg(args, "PROXYHOST1"), readArg(args, "PROXYPORT1"));
    }

    public Socket createSocket(String host, int port, String proxyHost, String proxyPortStr) throws IOException {
        int proxyPort = 0;
        if (proxyPortStr != null) {
            try {
                proxyPort = Integer.parseInt(proxyPortStr);
            } catch (NumberFormatException e) {
            }
        }
        if (proxyHost == null || proxyPort == 0) {
            System.out.println("Incomplete parameter list for HTTPConnectSocket");
            return new Socket(host, port);
        }
        System.out.println("HTTP CONNECT via proxy " + proxyHost + " port " + proxyPort);
        HTTPConnectSocket s = new HTTPConnectSocket(host, port, proxyHost, proxyPort);
        return (Socket) s;
    }

    private String readArg(String[] args, String name) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equalsIgnoreCase(name)) {
                try {
                    return args[i + 1];
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
}
