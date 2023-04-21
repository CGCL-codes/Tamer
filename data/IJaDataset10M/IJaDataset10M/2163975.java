package org.jcvi.tasker.flu;

import org.hibernate.Session;
import org.jcvi.fluvalidator.errors.DeletionError;
import org.jcvi.fluvalidator.errors.InsertionError;
import org.jcvi.fluvalidator.errors.ValidationError;
import org.jcvi.glk.task.TaskFeatureType;
import org.jcvi.tasker.CommonFeatureType;
import org.jcvi.tasker.db.DatabaseFeatureTypeLookup;

/**
 * A <code>FluDatabaseFeatureTypeLookup</code> is a {@link DatabaseFeatureTypeLookup} with
 * default associations for resolving Influenza {@link ValidationError}s to
 * {@link TaskFeatureType}s.
 *
 * @author jsitz@jcvi.org
 */
public class FluDatabaseFeatureTypeLookup extends DatabaseFeatureTypeLookup {

    /**
     * Constructs a new <code>FluDatabaseFeatureTypeLookup</code>.
     *
     * @param session
     */
    public FluDatabaseFeatureTypeLookup(Session session) {
        super(session);
        this.initialize();
    }

    /**
     * Initialize the lookup, setting any default associations.
     */
    private void initialize() {
        this.assign(InsertionError.class, CommonFeatureType.INSERTION);
        this.assign(DeletionError.class, CommonFeatureType.DELETION);
    }
}
