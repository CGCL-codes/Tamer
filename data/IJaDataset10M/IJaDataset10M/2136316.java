package com.gargoylesoftware.htmlunit.javascript.background;

import java.io.Serializable;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * An event loop to execute all the JavaScript jobs.
 *
 * @version $Revision: 6701 $
 * @author Amit Manjhi
 * @author Kostadin Chikov
 * @author Ronald Brill
 */
public interface JavaScriptExecutor extends Runnable, Serializable {

    /**
     * Register a window with the eventLoop.
     * @param newWindow the new web window
     */
    void addWindow(final WebWindow newWindow);

    /**
     * Notes that this thread has been shutdown.
     */
    void shutdown();

    /**
     * Executes the jobs in the eventLoop till timeoutMillis expires or the eventLoop becomes empty.
     * No use in non-GAE mode.
     * @param timeoutMillis the timeout in milliseconds
     * @return the number of jobs executed
     */
    int pumpEventLoop(final long timeoutMillis);
}
