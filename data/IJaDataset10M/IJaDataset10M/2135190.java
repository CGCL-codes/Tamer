package ecks.Threads;

import ecks.Logging;
import ecks.Utility.Client;
import ecks.Utility.RBLChecker;
import ecks.protocols.Generic;
import ecks.services.Service;
import ecks.util;

public class CheckClientThread implements Runnable {

    Client who;

    Service parent;

    public CheckClientThread(Client which, Service daddy) {
        who = which;
        parent = daddy;
    }

    public void run() {
        Logging.info("CLIENTCHK", "Checking incoming client...");
        if (RBLChecker.checkRelay(who.host)) {
            Logging.warn("CLIENTCHK", who.uid + " is listed in DNSBL!");
            Generic.curProtocol.outGLINE(parent, who, "Your host is listed in an open spam relay.");
        }
        Logging.info("CLIENTCHK", "Client check thread completed.");
        util.getThreads().remove(Thread.currentThread());
    }
}
