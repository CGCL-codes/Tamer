package org.jimcat.model.notification;

import java.util.List;
import java.util.Set;

/**
 * An interface describing a listener interrestied in collection changes.
 * 
 * $Id: CollectionListener.java 999 2007-09-14 20:02:58Z cleiter $
 * 
 * @author csag1760
 * @param <T>
 * @param <U>
 */
public interface CollectionListener<T extends ObservableBean<T>, U extends ObservableCollection<T, ? super U>> {

    /**
	 * will be called if a set of elements has been added
	 * 
	 * @param collection
	 * @param elements
	 */
    void elementsAdded(U collection, Set<T> elements);

    /**
	 * will be called if a set of elements is removed
	 * 
	 * @param collection
	 * @param elements
	 */
    void elementsRemoved(U collection, Set<T> elements);

    /**
	 * Some contained elements have changed some of thire values.
	 * 
	 * The list of BeanChangeEvents is in cronological order.
	 * 
	 * @param collection
	 * @param events -
	 *            the BeanChangeEvents containing the change informations
	 */
    void elementsUpdated(U collection, List<BeanChangeEvent<T>> events);

    /**
	 * will be called if there is a major change in basement structure
	 * (resorted, completly exchanged, ...)
	 * 
	 * @param collection
	 */
    void basementChanged(U collection);
}
