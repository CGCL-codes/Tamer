package org.tidelaget.core;

/**
 * The ShutdownAbleIfc used by components that need to be notified when the application 
 * is about to terminate
 * @author  unkown
 */
public interface ShutdownAbleIfc {

    /**
     *  Do cleanUp actions when the VM is about to shutdown
     */
    void cleanUp();
}
