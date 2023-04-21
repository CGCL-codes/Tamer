package org.sgutil.daemon;

import java.util.concurrent.*;
import org.sgutil.storage.*;

public abstract class Daemon implements Runnable {

    public static CircularBuffer requestHistory;

    protected static void addRequestToHistory(ConcurrentRequest<?> request) {
        requestHistory.add(request);
    }

    protected ConcurrentLinkedQueue<ConcurrentRequest<?>> requests;

    public Daemon() {
        requests = new ConcurrentLinkedQueue<ConcurrentRequest<?>>();
        if (null == requestHistory) {
            requestHistory = new CircularBuffer();
        }
    }
}
