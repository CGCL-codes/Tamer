package cxtable.peer;

import cxtable.core_comm.*;
import cxtable.plugin.*;

public class xMultiBroadcast implements xOutListen {

    private xRegistry servers;

    public xMultiBroadcast(xRegistry x) {
        servers = x;
    }

    public void send(String s) {
        xClientConn[] xx = servers.on();
        new Thread(new xMultiBroadcastWorker(xx, s)).start();
    }
}

class xMultiBroadcastWorker extends Thread {

    private xClientConn[] xcc;

    private String mssg;

    xMultiBroadcastWorker(xClientConn[] x, String s) {
        mssg = "<MSSG>" + s + "</MSSG>";
        xcc = x;
    }

    public void run() {
        for (int i = 0; i < xcc.length; i++) {
            try {
                xcc[i].send(mssg);
            } catch (Exception e) {
            }
        }
    }
}
