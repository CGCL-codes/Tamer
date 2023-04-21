package seventhsense.data.listenerlist;

import java.util.List;

/**
 * List with an implementation for the use with listeners.
 * Listeners can be registered to listen to changes in the list.
 * 
 * @author Parallan
 *
 * @param <E> type of list item
 */
public interface IListenerList<E> extends List<E> {

    /**
	 * Adds a listener
	 * 
	 * @param listener listener
	 */
    void addListener(IListItemListener<E> listener);

    /**
	 * Removes a listener
	 * 
	 * @param listener listener
	 */
    void removeListener(IListItemListener<E> listener);
}
