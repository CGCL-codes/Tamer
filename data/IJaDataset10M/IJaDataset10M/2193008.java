package DE.FhG.IGD.util;

import java.io.*;
import java.util.*;

/**
 * This is a convenience class for writing objects that
 * block on signals such as closing a window.
 * It defines a map of named objects on which threads
 * may block until they receive notification.
 *
 * @author Jan Peters
 * @version "$Id: Signals.java 759 2002-11-14 22:29:31Z jpeters $"
 */
public class Signals extends Object {

    /**
     * This map holds the named objects. Each name
     * corresponds to a signal that may be sent and
     * on which threads of the agent may block.
     */
    private Map signals_;

    /**
     * Creates an instance of this class. The primary
     * purpose of this class is to initialise the map of signals.
     */
    public Signals() {
        signals_ = new HashMap();
    }

    /**
     * This method blocks the calling thread until the
     * signal is sent by another thread or an interrupt
     * occurs. If no such signal already exists then a
     * new one will be created for the given name.
     *
     * @param sig The name of the signal to wait for.
     */
    public void waitForSignal(String name) {
        Object signal;
        synchronized (signals_) {
            signal = signals_.get(name);
            if (signal == null) {
                signal = new Object();
                signals_.put(name, signal);
            }
        }
        synchronized (signal) {
            try {
                signal.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * This method signals all threads waiting on the
     * signal with the given name. If a signal with
     * that name does not exists then nothing happens.
     *
     * @param name The name of the signal.
     */
    public void sendSignal(String name) {
        Object signal;
        synchronized (signals_) {
            signal = signals_.get(name);
        }
        if (signal != null) {
            synchronized (signal) {
                signal.notifyAll();
            }
        }
    }
}
