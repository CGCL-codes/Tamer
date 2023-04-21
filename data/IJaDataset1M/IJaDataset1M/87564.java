package lt.baltic_amadeus.jqbridge.server;

import lt.baltic_amadeus.jqbridge.providers.ChannelHandler;
import lt.baltic_amadeus.jqbridge.util.BooleanHolder;
import lt.baltic_amadeus.jqbridge.util.ErrorGenerator;
import lt.baltic_amadeus.jqbridge.util.ErrorRateControl;
import org.apache.log4j.Logger;

/**
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class ChannelInstance implements Runnable {

    private static Logger log = Logger.getLogger(ChannelInstance.class);

    private VirtualChannel channel;

    private Thread worker;

    private ChannelHandler[] handler;

    private BooleanHolder stopping;

    private ErrorRateControl erCtl;

    private ErrorGenerator buggySender;

    public ChannelInstance(VirtualChannel channel, int sequence) throws BridgeException {
        this.channel = channel;
        worker = new Thread(this, "CH:" + channel.getName() + "," + sequence);
        stopping = new BooleanHolder(false);
        erCtl = new ErrorRateControl();
        buggySender = new ErrorGenerator("BuggySender");
        if (channel.getName().equals("pfw") && sequence == 0) {
            buggySender.setErrorPattern(ErrorGenerator.EP_REVOLVER);
            buggySender.setInterval(10);
            buggySender.setCount(6);
        }
    }

    public VirtualChannel getChannel() {
        return channel;
    }

    public ChannelHandler getHandler(int side) {
        return handler[side];
    }

    public Server getServer() {
        return channel.getServer();
    }

    public ErrorGenerator getBuggySender() {
        return buggySender;
    }

    public void start() {
        worker.start();
    }

    public void stop() throws BridgeException {
        stopping.setValue(true);
        handler[Endpoint.SOURCE].stop();
    }

    public void close() {
        try {
            if (!worker.isAlive()) return;
            log.debug("Waiting for worker " + worker.getName() + " to exit normally");
            worker.join(6000);
            if (!worker.isAlive()) return;
            log.info("Interrupting worker " + worker.getName() + " because it did not exit normally");
            worker.interrupt();
            worker.join(2000);
            if (!worker.isAlive()) return;
            log.warn("Continuing with worker " + worker.getName() + " still running");
        } catch (InterruptedException ex) {
            log.warn("Server thread interrupted while closing channel " + channel.getName());
        }
    }

    public void run() {
        log.debug("Worker starting");
        do {
            erCtl.begin();
            log.info("Resuming operation");
            try {
                handler = new ChannelHandler[2];
                for (int side = Endpoint.SOURCE; side <= Endpoint.DESTINATION; side++) {
                    handler[side] = channel.getEndpoint(side).createHandler();
                }
            } catch (BridgeException ex) {
                log.error("Channel could not be established", ex);
                continue;
            }
            try {
                ChannelTransaction trx = new ChannelTransaction(this);
                boolean success = true;
                while (!Thread.interrupted() && success) {
                    success = trx.execute();
                    if (success) erCtl.success();
                }
            } finally {
                log.info("Closing channel");
                closeChannel();
            }
        } while (stopping.getValue() == false);
        log.debug("Worker exiting");
    }

    private void closeChannel() {
        for (int side = Endpoint.SOURCE; side <= Endpoint.DESTINATION; side++) {
            try {
                handler[side].close();
            } catch (BridgeException ex) {
                log.warn("Endpoint " + Endpoint.getMnemonic(side) + " of channel " + channel.getName() + " did not close gracefully", ex);
            }
        }
    }
}
