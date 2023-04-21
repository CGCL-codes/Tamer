package org.datanucleus.store.rdbms.exceptions;

import org.datanucleus.store.exceptions.DatastoreValidationException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.util.Localiser;

/**
 * A <tt>NotAViewException</tt> is thrown during schema validation if a
 * table should be a view but is found not to be in the database.
 */
public class NotAViewException extends DatastoreValidationException {

    private static final Localiser LOCALISER_RDBMS = Localiser.getInstance("org.datanucleus.store.rdbms.Localisation", RDBMSStoreManager.class.getClassLoader());

    /**
     * Constructs a not-a-view exception.
     * @param viewName Name of the view that is of the wrong type.
     * @param type the type of the object
     */
    public NotAViewException(String viewName, String type) {
        super(LOCALISER_RDBMS.msg("020013", viewName, type));
    }
}
