package com.dynomedia.esearch.util.groupkeycache.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerThreadPool {

    private static boolean isRunning = true;

    private static ExecutorService writeService;

    static {
        isRunning = true;
        writeService = Executors.newFixedThreadPool(GkcConfig.getInstance().getConsumerThreadCount());
    }

    public static void submit(Runnable task) {
        writeService.submit(task);
    }

    public static void shutdown() {
        isRunning = false;
        writeService.shutdown();
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setRunning(boolean isRunning) {
        ConsumerThreadPool.isRunning = isRunning;
    }
}
