package invertedindex;

import java.util.Date;

/**
 * Simple class for timing index and search operations
 */
public class StopWatch {

    String label;

    long startTime;

    public StopWatch(String label) {
        this.label = label;
        this.startTime = new Date().getTime();
    }

    public long stop() {
        long stopTime = new Date().getTime();
        long delta = stopTime - startTime;
        System.out.println(label + " : " + delta + " ms");
        return delta;
    }
}
