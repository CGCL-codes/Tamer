package playground.dressler.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import playground.dressler.control.Debug;

public class CPUTimer {

    ThreadMXBean bean;

    long startCPU;

    public long lastCPU = 0;

    public long iterCPU = 0;

    public long totalCPU = 0;

    boolean on = false;

    public String name = "";

    public CPUTimer() {
        bean = ManagementFactory.getThreadMXBean();
    }

    public CPUTimer(String s) {
        bean = ManagementFactory.getThreadMXBean();
        name = s + " ";
    }

    public void onoff() {
        if (!on) {
            startCPU = getCpuTime();
            on = true;
        } else {
            lastCPU = getCpuTime() - startCPU;
            iterCPU += lastCPU;
            totalCPU += lastCPU;
            on = false;
        }
    }

    public void newiter() {
        iterCPU = 0;
    }

    public long getLastMs() {
        return lastCPU / 1000000;
    }

    public long getIterMs() {
        return iterCPU / 1000000;
    }

    public long getTotalMs() {
        return totalCPU / 1000000;
    }

    public String formatMs(long ms) {
        if (ms < 10 * 1000) {
            return ms + "ms";
        } else {
            long sec = ms / 1000;
            long min = sec / 60;
            long hour = min / 60;
            sec = sec % 60;
            min = min % 60;
            return hour + ":" + (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
        }
    }

    /** Get CPU time in nanoseconds. */
    long getCpuTime() {
        if (Debug.TIMERS) return System.currentTimeMillis() * 1000000L;
        return 0;
    }

    /** Get user time in nanoseconds. */
    long getUserTime() {
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadUserTime() : 0L;
    }

    /** Get system time in nanoseconds. */
    long getSystemTime() {
        return bean.isCurrentThreadCpuTimeSupported() ? (bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime()) : 0L;
    }

    public String toString() {
        return name + formatMs(getLastMs()) + " , " + formatMs(getIterMs()) + " , " + formatMs(getTotalMs()) + " (last,iter,total)";
    }
}
