package com.topq.monitor;

import junit.framework.SystemTestCase;

public class MonitorSamples extends SystemTestCase {

    private MachineMonitor monitor;

    @Override
    public void setUp() throws Exception {
        monitor = (MachineMonitor) system.getSystemObject("machineMonitor");
    }

    public void testStartMonitor() throws Exception {
        monitor.setInterval(500);
        monitor.start();
    }

    public void testHelloWorld() throws Exception {
        report.report("hello world");
        sleep(4000);
    }

    public void testStopMonitor() throws Exception {
        monitor.stop();
        monitor.saveToCsv();
    }
}
