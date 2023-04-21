package org.shapeoforion.utils.timer;

/**
 * This class contains a utility method to sleep a while.
 *
 * There are two ways to use this interface: by implementing the interface, or by extending AbstractSleeper.
 * The good thing of AbstractSleeper is that it takes into account the overhead caused by executing a sleep. This
 * happens automatically.
 * 
 * @version 1.0
 * @author jps
 */
public interface Sleeper {

    /**
     * Sleep a number of milliseconds.
     * 
     * @param millis
     */
    public void sleep(final long millis);
}
