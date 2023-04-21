package uk.org.ogsadai.resource.event;

/**
 * This interface should be implemented by objects that wish
 * to notify persistence components when an resource or resource name
 * has been added/removed.
 * It is typically implemented by objects that the persistence
 * layer knows how to persist.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface ResourceEventDispatcher {

    /**
     * Add a listener to those to be notified of state changed.
     * 
     * @param listener
     *     the listener to be added
     */
    public void addResourceEventListener(ResourceEventListener listener);

    /**
     * Remove a listener from those to be notified of state changed.
     * 
     * @param listener 
     *    the listener to be removed
     */
    public void removeResourceEventListener(ResourceEventListener listener);
}
