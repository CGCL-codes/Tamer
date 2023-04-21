package com.volantis.shared.net.http;

import com.volantis.shared.throwable.ExtendedIOException;
import java.io.IOException;
import java.net.Socket;

/**
 * A transaction that simply waits for the specified period and then returns.
 */
public class WaitTransaction implements HttpTransaction {

    /**
     * The period to wait in milliseconds.
     */
    private final long periodInMillis;

    /**
     * Initialise.
     *
     * @param periodInMillis The period to wait in milliseconds.
     */
    public WaitTransaction(long periodInMillis) {
        this.periodInMillis = periodInMillis;
    }

    public void process(Socket socket) throws IOException {
        try {
            Thread.sleep(periodInMillis);
        } catch (InterruptedException e) {
            throw new ExtendedIOException(e);
        } finally {
            socket.close();
        }
    }
}
