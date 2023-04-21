package com.eris4.benchdb.core.monitor;

import java.util.LinkedList;
import java.util.List;
import com.eris4.benchdb.core.util.ThreadUtils;

public class CpuMonitor extends Monitor {

    private static final int PERIOD_CHECK = 300;

    private boolean stop;

    private List<ThreadMonitor> threadMonitors = new LinkedList<ThreadMonitor>();

    private int cpuUsage;

    public void begin() {
        stop = false;
        new Thread() {

            public void run() {
                int tmp;
                while (!stop) {
                    tmp = 0;
                    for (ThreadMonitor threadMonitor : threadMonitors) {
                        tmp += threadMonitor.getValue();
                    }
                    synchronized (this) {
                        cpuUsage = tmp;
                    }
                    ThreadUtils.sleep(PERIOD_CHECK);
                }
            }
        }.start();
    }

    public void addThread(Thread thread) {
        threadMonitors.add(new ThreadMonitor(thread));
    }

    public void end() {
        stop = true;
    }

    public String getDescription() {
        return "Cpu usage";
    }

    public long getAvgValue() {
        int result = 0;
        for (ThreadMonitor threadMonitor : threadMonitors) {
            result += threadMonitor.getAvgValue();
        }
        return result;
    }

    public long getValue() {
        synchronized (this) {
            return cpuUsage;
        }
    }

    public void reset() {
        threadMonitors = new LinkedList<ThreadMonitor>();
        stop = false;
        cpuUsage = 0;
    }
}
