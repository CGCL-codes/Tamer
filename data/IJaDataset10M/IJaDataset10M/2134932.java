package org.mortbay.util;

import java.util.EventListener;

/** Source of EventObjects for registered EventListeners.
 * the actual implementation of EventProvider must define what types of
 * EventListeners can be registered.
 * @author gregw
 *
 */
public interface EventProvider {

    /** Register an EventListener
     * @param listener
     * @throws IllegalArgumentException If the EventListener type is not supported.
     */
    public void addEventListener(EventListener listener) throws IllegalArgumentException;

    public void removeEventListener(EventListener listener);
}
