package org.sourceforge.jemm.client;

import org.sourceforge.jemm.JEMMInternalException;

/**
 * A definitive source to get the current ObjectDatabase being used within the JEMM context.
 * 
 * Defaults to a null Database.
 * 
 * @author Paul Keeble
 *
 */
public final class ObjectDatabaseSingleton {

    private static ObjectDatabase db = null;

    /** Private constructor to prevent instantiation */
    private ObjectDatabaseSingleton() {
    }

    /**
	 * Returns an Instance of ObjectDatabase if one has been set previously by
	 * a call to setInstance.
	 * 
	 * @return The ObjectDatabase instance.
	 */
    public static synchronized ObjectDatabase getInstance() {
        if (db == null) throw new JEMMInternalException("ObjectDatabase instance has not been set");
        return db;
    }

    /**
	 * Sets a new instance. To clear the value set it to null.
	 * 
	 * @param newDB The new objectDatabase to use, or null to erase the existing one
	 */
    public static synchronized void setInstance(ObjectDatabase newDB) {
        db = newDB;
    }
}
