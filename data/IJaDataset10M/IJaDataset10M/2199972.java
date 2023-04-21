package com.topq.monitor;

import junit.framework.SystemTestCase4;
import org.junit.Before;
import org.junit.Test;

public class PyMonitorSamples extends SystemTestCase4 {

    private PyProcessMonitor monitor;

    @Before
    public void getMonitor() throws Exception {
        monitor = (PyProcessMonitor) system.getSystemObject("monitor");
    }

    @Test
    public void testProcessExists() throws Exception {
        boolean status = monitor.isProcessRunning("eclipse.exe");
        report.report("Eclipse process is " + (status ? "running" : "not running"), status);
    }

    @Test
    public void gatherStatistics() throws Exception {
        report.report("First time");
        monitor.setDone(false);
        Thread thread = new Thread(monitor);
        thread.start();
        Thread.sleep(10000);
        monitor.setDone(true);
        thread.join();
        monitor.getAvgCpu();
        monitor.getAvgMem();
        monitor.getAvgVirtualMem();
        monitor.getAvgPrivateBytes();
        monitor.getAvgThreadCount();
        monitor.getAvgHandleCount();
        monitor.setDone(false);
        thread = new Thread(monitor);
        thread.start();
        Thread.sleep(2000);
        monitor.setDone(true);
        thread.join();
        monitor.getCpu();
        monitor.getMem();
        monitor.getVirtualMem();
        monitor.getPrivateBytes();
        monitor.getThreadCount();
        monitor.getHandleCount();
    }
}
