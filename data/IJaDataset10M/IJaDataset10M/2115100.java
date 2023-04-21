package net.java.dev.joode.util;

/**
 * @author Tom Larkworthy
 *
 */
public class StopWatch {

    long lastTime;

    long totalTime;

    public void start() {
        lastTime = System.nanoTime();
    }

    public void stop() {
        totalTime += System.nanoTime() - lastTime;
    }

    public void reset() {
        totalTime = 0;
        lastTime = 0;
    }

    public long time() {
        return totalTime;
    }
}
