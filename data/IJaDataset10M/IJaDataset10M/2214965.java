package org.evolizer.base.versioning.dao;

import java.util.List;
import org.evolizer.base.dao.GenericDao;
import org.evolizer.base.dao.QueryResult;
import org.evolizer.base.model.Period;
import org.evolizer.base.versioning.model.Revision;

/**
 * Provides access to persistent storage for class
 * org.evolizer.base.versioning.model.Revision
 * 
 * @author Jacek Ratzinger
 *
 */
public interface RevisionDao extends GenericDao {

    /**
     * Retrieves all persistent revisions
     * 
     * @return <code>List</code> with all persistent revisions.
     */
    public QueryResult loadAll();

    /**
     * Retrieves all persistent revisions
     * ordered by creation time (1st) and author (2nd).
     * 
     * @return <code>QueryResult</code> with all persistent revisions ordered by creation time and author.
     */
    public QueryResult<Revision> loadAllOrderedByCreationtimeAndAuthor();

    /**
     * Query all Revisions in the given time period.
     * @param period The time frame to 
     * @return
     */
    public List<Revision> findInPeriod(Period period);

    /**
     * Retrive all distinct cluster names;
     * @return Names of clusters from commit message
     */
    public List<String> findClusterNames();
}
