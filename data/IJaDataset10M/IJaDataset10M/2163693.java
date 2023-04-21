package com.sun.pdfview.display;

/**
 * A generic synchronized flag, because Java doesn't have one.
 */
class Flag {

    private boolean isSet;

    /**
     * Sets the flag.  Any pending waitForFlag calls will now return.
     */
    public synchronized void set() {
        isSet = true;
        notifyAll();
    }

    /**
     * Clears the flag.  Do this before calling waitForFlag.
     */
    public synchronized void clear() {
        isSet = false;
    }

    /**
     * Waits for the flag to be set, if it is not set already.
     * This method catches InterruptedExceptions, so if you want
     * notification of interruptions, use interruptibleWaitForFlag
     * instead.
     */
    public synchronized void waitForFlag() {
        if (!isSet) {
            try {
                wait();
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * Waits for the flag to be set, if it is not set already.
     */
    public synchronized void interruptibleWaitForFlag() throws InterruptedException {
        if (!isSet) {
            wait();
        }
    }
}
