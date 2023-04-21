package org.fishwife.jrugged;

import java.util.concurrent.Callable;

/** This is a statistics wrapper that records the latency of requests,
 *  both for successes and failures. The most recent measurement is
 *  available for query, and can be polled periodically to determine 
 *  average latencies. 
 */
public class LatencyTracker implements ServiceWrapper {

    private long lastSuccessMillis;

    private long lastFailureMillis;

    public <T> T invoke(Callable<T> c) throws Exception {
        long start = System.currentTimeMillis();
        try {
            T result = c.call();
            lastSuccessMillis = System.currentTimeMillis() - start;
            return result;
        } catch (Exception e) {
            lastFailureMillis = System.currentTimeMillis() - start;
            throw e;
        }
    }

    public void invoke(Runnable r) throws Exception {
        long start = System.currentTimeMillis();
        try {
            r.run();
            lastSuccessMillis = System.currentTimeMillis() - start;
        } catch (Exception e) {
            lastFailureMillis = System.currentTimeMillis() - start;
            throw e;
        }
    }

    public <T> T invoke(Runnable r, T result) throws Exception {
        long start = System.currentTimeMillis();
        try {
            r.run();
            lastSuccessMillis = System.currentTimeMillis() - start;
            return result;
        } catch (Exception e) {
            lastFailureMillis = System.currentTimeMillis() - start;
            throw e;
        }
    }

    /** Returns how long the last successful request took.
	 *  @return long request service time in milliseconds */
    public long getLastSuccessMillis() {
        return lastSuccessMillis;
    }

    /** Returns how long the last failed request took.
	 *  @return long request service time in milliseconds. */
    public long getLastFailureMillis() {
        return lastFailureMillis;
    }
}
