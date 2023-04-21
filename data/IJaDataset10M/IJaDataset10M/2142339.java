package org.openbandy.example.test;

/**
 * Monitor object to indicate when the EventSourceSimulation
 * has sent all events.
 * 
 * <br><br>
 * (c) Copyright Philipp Bolliger 2007, ALL RIGHTS RESERVED.
 *
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @version 1.0
 */
public class AllEventsSentMonitor {

    public synchronized void setAllSent() {
        notifyAll();
    }

    public synchronized boolean areAllSent() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
        return true;
    }
}
