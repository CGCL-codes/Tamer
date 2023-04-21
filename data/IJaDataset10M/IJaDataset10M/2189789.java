package net.sf.flophase.app.action;

/**
 * Indicates that the class handles a "Add Transaction" action.
 */
public interface HasAddTransactionListeners {

    /**
     * Add a listener to be notified of a "Add Transaction" action.
     * 
     * @param listener The listener.
     */
    public void addAddTransactionListener(AddTransactionListener listener);

    /**
     * Notifies all listeners that the "Add Transaction" action occurred.
     * 
     * @param event The event details.
     */
    public void notifyAddTransactionListeners(AddTransactionEvent event);

    /**
     * Removes a listener.
     * 
     * @param listener The listener.
     */
    public void removeAddTransactionListener(AddTransactionListener listener);
}
