package org.gwtoolbox.commons.ui.client.event.custom;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Uri Boness
 */
public class RemoveEvent<T> extends GwtEvent<RemoveHandler<T>> {

    /**
     * Handler type.
     */
    private static Type<RemoveHandler<?>> TYPE;

    /**
     * Fires a selection event on all registered handlers in the handler
     * manager.If no such handlers exist, this method will do nothing.
     *
     * @param <T> the selected item type
     * @param source the source of the handlers
     * @param removedItem the selected item
     */
    public static <T> void fire(HasRemoveHandlers<T> source, T removedItem) {
        if (TYPE != null) {
            RemoveEvent<T> event = new RemoveEvent<T>(removedItem);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static Type<RemoveHandler<?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<RemoveHandler<?>>();
        }
        return TYPE;
    }

    private final T removedItem;

    /**
     * Creates a new selection event.
     *
     * @param removedItem removed item
     */
    protected RemoveEvent(T removedItem) {
        this.removedItem = removedItem;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Type<RemoveHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    /**
     * Gets the removed item.
     *
     * @return the selected item
     */
    public T getRemovedItem() {
        return removedItem;
    }

    @Override
    protected void dispatch(RemoveHandler<T> handler) {
        handler.onRemove(this);
    }
}
