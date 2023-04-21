package net.sf.nachocalendar.event;

import java.util.EventListener;

/**
 *
 * @author  Ignacio Merani
 */
public interface MonthChangeListener extends EventListener {

    /**
     * Called when the day overflows.
     * @param e Event fired
     */
    void monthIncreased(MonthChangeEvent e);

    /**
     * Called when the day underflows.
     * @param e Event fired
     */
    void monthDecreased(MonthChangeEvent e);
}
