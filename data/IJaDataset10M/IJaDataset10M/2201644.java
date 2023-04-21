package org.jeuron.jlightning.event.listener;

import org.jeuron.jlightning.event.Event;

/**
 *
 * @author Mike Karrys
 * @since 1.1
 * @see AbstractEventListener
 */
public interface EventListener {

    /**
     * Method to receive {@link Event} notifications.
     * @param event the {@link Event} object
     */
    void onEvent(Event event);
}
