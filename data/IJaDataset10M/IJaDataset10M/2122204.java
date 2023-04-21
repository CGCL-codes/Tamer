package org.blojsom.event;

/**
 * BlojsomEventBroadcaster
 *
 * @author David Czarnecki
 * @since blojsom 2.18
 * @version $Id: BlojsomEventBroadcaster.java,v 1.5 2006-01-04 16:59:54 czarneckid Exp $
 */
public interface BlojsomEventBroadcaster {

    /**
     * Add a event to this event broadcaster
     *
     * @param listener {@link BlojsomListener}
     */
    public void addListener(BlojsomListener listener);

    /**
     * Add a event to this event broadcaster. Events are filtered using the {@link BlojsomFilter} instance
     * passed to this method.
     *
     * @param listener {@link BlojsomListener}
     * @param filter {@link BlojsomFilter} used to filter events
     */
    public void addListener(BlojsomListener listener, BlojsomFilter filter);

    /**
     * Remove a event from this event broadcaster
     *
     * @param listener {@link BlojsomListener}
     */
    public void removeListener(BlojsomListener listener);

    /**
     * Broadcast an event to all listeners
     *
     * @param event {@link org.blojsom.event.BlojsomEvent} to be broadcast to all listeners
     */
    public void broadcastEvent(BlojsomEvent event);

    /**
     * Process an event with all listeners
     *
     * @param event {@link BlojsomEvent} to be processed by all listeners
     * @since blojsom 2.24
     */
    public void processEvent(BlojsomEvent event);
}
