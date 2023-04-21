package com.continuent.bristlecone.benchmark.test;

import java.util.Properties;
import com.continuent.bristlecone.benchmark.Monitor;

/**
 * Most basic scenario with counters for required instance methods.
 * 
 * @author rhodges
 */
public class SimpleMonitor implements Monitor {

    public static int calledPrepare = 0;

    public static int calledRun = 0;

    public static int calledCleanup = 0;

    public static synchronized void clearCounters() {
        calledPrepare = 0;
        calledRun = 0;
        calledCleanup = 0;
    }

    public SimpleMonitor() {
        super();
    }

    public void prepare(Properties p) {
        calledPrepare++;
    }

    public void run() {
        calledRun++;
        try {
            while (!Thread.interrupted()) {
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
        }
    }

    public void cleanup() {
        calledCleanup++;
    }
}
