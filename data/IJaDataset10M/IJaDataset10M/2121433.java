package org.sourceforge.jemm;

/**
 * 
 * 
 * @author Rory Graves
 * @author Paul Keeble
 */
public final class Session {

    /** Handle to the currently active store. */
    private static Store currentStore;

    /** Private constructor to prevent instantiation. */
    private Session() {
    }

    /**
	 * Set the currently active store.
	 * 
	 * @param store
	 *            The store to make active.
	 */
    public static synchronized void setStore(Store store) {
        currentStore = store;
        currentStore.initialise();
    }

    public static synchronized void shutdown() {
        if (currentStore != null) {
            currentStore.shutdown();
            currentStore = null;
        }
    }

    public static Object getRoot(String rootName) {
        return currentStore.getRoot(rootName);
    }

    public static void setRoot(String rootName, Object newRootObject) {
        currentStore.setRoot(rootName, newRootObject);
    }

    public static Object setRootIfNull(String rootName, Object newRootObject) {
        return currentStore.setRootIfNull(rootName, newRootObject);
    }

    /**
	 * Return a reference to the current active JEMMStore
	 * 
	 * @return The current active JEMMStore or null if no store is active.
	 */
    public static Store getActiveStore() {
        return currentStore;
    }
}
