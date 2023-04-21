package net.sf.flophase.app.action;

/**
 * This is a listener that will be notified of add transaction events.
 */
public interface AddTransactionListener {

    /**
     * This method will be invoked when an add transaction event occurs.
     * 
     * @param event The event details.
     */
    public void addTransaction(AddTransactionEvent event);
}
