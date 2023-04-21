package org.jma.lib.utils.networking;

import java.io.*;
import java.net.*;

public class Ping {

    private long start;

    private long end;

    private void start() {
        start = System.currentTimeMillis();
    }

    private void end() {
        end = System.currentTimeMillis();
    }

    public long getDuration() {
        return (end - start);
    }

    private void reset() {
        start = 0;
        end = 0;
    }

    public void doPing(String server) {
        try {
            int timeOut = 5000;
            reset();
            start();
            boolean status = InetAddress.getByName(server).isReachable(timeOut);
            end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
