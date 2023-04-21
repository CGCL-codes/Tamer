package org.openintents.tools.simulator.util;

/**
 * Mutable integer accessible on multiple threads. 
 * @author ilarele
 *
 */
public class SynchronizedInteger {

    private int value = 0;

    public synchronized void setValue(int value) {
        this.value = value;
    }

    public synchronized int getValue() {
        return value;
    }
}
