package org.dishevelled.observable.event;

import java.util.EventObject;
import org.dishevelled.observable.ObservableList;

/**
 * An event object representing a change made to
 * an observable list.
 *
 * @param <E> list element type
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public class ListChangeEvent<E> extends EventObject {

    /**
     * Create a new list change event with the specified
     * observable list as the event source.
     *
     * @param source source of the event
     */
    public ListChangeEvent(final ObservableList<E> source) {
        super(source);
    }

    /**
     * Return the source of this list change event as an
     * <code>ObservableList</code>.
     *
     * @return the source of this list change event as an
     *    <code>ObservableList</code>
     */
    public final ObservableList<E> getObservableList() {
        return (ObservableList<E>) super.getSource();
    }
}
