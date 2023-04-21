package org.tanukisoftware.wrapper;

/**
 * WrapperShuttingDownExceptions are thrown when certain Wrapper functions
 *  are accessed after the Wrapper has started shutting down.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class WrapperShuttingDownException extends Exception {

    /**
     * Creates a new WrapperShuttingDownException.
     */
    WrapperShuttingDownException() {
        super();
    }
}
