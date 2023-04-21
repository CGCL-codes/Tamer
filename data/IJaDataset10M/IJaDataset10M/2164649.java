package net.sf.saxon.trans;

/**
 * Utility class for collecting and reporting timing information, used only under diagnostic control
 */
public class Timer {

    private long start;

    private long prev;

    public Timer() {
        start = System.currentTimeMillis();
        prev = start;
    }

    public void report(String label) {
        long time = System.currentTimeMillis();
        System.err.println(label + " " + (time - prev) + "ms");
        prev = time;
    }

    public void reportCumulative(String label) {
        long time = System.currentTimeMillis();
        System.err.println(label + " " + (time - start) + "ms");
        prev = time;
    }
}
