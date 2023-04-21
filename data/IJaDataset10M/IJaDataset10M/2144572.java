package org.jeuron.jlightning.event.listener;

import org.jeuron.jlightning.event.Event;

/**
 *
 * @author Mike Karrys
 * @since 1.1
 */
public abstract class AbstractEventListener implements EventListener {

    /**
     * Method implemented by sub-classes to receive {@link Event} notifcations.
     * @param event the {@link Event} object
     */
    public abstract void onEvent(Event event);
}
