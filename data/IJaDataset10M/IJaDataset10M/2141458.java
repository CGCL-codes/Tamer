package org.gzigzag.module;

import org.gzigzag.ztp.*;
import org.gzigzag.ztp.server.*;
import org.gzigzag.*;
import java.net.*;
import java.io.*;
import java.util.*;

/** This is a prototype STP server.  Currently it supports a
 * mock-STP/TCP in order to test the ideas in the WIP spec. */
public class ZTPserver {

    public static boolean dbg = false;

    private static void p(String s) {
        if (dbg) ZZLogger.log("[" + s + "]");
    }

    private static void pa(String s) {
        ZZLogger.log(s);
    }

    protected Listener listener;

    public ZTPserver(ZZSpace space, Integer inetport) {
        this(space, inetport.intValue());
    }

    public ZTPserver(ZZSpace space, int inetport) {
        p("ZTP creation start");
        ServerSocket sock;
        try {
            sock = new ServerSocket(inetport);
        } catch (IOException e) {
            throw new ZZError("Unable to bind to server port " + inetport + ": " + e);
        }
        listener = new Listener(space, sock);
        listener.start();
        p("ZTP creation finished");
    }

    public void kill() throws InterruptedException {
        listener.interrupt();
        listener.join();
        for (Enumeration e = Session.getActiveSessions(); e.hasMoreElements(); ) {
            Session s = (Session) e.nextElement();
            s.interrupt();
        }
        for (Enumeration e = Session.getActiveSessions(); e.hasMoreElements(); ) {
            Session s = (Session) e.nextElement();
            s.join();
        }
    }

    public static void main(String[] args) {
        ZTPserver serv = new ZTPserver(new ZZDimSpace(), 5555);
        try {
            serv.listener.join();
            serv.kill();
        } catch (InterruptedException e) {
            ZZLogger.exc(e);
        }
        SafeExit.exit(0);
    }
}
