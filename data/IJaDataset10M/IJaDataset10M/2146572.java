package org.skycastle.util.propertyaccess.mutable;

import java.util.List;

/**
 * A listener that is notified when a list type property changes.
 * <p/>
 * H is the type of host object containing the list property, and E is the type of elements in the list property.
 * <p/>
 * Extends the normal property listener.  The normal property listener is called when the whole list changes.
 *
 * @author Hans H�ggstr�m
 */
public interface ListPropertyListener<H, E> extends PropertyListener<H> {

    /**
     * Called when an element is added to the list property.
     *
     * @param hostObject       the host object whose property changed.
     * @param listPropertyName The name of the list property that changed.
     * @param list             a read-only view on the list.
     * @param addedElement     an element that was added to the list.
     */
    void onElementAdded(H hostObject, String listPropertyName, List<E> list, E addedElement);

    /**
     * Called when an element is removed from the list property.
     *
     * @param hostObject       the host object whose property changed.
     * @param listPropertyName The name of the list property that changed.
     * @param list             a read-only view on the list.
     * @param removedElement   an element that was removed from the list.
     */
    void onElementRemoved(H hostObject, String listPropertyName, List<E> list, E removedElement);
}
