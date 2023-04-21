package uk.ac.ebi.intact.application.search3.searchEngine.business;

import uk.ac.ebi.intact.business.IntactException;
import java.util.Map;

/**
 * Defines the requirements for a Search Engine.
 *
 * @author Anja Friedrichsen
 * @version $Id: SearchEngine.java 4573 2006-02-02 16:13:50Z skerrien $
 */
public interface SearchEngine {

    /**
     * Find an object by IQL.
     *
     * @param iqlQuery        the query.
     * @param numberOfResults the number of results
     *
     * @return a Map giving access to the object found.
     *
     * @throws IntactException
     */
    public Map findObjectByIQL(final String iqlQuery, final int numberOfResults) throws IntactException;

    /**
     * Find an object by IQL.
     *
     * @param iqlQuery the query.
     *
     * @return a Map giving access to the object found.
     *
     * @throws IntactException
     */
    public Map findObjectByIQL(final String iqlQuery) throws IntactException;

    /**
     * Returns the result value.
     *
     * @param searchKeys a Map object
     *
     * @return a map give access to the results.
     *
     * @throws IntactException ...
     */
    public Map getResult(Map searchKeys) throws IntactException;
}
