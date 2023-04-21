package cz.psika.numerist;

/**
 * Simple observable interface for items.
 *
 * @author Tomas Psika
 */
public interface ItemObservable {

    /**
     * Attach observer to the object.
     *
     * @param observer an observer to attach
     */
    public void addObserver(ItemObserver observer);

    /**
     * Detach observer.
     *
     * @param observer to remove
     */
    public void deleteObserver(ItemObserver observer);

    /**
     * Notify all attached observers.
     *
     * @param event type of event to notify all registered observers
     */
    public void notifyObservers(Constants.ItemEvents event);
}
