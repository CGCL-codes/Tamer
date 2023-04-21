package org.pirate.simpledebate.database;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.apache.log4j.Logger;
import org.pirate.simpledebate.shared.DatabaseException;

/**
 * This class is used to store the id of the debate which is shown when no thesis id is specified.
 * 
 * @author Arne Mueller <arne.c.mueller@googlemail.com>
 *
 */
@PersistenceCapable
public class RootDebate {

    @NotPersistent
    private static final Logger log = Logger.getLogger(RootDebate.class);

    /** Id with which to find the entry in the database. */
    @NotPersistent
    private static final String ROOT_KEY = "root";

    /** Will always be equal to {@link #ROOT_KEY}. */
    @SuppressWarnings("unused")
    @PrimaryKey
    private String key;

    /** Id of the thesis to show as default. */
    @Persistent
    private long thesisId;

    /**
	 * Gets the root debate or null if there is none yet
	 * @param pm PersistenceManager for access to the database.
	 * 
	 * @return the id of the default thesis (or null if there is none).
	 */
    public static Long getRoot(PersistenceManager pm) {
        Query query = pm.newQuery(RootDebate.class);
        query.setFilter("key == 'root'");
        @SuppressWarnings("unchecked") List<RootDebate> result = (List<RootDebate>) query.execute();
        if (result.isEmpty()) return null; else {
            return result.get(0).thesisId;
        }
    }

    /**
	 * Sets the id of the thesis to show at default.
	 * 
	 * @param thesisId the id of the default thesis.
	 * @param pm needed for access to the database
	 * @throws DatabaseException if the id was not set.
	 */
    public static void setRoot(long thesisId, PersistenceManager pm) throws DatabaseException {
        RootDebate rd = new RootDebate();
        rd.key = ROOT_KEY;
        rd.thesisId = thesisId;
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makePersistent(rd);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
                log.error("Failed to create Root-Debate as " + thesisId);
                throw new DatabaseException("Failed to create Root-Debate", DatabaseException.Cause.ROLLBACK);
            }
        }
    }
}
