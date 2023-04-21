package org.encuestame.core.cron;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reindex Job.
 * @author Picado, Juan juanATencuestame.org
 * @since Jul 8, 2010 9:49:41 PM
 * @version $Id:$
 */
public class ReIndexJob {

    /**
     * Log.
     */
    private static final Log log = LogFactory.getLog(ReIndexJob.class);

    /**
     * {@link IndexRebuilder}.
     */
    private IndexRebuilder indexRebuilder;

    /**
     * Reindex.
     */
    public void reindex() {
        this.reindexData();
    }

    /**
     * Reindex Data.
     */
    private void reindexData() {
        try {
            getIndexRebuilder().reindexEntities();
        } catch (Exception e) {
            ReIndexJob.log.error("Error Reindexing " + e.getMessage());
        }
    }

    /**
     * @return the indexRebuilder
     */
    public IndexRebuilder getIndexRebuilder() {
        return indexRebuilder;
    }

    /**
     * @param indexRebuilder the indexRebuilder to set
     */
    public void setIndexRebuilder(final IndexRebuilder indexRebuilder) {
        this.indexRebuilder = indexRebuilder;
    }
}
