package com.googlecode.springeventmanager;

import java.util.List;
import java.util.Map;

/**
 * Manager of Events.  {@link EventListener}s and 
 * {@link AsyncEventListener}s can be added to the EventManager
 * to handle events of any name.  Multiple listeners may
 * be added for any given event name.
 * 
 * TODO: Document this better, need to talk about best practices
 *  for defining event names and documenting event arguments and
 *  their offsets (if any).
 * 
 * @see EventListener
 * @see AsyncEventListener
 */
public interface EventManager {

    /**
     * Events that the EventManger itself raises.
     */
    interface Events {

        /**
         * <p>
         * Event raised when an {@link EventListener} is added
         * to the EventManager.
         * </p>
         * <p> 
         * <ol type="1" start="0">
         *  <li>{@link EventListener} the EventListener added</li>
         *  <li>{@link String} the event name</li>
         * </ol>
         * </p>
         */
        String EVENT_LISTENER_ADDED = "EventManager::EVENT_LISTENER_ADDED";

        /**
         * <p>
         * Event raised when an {@link EventListener} is added
         * to the EventManager.
         * </p>
         * <p> 
         * <ol type="1" start="0">
         *  <li>{@link EventListener} the EventListener removed</li>
         *  <li>{@link String} the event name</li>
         * </ol>
         * </p>
         */
        String EVENT_LISTENER_REMOVED = "EventManager::EVENT_LISTENER_REMOVED";
    }

    /**
     * Raises the given event. This method blocks until 
     * all non {@link AsyncEventListeners}s are finished 
     * handling the event.
     * @param event the event to raise
     * 
     * @see Event
     * @see EventListener
     * @see AsyncEventListener
     */
    void raiseEvent(Event event);

    /**
     * Raises an event with the given name and arguments.
     * This method blocks until all non {@link AsyncEventListeners}s
     * are finished handling the event.
     * @param name the name of the event
     * @param arguments the event arguments
     * 
     * @see Event
     * @see Event#getArgumentOfType(Class)
     * @see Event#getArgumentOfType(Class,int)
     * @see EventListener
     * @see AsyncEventListener
     */
    void raiseEvent(String name, Object... arguments);

    /**
     * Adds an {@link EventListener} that will handle events 
     * of the given name.
     * @param listener the listener to add
     * @param eventName the event name to handle
     * 
     * @see EventListener
     * @see AsyncEventListener
     */
    void addEventListener(EventListener listener, String eventName);

    /**
     * Removes the given {@link EventListener} as a handler
     * of the given eventName.
     * @param listener the listener to remove
     * @param eventName the event name to remove it from
     * 
     * @see EventListener
     * @see AsyncEventListener
     */
    void removeEventListener(EventListener listener, String eventName);

    /**
     * Removes the given {@link EventListener} as a handler 
     * from all event names.
     * @param listener the listener to remove
     * 
     * @see EventListener
     * @see AsyncEventListener
     */
    void removeEventListener(EventListener listener);

    /**
     * Returns a {@link Map} of all the registered {@link EventListener}s
     * where the key is the event name and the value is a {@link List}
     * of the EventListeners for that event name. Modifying this map 
     * does not modify registered EventListeners.
     * @return a Map of EventListeners
     */
    Map<String, List<EventListener>> getEventListeners();

    /**
     * Returns a {@link List} of all the registered {@link EventListener}s
     * for the given event name. Modifying this List  does not modify 
     * registered EventListeners.
     * @param eventName the eventName
     * @return a List of EventListeners
     */
    List<EventListener> getEventListeners(String eventName);
}
