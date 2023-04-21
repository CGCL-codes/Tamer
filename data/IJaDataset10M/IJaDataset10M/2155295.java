package org.creativor.rayson.server;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.creativor.rayson.util.Log;

/**
 * A worker thread to take server call out of transport layer and process it,
 * and send the response back to transport layer.
 * 
 * @author Nick Zhang
 */
class CallWorker extends Thread {

    private static final Logger LOGGER = Log.getLogger();

    private static final AtomicLong UID = new AtomicLong(0);

    private long id;

    private RpcServer server;

    private boolean running = true;

    /**
	 * Instantiates a new call worker.
	 * 
	 * @param server
	 *            the server
	 */
    CallWorker(RpcServer server) {
        this.server = server;
        this.id = UID.getAndIncrement();
        setName("Call worker " + id + " of  " + server.getName());
    }

    /**
	 * Quit this connection call worker thread.
	 */
    void quit() {
        this.running = false;
    }

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    @Override
    public long getId() {
        return id;
    }

    /**
	 * Run.
	 */
    @Override
    public void run() {
        LOGGER.info(getName() + " starting ...");
        while (running) {
            try {
                ServerCall call = this.server.getRpcConnector().takeCall();
                this.server.invokeCall(call);
                try {
                    this.server.getRpcConnector().responseCall(call);
                } catch (Throwable e) {
                    LOGGER.log(Level.SEVERE, "Response call " + call.getId() + " error!");
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                break;
            } catch (Throwable e) {
                LOGGER.log(Level.SEVERE, "Call worker got a error", e);
            }
        }
        LOGGER.info(getName() + " stoped");
    }
}
