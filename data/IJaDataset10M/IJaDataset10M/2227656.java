package com.google.code.sagetvaddons.sjq.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.apache.log4j.Logger;

/**
 * @author dbattams
 *
 */
public final class Listener {

    private String cmdPkg;

    private int port;

    private ServerSocket srvSocket;

    private String logPkg;

    private Logger log;

    public Listener(String cmdPkg, int port, String logPkg) {
        this.cmdPkg = cmdPkg;
        this.port = port;
        this.logPkg = logPkg;
        log = Logger.getLogger(logPkg + "." + Listener.class.getSimpleName());
    }

    public void init() throws IOException {
        srvSocket = new ServerSocket(port);
        srvSocket.setSoTimeout(5000);
        while (true) {
            try {
                Socket s = srvSocket.accept();
                log.info(String.format("Received connection from: %s:%d", s.getInetAddress(), s.getPort()));
                Thread t = new Thread(new Handler(s, cmdPkg, logPkg));
                t.setDaemon(true);
                t.start();
            } catch (SocketTimeoutException e) {
            }
            if (Thread.interrupted()) {
                srvSocket.close();
                log.warn("Shutting down listener...");
                break;
            }
        }
    }
}
