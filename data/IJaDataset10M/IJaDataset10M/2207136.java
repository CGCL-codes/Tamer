package org.proclos.etlcore.persistence;

import java.util.Hashtable;

public class DatastoreManager {

    private static final DatastoreManager instance = new DatastoreManager();

    private Hashtable<String, Datastore> lookup = new Hashtable<String, Datastore>();

    DatastoreManager() {
    }

    public static final DatastoreManager getInstance() {
        return instance;
    }

    public void add(Datastore store) {
        lookup.put(store.getLocator().toString(), store);
    }

    public Datastore get(String locator) {
        return lookup.get(locator);
    }
}
