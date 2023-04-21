package net.jonbuck.tassoo.eventmanager;

import net.jonbuck.tassoo.eventmanager.events.TassooEvent;

/**
 * <p>
 * <b>The ContactBarEventManager is responsible for orchestrating all of the
 * various events within the Contact Bar.</b>
 * </p>
 * 
 * @since 1.0.0
 */
public interface EventManager {

    /**
     * <p>
     * <b> Adds the listener to the EventManager for the specified event type.
     * </b>
     * </p>
     * 
     * @param eventType
     *            the type of event for which this listener is registering.
     * @param listener
     *            the listener that wants to be notified when the event is
     *            fired.
     */
    void addListener(final Class<?> eventType, Object listener);

    /**
     * <p>
     * <b> Notifies the listeners associated with the event type that the event
     * has occurred. </b>
     * </p>
     * 
     * @param eventType
     *            the event type whose listeners will be notified of the event.
     */
    void fireEvent(TassooEvent eventType);

    /**
     * <p>
     * <b> Removes the listener associated with the event type from the
     * EventManager. </b>
     * </p>
     * 
     * @param eventType
     *            the type of event for which the listener has been registered.
     * @param listener
     *            the listener to remove.
     */
    void removeListener(final Class<?> eventType, Object listener);
}
