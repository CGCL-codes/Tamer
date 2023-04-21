package org.elogistics.domain.interfaces.event;

import java.util.Set;

/**
 * An Interface to describe standard-eventsources within the domain-model
 * @author Jurkschat, Oliver
 *
 */
public interface IDomainEventDispatcher<T extends IAbstractDomainListener> {

    /**
	 * Gets the set of checklisteners. May initialize a new set lazyly.
	 * 
	 * @return	An initialized set of listeners.
	 */
    public Set<T> getCheckListeners();

    /**
	 * Gets the set of performlisteners. May initialize a new set lazyly.
	 * 
	 * @return An initialized set of listeners.
	 */
    public Set<T> getPerformListeners();

    /**
	 * Adds a checklistener to the set, if it's not registered yet.
	 * 
	 * @param newListner	A new listener to add.
	 */
    public void addCheckListener(T newListner);

    /**
	 * Adds a performlistner to the set, if it's not registered yet.
	 * 
	 * @param newListner	A new listener to add.
	 */
    public void addPerformListener(T newListner);

    /**
	 * Removes a checklistener from the set, if it's registered.
	 * 
	 * @param toRemove	The registered listener to remove.
	 */
    public void removeCheckListner(T toRemove);

    /**
	 * Removes a performlistener from the set, if it's registered.
	 * 
	 * @param toRemove	The registered listener to remove.
	 */
    public void removePerformListner(T toRemove);
}
