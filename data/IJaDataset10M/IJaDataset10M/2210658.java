package net.eiroca.j2me.sm.util;

/**
 * The <code>StoreObserver</code> interface defines the way for the store observers to be notified of any of change in the store content (objects added/removed).
 */
public interface StoreObserver {

    /** The Constant DEL. */
    public static final int DEL = 2;

    /** The Constant ADD. */
    public static final int ADD = 1;

    /**
   * This method is called when information about an Store which was previously requested using an asynchronous interface becomes available.
   * 
   * @param action the action
   * @param obj the obj
   * @param store the store
   */
    public void actionDone(int action, Object obj, Store store);
}
