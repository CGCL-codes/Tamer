package org.datanucleus.store.types.sco.queued;

import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.scostore.MapStore;

/**
 * Remove operation for a collection or map.
 */
public class RemoveMapOperation implements QueuedOperation<MapStore> {

    /** The value to remove. */
    private final Object value;

    /**
     * Constructor.
     * @param value The value to remove
     */
    public RemoveMapOperation(Object value) {
        this.value = value;
    }

    /**
     * Accessor for the value being removed.
     * @return Value being removed
     */
    public Object getValue() {
        return value;
    }

    /**
     * Perform the remove(Object) operation on the specified container.
     * @param store The backing store to perform it on
     * @param sm StateManager for the owner of the container
     */
    public void perform(MapStore store, ObjectProvider sm) {
        store.remove(sm, value);
    }
}
