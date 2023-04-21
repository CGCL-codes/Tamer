package com.notuvy.thread;

import java.util.List;
import java.util.ArrayList;

/**
 * A collection of controllable workers.
 * The main purpose is to allow an application to build a list of all background
 * thread workers, and then perform a graceful shutdown of all when needed.
 *
 * @author murali
 */
public class ThreadControlGroup {

    private final List<ThreadControllable> fList = new ArrayList<ThreadControllable>();

    public void add(ThreadControllable pControllable) {
        fList.add(pControllable);
    }

    /**
     * Terminate each thread then wait for each to finish any current work.
     */
    public void gracefulShutdown() {
        for (ThreadControllable controllable : fList) {
            controllable.getControl().stop();
        }
        for (ThreadControllable controllable : fList) {
            controllable.getControl().waitForQuiescence();
        }
    }
}
