package com.google.inject.tools.suite;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link ProgressHandler} that does nothing to display
 * the progress and blocks the calling thread while it runs.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class BlockingProgressHandler implements ProgressHandler {

    private final List<ProgressStep> steps = new ArrayList<ProgressStep>();

    private volatile boolean done;

    private Runnable executeAfter;

    static class BlockingProgressMonitor implements ProgressMonitor {

        public void begin(String label, int units) {
        }

        public void done() {
        }

        public ProgressMonitor getSubMonitor(int parentunits) {
            return this;
        }

        public void worked(int workedunits) {
        }
    }

    public void go(String label, boolean backgroundAutomatically) {
        done = false;
        for (ProgressStep step : steps) {
            step.run(new BlockingProgressMonitor());
            step.complete();
        }
        done = true;
        if (executeAfter != null) executeAfter.run();
    }

    public void go(String label, boolean backgroundAutomatically, boolean cancelThread) {
        go(label, backgroundAutomatically);
    }

    public void waitFor() {
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return done;
    }

    public void step(ProgressStep step) {
        steps.add(step);
    }

    public void executeAfter(Runnable executeAfter) {
        this.executeAfter = executeAfter;
    }
}
