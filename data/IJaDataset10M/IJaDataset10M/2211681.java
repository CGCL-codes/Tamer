package net.sf.crsx;

/**
 * Object that can be observed by an {@link Observer}.
 * 
 * @author Takahide Nogayama
 * @version $Id: Observable.java,v 1.2 2010/07/20 15:05:20 krisrose Exp $
 */
public interface Observable {

    /**
	 * Add an observer.
	 */
    public void addObserver(Observer observer);

    /**
	 * Remove an observer.
	 */
    public void deleteObserver(Observer observer);

    /**
	 * Notify all observers.
	 */
    public void notifyObservers(Object[] observedObjects);
}
