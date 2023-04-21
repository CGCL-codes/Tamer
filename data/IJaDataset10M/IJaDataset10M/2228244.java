package nl.huub.van.amelsvoort.sys;

import sun.misc.Perf;

class HighPrecisionTimer extends Timer {

    private Perf perf = Perf.getPerf();

    private double f = 1000.0 / perf.highResFrequency();

    private long base;

    HighPrecisionTimer() {
        base = perf.highResCounter();
    }

    public long currentTimeMillis() {
        long time = perf.highResCounter();
        long delta = time - base;
        if (delta < 0) {
            delta += Long.MAX_VALUE + 1;
        }
        return (long) (delta * f);
    }
}
