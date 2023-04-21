package com.google.gwt.ajaxloader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

/**
 * Helps with the GWT uncaught exception handler.
 */
public abstract class ExceptionHelper {

    /**
   * If an uncaught exception handler has been registered, execute the Runnable 
   * in a try/catch block and handle exceptions with the uncaught exception 
   * handler.  Otherwise, run the Runnable and do not catch exceptions.
   * 
   * @param runnable The Runnable to execute.
   */
    public static void runProtected(Runnable runnable) {
        UncaughtExceptionHandler handler = GWT.getUncaughtExceptionHandler();
        if (handler != null) {
            try {
                runnable.run();
            } catch (Throwable e) {
                handler.onUncaughtException(e);
            }
        } else {
            runnable.run();
        }
    }

    private ExceptionHelper() {
    }
}
