package org.sqsh.signals;

/**
 * Generic signal handler used to send a thread an interrupt.
 */
public class InterruptingSignalHandler extends FlaggingSignalHandler {

    private Thread interruptThread;

    public InterruptingSignalHandler() {
        interruptThread = Thread.currentThread();
    }

    public InterruptingSignalHandler(Thread thread) {
        interruptThread = thread;
    }

    public void signal(Sig sig) {
        System.err.println("^C");
        interruptThread.interrupt();
        triggered = true;
    }
}
