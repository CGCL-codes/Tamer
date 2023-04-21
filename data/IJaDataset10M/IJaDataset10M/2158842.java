package com.tredart.timer;

import static com.tredart.timer.TimeConstants.THOUSAND;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.tredart.exceptions.ReceiveException;
import com.tredart.exceptions.SendException;
import com.tredart.utils.TimeStamp;

/**
 * A Timer produces timestamps synchronising itself with time servers around the
 * globe.
 * <p>
 * Add a set of timeServerClients to the timer and the invoke start(). The time
 * server will be running in background updating its information. To retrieve a
 * time stamp just call getTimeStamp() and you will get a time stamp independent
 * of the clock on the machine that is actually running the code.
 */
public class Timer extends Thread implements ITimer {

    private static final Logger LOGGER = Logger.getLogger(Timer.class);

    private static final String NOT_STARTED = "NOT_STARTED";

    private static final String STARTED = "STARTED";

    private String state = NOT_STARTED;

    private static final long OFFSET_NOT_AVAILABLE = -1;

    private long localClockOffset = OFFSET_NOT_AVAILABLE;

    /**
     * The delegated ITimeClient.
     */
    private List<ITimeServerClient> timeClients;

    /**
     * Default constructor.
     */
    public Timer() {
        timeClients = new ArrayList<ITimeServerClient>();
    }

    /**
     * {@inheritDoc}
     */
    public final TimeStamp getTimeStamp() {
        if (state.equals(NOT_STARTED)) {
            refresh();
            this.start();
        }
        if (localClockOffset != OFFSET_NOT_AVAILABLE) {
            return new TimeStamp(getCurrentLocalTime() + localClockOffset);
        }
        throw new IllegalStateException("It is not possible to get a time stamp because no " + "time server has been found to initialise the offset");
    }

    /**
     * Iterates through the clients trying to get the offset.
     */
    protected final void refresh() {
        for (ITimeServerClient client : timeClients) {
            try {
                client.tick();
                localClockOffset = (long) (client.getLocalClockOffset() * THOUSAND);
                return;
            } catch (SocketException e) {
                LOGGER.error("Timer for " + client.getServer() + " exception", e);
            } catch (UnknownHostException e) {
                LOGGER.error("Timer for " + client.getServer() + " exception", e);
            } catch (SendException e) {
                LOGGER.error("Timer for " + client.getServer() + " exception", e);
            } catch (ReceiveException e) {
                LOGGER.error("Timer for " + client.getServer() + " exception", e);
            }
        }
        LOGGER.error("No time server has been found, control you internet connection or your configuration");
    }

    /**
     * Refresh this info continuously.
     */
    @Override
    public final void run() {
        state = STARTED;
        while (true) {
            refresh();
            try {
                sleep(REFRESH_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the a time server clients to the timer.
     * 
     * @param clients
     *            the clients to add
     */
    public final void setTimeClients(final List<ITimeServerClient> clients) {
        this.timeClients = clients;
    }

    /**
     * Returns the current local time in millis.
     * 
     * @return the current local time in millis
     */
    protected long getCurrentLocalTime() {
        return System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        StringBuilder b = new StringBuilder("[");
        for (ITimeServerClient client : timeClients) {
            b.append(client.getServer()).append("; ");
        }
        b.append("]");
        return b.toString();
    }
}
