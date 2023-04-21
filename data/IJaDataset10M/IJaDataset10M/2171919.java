package com.eptica.ias.models.requests.requestevent;

import java.util.Collection;
import com.eptica.ias.util.SearchTemplate;

/**
 * <a href="http://en.wikipedia.org/wiki/Data_Access_Object">DAO</a> interface for the RequestEventModel table.<br>
 * This pattern is also called repository.<br>
 * As a developper you should be only using the RequestEventManager.<br>
 * If you want to create a new binding to a data source, please implement all these methods, please note that you need to handle the SearchTemplate pattern.<br>
 * Please note that if you did not set a cacheRegion, cacheEnabled and maxResultsAuthorized in searchTemplate, these values will be set.
 */
public interface RequestEventDAO {

    /**
     * Return the persistent instance of RequestEventModel class with the primary key of the passed RequestEventModel instance, 
     * or null if there is no such persistent instance. 
     *
     * @param requestEvent the RequestEventModel instance with its primary key set
     * @return the corresponding RequestEventModel persistant instance or null
     */
    public RequestEventModel get(final RequestEventModel requestEvent);

    /**
     * Save or update a collection of RequestEventModel instances.
     *
     * @param requestEvents The requestEvents collection to save
     */
    public void save(final Collection<RequestEventModel> requestEvents);

    /**
     * Delete a collection of RequestEventModel.
     *
     * @param requestEvents The requestEvents collection to delete
     */
    public void delete(final Collection<RequestEventModel> requestEvents);

    /**
     * Return the persistent instances of RequestEventModel matching the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     * 
     * @param requestEvent the sample to match
     * @param searchTemplate the specific criteria such as SearchMode, orderBy, etc.
     */
    public Collection<RequestEventModel> find(final RequestEventModel requestEvent, final SearchTemplate searchTemplate);

    /**
     * Return the number of persistent instances of RequestEventModel matching the given sample.
     * 
     * @param requestEvent the model to match
     * @param searchTemplate the specific criteria such as SearchMode
     * @return the resulting count
     */
    public int findCount(final RequestEventModel requestEvent, final SearchTemplate searchTemplate);
}
