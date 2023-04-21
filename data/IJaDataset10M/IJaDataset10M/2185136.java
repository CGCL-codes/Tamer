package com.tightvnc;

import java.net.*;
import java.io.*;

class HTTPConnectSocket extends Socket {

    @SuppressWarnings("deprecation")
    public HTTPConnectSocket(String host, int port, String proxyHost, int proxyPort) throws IOException {
        super(proxyHost, proxyPort);
        getOutputStream().write(("CONNECT " + host + ":" + port + " HTTP/1.0\r\n\r\n").getBytes());
        DataInputStream is = new DataInputStream(getInputStream());
        String str = is.readLine();
        if (!str.startsWith("HTTP/1.0 200 ")) {
            if (str.startsWith("HTTP/1.0 ")) str = str.substring(9);
            throw new IOException("Proxy reports \"" + str + "\"");
        }
        do {
            str = is.readLine();
        } while (str.length() != 0);
    }
}
