package net.sf.opentranquera.mdp.monitor;

import net.sf.opentranquera.mdp.ConnectException;
import net.sf.opentranquera.mdp.Monitor;

/**
 * TODO DOCUMENT ME!
 *
 * <br>Created 31/10/2005
 * @author <a href="mailto:diego.campodonico@eds.com">Diego Campodonico</a>
 */
public class BlockingThreadMonitor extends ThreadMonitor implements Monitor {

    public void run() {
        try {
            Thread.sleep(this.delayStart);
        } catch (InterruptedException ie) {
            return;
        }
        outer: while (true) {
            this.controller.closeAll();
            try {
                logger.info("Connecting to JMS provider...");
                this.controller.connect();
                logger.info("Connected to JMS provider");
                synchronized (Monitor.class) {
                    try {
                        Monitor.class.wait();
                        throw new ConnectException("The service has been disconnected to JMS provider.");
                    } catch (InterruptedException ie) {
                        logger.info("Thread interrupted. It is finish now!");
                        break outer;
                    }
                }
            } catch (ConnectException e) {
                logger.error(e.getMessage());
                logger.error("The JMS provider is not started. It will attempt to reconnect every " + this.delayReconnect + " miliseconds");
                try {
                    Thread.sleep(this.delayReconnect);
                } catch (InterruptedException ie) {
                    logger.info("Thread interrupted. It is finish now!");
                    break outer;
                }
            }
        }
    }
}
