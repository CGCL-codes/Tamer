package net.sf.traser.client.scanner;

/**
 * This interface is to be implemented by Auto-ID reader drivers. The interface 
 * only defines two methods that support the management of the registered 
 * listeners to notify on reading events, but the implementations must provide
 * that all listeners get called back, when a reading event happens. The type of
 * callback method called depends on the outcome of the reading event.
 * 
 * @see net.sf.traser.client.scanner.IdentifierListener
 * @author Marcell Szathmári
 */
public interface IdentifierScanner {

    /**
     * Inserts the listener into the array of listeners that need to be notified
     * upon detection of an identifier.
     * @param lsnr the listener to insert.
     */
    void register(IdentifierListener lsnr);

    /**
     * Removes the listener from the array of listeners that need to be notified
     * upon detection of an identifier.
     * @param lsnr the listener to remove.
     */
    void unRegister(IdentifierListener lsnr);
}
