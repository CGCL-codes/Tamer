package org.sharrissf.performance.client;

import java.util.HashSet;
import java.util.Set;
import org.sharrissf.performance.action.LifeCycleListener;
import org.sharrissf.performance.action.PerformanceAction;
import org.sharrissf.performance.configuration.PerformanceConfiguration;

public class PerformanceTestRun {

    private final Set<PerformanceThread> threads = new HashSet<PerformanceThread>();

    private final Set<PerformanceAction> actions = new HashSet<PerformanceAction>();

    private final PerformanceConfiguration config;

    private boolean notCompleted = true;

    private int totalOperationCount;

    private Set<LifeCycleListener> listeners = new HashSet<LifeCycleListener>();

    public PerformanceTestRun(Set<PerformanceAction> actions, PerformanceConfiguration config) {
        this.actions.addAll(actions);
        this.config = config;
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        listeners.add(listener);
    }

    public void waitForTestComplete() {
        synchronized (this) {
            while (notCompleted) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }
            }
        }
    }

    public void runTest() {
        System.out.println("Starting client");
        long testEnd = System.currentTimeMillis() + config.getTestDuration();
        for (int i = 0; i < config.getClientThreadCount(); i++) {
            PerformanceThread t = new PerformanceThread(actions, testEnd);
            threads.add(t);
            t.start();
        }
        for (PerformanceThread t : threads) {
            try {
                t.join();
                totalOperationCount += t.operationCount;
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
        notifyListeners();
    }

    private void notifyListeners() {
        for (LifeCycleListener listener : listeners) {
            listener.notifyTestCompleted(this.totalOperationCount);
        }
    }

    private static class PerformanceThread extends Thread {

        private Set<PerformanceAction> actions;

        private long testEnd;

        private volatile int operationCount = 0;

        public PerformanceThread(Set<PerformanceAction> actions, long testEnd) {
            super("Perf thread");
            setDaemon(true);
            this.actions = actions;
            this.testEnd = testEnd;
        }

        public void run() {
            System.out.println("Running Thread");
            while ((System.currentTimeMillis()) < testEnd) {
                for (PerformanceAction action : actions) {
                    action.execute();
                    operationCount++;
                }
            }
        }
    }

    public void setNodeID(int nodeid) {
        for (PerformanceAction action : actions) {
            action.setNodeID(nodeid);
        }
    }

    public void setNodeCount(int nodeCount) {
        for (PerformanceAction action : actions) {
            action.setNodeCount(nodeCount);
        }
    }

    public void preRun() {
        for (PerformanceAction action : actions) {
            action.preRun();
        }
    }

    public void postRun() {
        for (PerformanceAction action : actions) {
            action.postRun();
        }
    }
}
