package com.msimone.j2me;

/**
 * Interface for classes of objects that need to be notified when something they
 * where waiting for finished. The implementation is used when a certain form of
 * callback method is needed.
 * 
 * @author msimone
 * 
 */
public interface Waiter {

    /**
	 * Tells the waiter that what it is waiting for has finished.
	 */
    void ready();
}
