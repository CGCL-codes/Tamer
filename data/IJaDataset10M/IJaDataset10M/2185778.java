package com.ibm.tuningfork.tracegen;

/**
 * A timer event, which consists of a series of start and stop pairs over time.
 */
public interface ITimerEvent extends IEvent {

    /**
     * Start the timer using the feedlet bound to the current thread.
     */
    public void start();

    /**
     * Stop the timer using the feedlet bound to the current thread.
     */
    public void stop();

    /**
     * Start the timer using a specific feedlet.
     * 
     * @param feedlet The feedlet to use.
     */
    public void start(IFeedlet feedlet);

    /**
     * Start the timer using a specific feedlet.
     * 
     * @param feedlet The feedlet to use.
     */
    public void stop(IFeedlet feedlet);

    /**
     * Start the timer using a specific feedlet.
     * 
     * @param feedlet The feedlet to use.
     */
    public void start(IConversionFeedlet feedlet, long timeStamp);

    /**
     * Start the timer using a specific feedlet.
     * 
     * @param feedlet The feedlet to use.
     */
    public void stop(IConversionFeedlet feedlet, long timeStamp);
}
