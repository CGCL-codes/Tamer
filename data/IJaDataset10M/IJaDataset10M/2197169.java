package edu.java.concurrent.ch3;

public class GuidedClassUsingNotify {

    protected boolean cond = false;

    protected int nWaiting = 0;

    protected synchronized void awaitCond() throws InterruptedException {
        while (!cond) {
            ++nWaiting;
            try {
                wait();
            } catch (InterruptedException ie) {
                notify();
                throw ie;
            } finally {
                --nWaiting;
            }
        }
    }
}
