package com.elibera.gateway.elements;

import com.elibera.gateway.app.MLERedirectProtocol;

/**
 * @author meisi
 *
 */
public class TimeOutCheck extends Thread {

    private long time = 30000;

    public boolean aktiv = true;

    public RedirectClient server;

    public synchronized void resetTime(long t) {
        if (t < 0) time = 30000; else time = t;
    }

    public void run() {
        try {
            while (time > 0) {
                long t = time;
                time = 0;
                if (t > 0) Thread.sleep(t);
            }
            if (aktiv) {
                MLERedirectProtocol.quiteServerThread(server);
            }
        } catch (Exception e) {
        }
    }
}
