package edu.umd.cs.guitar.replayer.monitor;

import edu.umd.cs.guitar.exception.GException;
import edu.umd.cs.guitar.replayer.GReplayerMonitor;

public class WGCrashMonitor extends GTestMonitor {

    private GReplayerMonitor monitor;

    public WGCrashMonitor(GReplayerMonitor monitor) {
        this.monitor = monitor;
    }

    public void init() {
    }

    public void term() {
    }

    public void exceptionHandler(GException e) {
        monitor.cleanUp();
    }

    public void beforeStep(TestStepStartEventArgs step) {
    }

    public void afterStep(TestStepEndEventArgs eStep) {
    }
}
