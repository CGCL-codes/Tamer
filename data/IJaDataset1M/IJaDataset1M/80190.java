package org.gwt.beansbinding.ui.client.model.list;

import java.util.EventListener;

/**
 * A {@code ListDataListener} can register with a {@link ListModel} and receive
 * notofication of updates to the model.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface ListDataListener extends EventListener {

    /**
   * Notifies the listener that the contents of the list have changed in some
   * way. This method will be called if the change cannot be notified via the
   * {@link #intervalAdded(ListDataEvent)} or the
   * {@link #intervalRemoved(ListDataEvent)} methods.
   * 
   * @param event the event
   */
    void contentsChanged(ListDataEvent event);

    /**
   * Notifies the listener that one or more items have been added to the list.
   * The {@code event} argument can supply the indices for the range of items
   * added.
   * 
   * @param event the event.
   */
    void intervalAdded(ListDataEvent event);

    /**
   * Notifies the listener that one or more items have been removed from the
   * list. The {@code event} argument can supply the indices for range of items
   * removed.
   * 
   * @param event the event
   */
    void intervalRemoved(ListDataEvent event);
}
