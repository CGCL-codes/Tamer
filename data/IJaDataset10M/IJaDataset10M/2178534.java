package org.gcreator.pineapple.managers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A default Uncaught Exception Handler
 * 
 * @author Serge Humphrey
 */
public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * Stores the exception into the log.
     * @param t The thread in which the exception happened
     * @param e The exception
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger.getLogger(t.getClass().getName()).log(Level.SEVERE, "Exception in thread " + t.getName() + ":", e);
    }
}
