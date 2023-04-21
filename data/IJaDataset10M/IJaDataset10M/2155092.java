package org.binarylook.server;

import java.net.*;
import java.io.*;
import org.binarylook.server.xmpp.*;

public class Light implements Runnable {

    public Socket JIDThread;

    private BufferedReader br;

    private PrintWriter pw;

    public Light() {
        XMPPStream beanStream = new XMPPStream();
    }

    public void ListenLight(Socket s) {
        this.JIDThread = s;
        Thread lighThread = new Thread(this, "light");
        lighThread.start();
    }

    public void run() {
        try {
            Socket xmppclient = this.JIDThread;
            InputStream in = xmppclient.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            OutputStream os = xmppclient.getOutputStream();
            pw = new PrintWriter(os, true);
            while (true) {
                System.out.println("waiting for message");
                String s = br.readLine();
                if (s != null) {
                    System.out.println(s);
                    if (s.indexOf("stream") != -1) {
                        pw.println("<?xml version='1.0'?><stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' id='" + SessionID.GetSessionID() + "' from='localhost' version='1.0' xml:lang='en'>");
                        pw.println("<stream:features><starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'><required/></starttls><mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><mechanism>DIGEST-MD5</mechanism><mechanism>PLAIN</mechanism></mechanisms></stream:features>");
                    } else if (s.indexOf("<starttls") == 0) {
                        pw.println("<proceed xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>");
                    } else {
                        System.out.println("Else part" + s.indexOf("<starttls") + "--" + s);
                    }
                } else {
                }
            }
        } catch (Exception e) {
            System.out.println("Bug receive message" + e.getMessage());
        }
    }
}
