package org.konte.generate;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import org.konte.model.BitmapCache;

/**
 *
 * @author pto
 */
public class MemoryWatcher implements Runnable {

    private static boolean memoryNeeded;

    private static long memorySizeFactor;

    private MemoryMXBean mbb;

    private boolean rendering = false;

    public MemoryWatcher() {
        mbb = java.lang.management.ManagementFactory.getMemoryMXBean();
        setMemorySizeFactor(mbb.getHeapMemoryUsage().getMax() - 32000);
    }

    public void signalRender() {
        rendering = true;
    }

    public void signalRenderEnd() {
        rendering = false;
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        for (; ; ) {
            try {
                if (rendering) {
                    MemoryUsage musage = mbb.getHeapMemoryUsage();
                    if (musage.getMax() - musage.getUsed() < musage.getMax() / 4) {
                        setMemoryNeeded(true);
                        BitmapCache.clearCache();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ec) {
                            ec.printStackTrace();
                        }
                    }
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }

    public static void setMemoryNeeded(boolean b) {
        memoryNeeded = b;
    }

    public static boolean isMemoryNeeded() {
        return memoryNeeded;
    }

    private static void setMemorySizeFactor(long max) {
        memorySizeFactor = max;
    }

    public static long getMemorySizeFactor() {
        return memorySizeFactor;
    }

    public static int getMoveCountEstimate() {
        return (int) (getMemorySizeFactor() * 4 / 5 / 500);
    }
}
