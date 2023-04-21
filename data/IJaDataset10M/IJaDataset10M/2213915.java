package com.aplpi.wapreview.wricproxy;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.Frame;
import java.awt.Component;

/**
 * 
 * see http://wapreview.sourceforge.net
 *
 * Copyright (C) 2000 Robert Fuller, Applepie Solutions Ltd. 
 *                    <robert.fuller@applepiesolutions.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *

 * The wapreview image converting proxy server.
 * This application allows the wapreview WML browser
 * to run locally.
 *
 */
public class wricproxy {

    private static wricproxy wrp;

    public static Frame graphicsContext;

    public String test() {
        return "tested sucessfully!!\n";
    }

    public static void main(String[] args) {
        int port = 5000;
        graphicsContext = new Frame("WAP image converter by applepiesolutions.com");
        graphicsContext.setSize(0, 0);
        graphicsContext.setMenuBar(null);
        graphicsContext.show();
        graphicsContext.toBack();
        wrp = new wricproxy();
        wrp.serve(port);
    }

    public void serve(int thePort) {
        wrhttp.prepare();
        ServerSocket ss;
        try {
            ss = new ServerSocket(thePort);
            ss.getLocalPort();
            System.err.println("aplpi wricproxy server " + wrhttp.here + ":" + thePort);
            System.err.println("--accepting connections from " + wrhttp.here + " only!");
            System.err.println("point your web browser to:");
            System.err.println("http://" + wrhttp.here + ":" + thePort);
            while (true) {
                wrhttp j = new wrhttp(ss.accept());
                j.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + thePort + "!");
            System.exit(-1);
        }
    }
}

class wrhttp extends Thread {

    public static String server = "aplpi.com wricproxy";

    public static String here;

    public static final String localhost = "127.0.0.1";

    private Frame graphicsContext;

    public static void prepare() {
        try {
            here = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (java.net.UnknownHostException e) {
            System.err.println("Couldn't get localhost...");
            here = null;
        }
    }

    Socket theConnection;

    public wrhttp(Socket s) {
        String from = s.getInetAddress().getHostAddress();
        if (from.equals(localhost) || (here != null && from.equals(here))) {
            theConnection = s;
        } else {
            System.err.println("refusing connection from " + from);
            try {
                s.close();
            } catch (java.io.IOException e) {
                System.err.println(e.toString());
            }
            theConnection = null;
        }
    }

    public void run() {
        if (theConnection == null) {
            return;
        }
        String method;
        String ct;
        String version = "";
        String referer = null;
        String accept = "text/vnd.wap.wml, image/vnd.wap.wbmp, */*";
        String user_agent = "Nokia7110/1.0 (04.76) aplpi.com v0.5j";
        boolean urlIsFile = false;
        boolean convertWBMP = false;
        File theFile;
        PrintStream os = null;
        try {
            os = new PrintStream(theConnection.getOutputStream());
            InputStream inputstream = theConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputstream);
            BufferedReader is = new BufferedReader(isr);
            String get = null;
            for (int i = 1; i < 1000 && (get = is.readLine()) == null; i++) ;
            if (get == null) {
                System.err.println("no input...");
                os.close();
                theConnection.close();
                return;
            }
            System.err.println("get:" + get);
            StringTokenizer st = new StringTokenizer(get);
            method = st.nextToken();
            if (!(method.equals("GET") || method.equals("POST"))) {
                return;
            }
            String file = st.nextToken();
            if (file.equals("/")) {
                file = "/index.html";
            }
            if (file.toLowerCase().endsWith(".wbmp&.gif")) {
                file = file.substring(0, file.length() - 5);
                convertWBMP = true;
            }
            if (st.hasMoreTokens()) {
                version = st.nextToken();
            }
            while ((get = is.readLine()) != null) {
                if (get.toLowerCase().startsWith("referer: ")) {
                    referer = get.substring(9, get.length());
                }
                if (get.trim().equals("")) break;
            }
            URL url = null;
            if (file.toLowerCase().startsWith("/http:")) {
                url = new URL(file.substring(1, file.length()));
            } else {
                File f;
                if (file.toLowerCase().startsWith("/file://")) {
                    url = new URL(file.substring(1, file.length()));
                    f = new File(url.getFile());
                    url = new URL("file", "", f.toString());
                } else {
                    f = new File(URLDecode(file.substring(1, file.length())));
                    String p = f.getAbsolutePath();
                    boolean java2 = false;
                    if (java2) {
                        url = f.toURL();
                    } else {
                        p = p.replace(File.separatorChar, '/');
                        while (!p.startsWith("//")) {
                            p = "/" + p;
                        }
                        if (!p.endsWith("/") && f.isDirectory()) {
                            p = p + "/";
                        }
                        url = new URL("file", "", p);
                        f = new File(url.getFile());
                    }
                }
                if (f.canRead()) {
                    urlIsFile = true;
                    os.print("HTTP/1.0 200 OK\n");
                    Date now = new Date();
                    os.print("Date: " + now + "\n");
                    os.print("Server: " + server + "\n");
                    os.print("Content-length: " + f.length() + "\n");
                    os.print("Connection: close\n");
                    os.print("Content-type: " + guessContentType(f.toString()) + "\n\n");
                } else if (f.exists()) {
                    os.print("HTTP/1.0 200 OK\n");
                    Date now = new Date();
                    os.print("Date: " + now + "\n");
                    os.print("Server: " + server + "\n");
                    os.print("Connection: close\n");
                    os.print("Content-type: " + guessContentType(".wml") + "\n\n");
                    os.println("<wml><card>><title>Error!</title><p>Forbidden!</p></card></wml>");
                    os.close();
                    theConnection.close();
                    return;
                } else {
                    os.print("HTTP/1.0 200 OK\n");
                    Date now = new Date();
                    os.print("Date: " + now + "\n");
                    os.print("Connection: close\n");
                    os.print("Server: " + server + "\n");
                    os.print("Content-type: " + guessContentType(".wml") + "\n\n");
                    os.println("<wml><card><title>Error!</title><p>File not found!</p></card></wml>");
                    os.close();
                    theConnection.close();
                    return;
                }
            }
            URLConnection connection = url.openConnection();
            if (connection != null) {
                connection.setRequestProperty("Accept", accept);
                connection.setRequestProperty("User-Agent", user_agent);
                if (referer != null) {
                    connection.setRequestProperty("Referer", referer);
                }
                if (method.equals("POST")) {
                    connection.setDoOutput(true);
                    OutputStream cs = connection.getOutputStream();
                    while (is.ready()) {
                        cs.write(is.read());
                    }
                    cs.close();
                }
                connection.connect();
                if (!urlIsFile) {
                    os.print("HTTP/1.0 200 OK\n");
                    os.print("Connection: close\n");
                    boolean headers = true;
                    for (int i = 1; true; i++) {
                        String key = connection.getHeaderFieldKey(i);
                        if (key == null) break;
                        headers = true;
                        String value = connection.getHeaderField(i);
                        os.println(key + ": " + value);
                    }
                    if (headers) os.print("\n");
                }
                String txtLine;
                InputStream in = connection.getInputStream();
                if (convertWBMP) {
                    WBMPimg wbmp = new WBMPimg();
                    wbmp.wbmp2gif(wricproxy.graphicsContext, in, os);
                    os.close();
                    theConnection.close();
                    return;
                } else {
                    int r;
                    while ((r = in.read()) >= 0) {
                        os.write(r);
                    }
                    os.close();
                }
            } else {
            }
            os.close();
            return;
        } catch (IOException e) {
            System.err.println(e);
            try {
                os.print("HTTP/1.0 200 OK\n");
                Date now = new Date();
                os.print("Date: " + now + "\n");
                os.print("Connection: close\n");
                os.print("Server: " + server + "\n");
                os.print("Content-type: " + guessContentType(".wml") + "\n\n");
                os.println("<wml><card><title>Error!</title><p>" + e.toString() + "</p></card></wml>");
                os.close();
                theConnection.close();
            } catch (Exception xe) {
            }
        }
    }

