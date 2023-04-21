package org.opendte.stats;

public class Counter implements Collector {

    private long count;

    private int increment;

    public Counter() {
        count = 0;
        increment = 1;
    }

    public Counter(int inc) {
        count = 0;
        increment = inc;
    }

    public synchronized void update() {
        count += increment;
    }

    public long getCount() {
        return count;
    }

    public void reset() {
        count = 0;
    }

    public void cumulate(Object o) {
        Counter cnt = (Counter) o;
        count += cnt.getCount();
    }

    public String toString() {
        return "Counter: value=" + count;
    }
}
