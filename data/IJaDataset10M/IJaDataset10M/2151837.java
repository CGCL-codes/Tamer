package edu.cmu.sphinx.frontend;

/**
 *  The listener interface for being informed when a
 *  {@link Signal Signal} is generated.
 */
public interface SignalListener {

    /**
     * Method called when a signal is detected
     *
     * @param signal the signal
     *
     */
    public void signalOccurred(Signal signal);
}