    public String guessContentType(String fname) {
        String name = fname.toLowerCase();
        if (name.endsWith(".wml")) return "text/vnd.wap.wml";
        if (name.endsWith(".wmls")) return "text/vnd.wap.wmlscript";
        if (name.endsWith(".wmlc")) return "application/vnd.wap.wmlc";
        if (name.endsWith(".wmlsc")) return "application/vnd.wap.wmlscriptc";
        if (name.endsWith(".wbmp")) return "image/vnd.wap.wbmp";
        if (name.endsWith(".html")) return "text/html";
        if (name.endsWith(".htm")) return "text/html";
        if (name.endsWith(".txt")) return "text/plain";
        if (name.endsWith(".htm")) return "text/html";
        if (name.endsWith(".gif")) return "image/gif";
        if (name.endsWith(".class")) return "application/octet-stream";
        if (name.endsWith(".jar")) return "application/octet-stream";
        if (name.endsWith(".jpg")) return "image/jpeg";
        if (name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".js")) return "application/x-javascript";
        if (name.endsWith(".doc")) return "application/msword";
        return "text/plain";
    }

    /** this method would not be required if java 1.2 classes were available */
    public static final String URLDecode(String str) {
        if (str == null) return null;
        char[] res = new char[str.length()];
        int didx = 0;
        for (int sidx = 0; sidx < str.length(); sidx++) {
            char ch = str.charAt(sidx);
            if (ch == '+') res[didx++] = ' '; else if (ch == '%') {
                try {
                    res[didx++] = (char) Integer.parseInt(str.substring(sidx + 1, sidx + 3), 16);
                    sidx += 2;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(str.substring(sidx, sidx + 3) + " is an invalid code");
                }
            } else res[didx++] = ch;
        }
        return String.valueOf(res, 0, didx);
        ;
    }
}
