package org.paninij.runtime;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalTimeStamp {

    private static AtomicInteger timestamp = new AtomicInteger(0);

    public static synchronized int incrementTimeStamp() {
        return timestamp.incrementAndGet();
    }

    public static synchronized int getCurrentTimeStamp() {
        return timestamp.get();
    }
}
