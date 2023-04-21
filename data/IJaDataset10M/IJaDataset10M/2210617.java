package com.incendiaryblue.cmslite.index;

import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.storage.StorageManager;

/**
 * A 'home' object for terms.
 *
 * <p>The concept of a home object is borrowed from EJB.  This object implements as
 * instance methods those methods that would normally have been static in the Term
 * bussiness object.  The reason for this is so we can use two instances of this
 * object for the uptodate and temporary versions of the terms table.</p>
 */
class TermHome {

    private static final String QUERY_BY_NAME = "queryByName";

    private StorageManager storageManager;

    public TermHome(String name) {
        storageManager = (StorageManager) AppConfig.getComponent(StorageManager.class, name);
        if (storageManager == null) throw new IllegalStateException("Storage manager not found: " + name);
    }

    public Term getTerm(String name) {
        return (Term) storageManager.getObject(name);
    }

    public Term createTerm(String name, Integer termId) {
        Term t = new Term(name, termId);
        storageManager.store(t);
        return t;
    }

    public void clearCaches() {
        storageManager.clearCaches();
    }
}
