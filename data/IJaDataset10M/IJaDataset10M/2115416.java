package org.ecberkeley.css.threading.ex3;

import java.util.*;

public class Observer extends Thread {

    private volatile int nThreads;

    private ArrayList<WorkerThread> completedThreads = new ArrayList<WorkerThread>();

    private ArrayList<WorkerThread> incompleteThreads = new ArrayList<WorkerThread>();

    public Observer(int nThreads) {
        this.nThreads = nThreads;
    }

    private int threadTimeout = 60 * 1000;

    public void setThreadTimeout(int val) {
        threadTimeout = val;
    }

    public synchronized boolean allDone() {
        return nThreads == 0;
    }

    private boolean quitNow = false;

    public void run() {
        doWork(threadTimeout);
    }

    public synchronized void quit() {
        quitNow = true;
    }

    private void cleanup() {
        System.out.println("doWork timed out.");
        System.out.println("Observer timed out. Stopping thread count: " + nThreads);
        Iterator it = incompleteThreads.iterator();
        while (it.hasNext()) {
            WorkerThread wt = (WorkerThread) it.next();
            System.out.println("Interrupting workerThread:" + wt.getName());
            wt.interrupt();
        }
        incompleteThreads.clear();
        notify();
        return;
    }

    public synchronized void doWork(int timeoutMillis) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeoutMillis;
        for (int i = 0; i < nThreads; i++) {
            WorkerThread t = new WorkerThread(this);
            incompleteThreads.add(t);
            t.start();
        }
        while (nThreads > 0) {
            long now = System.currentTimeMillis();
            long remainingTime = endTime - now;
            if (quitNow || remainingTime <= 0) {
                cleanup();
                return;
            }
            System.out.println("doWork will wait up to: " + remainingTime);
            try {
                wait(remainingTime);
                System.out.println("doWork is done with one wait");
            } catch (InterruptedException e) {
                System.out.println("Observer interrupted.");
            }
        }
        notify();
    }

    public synchronized void callback(WorkerThread t) {
        completedThreads.add(t);
        incompleteThreads.remove(t);
        System.out.println("Observer: one thread done: " + t.getPayload());
        nThreads--;
        notify();
    }

    public synchronized int workersRemaining() {
        return nThreads;
    }

    public synchronized List getList() {
        return (List) completedThreads.clone();
    }
}
