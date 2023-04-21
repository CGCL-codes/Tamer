package com.eptica.ias.models.requests.draftrequest;

import java.util.*;
import com.eptica.ias.util.*;

/**
 * A DraftRequestManager provides a higher level of data manipulation than the DraftRequestDAO<br>
 * As a developper you should be using only managers.<br>
 * Please note that if you did not set a cacheRegion, cacheEnabled and maxResultsAuthorized in searchTemplate, these values will be set by the DAO implementation.
 */
public interface DraftRequestManager {

    /**
     * Saves or update the passed DraftRequestModel instance.
     *
     * @param draftRequest The DraftRequestModel to save or update
     */
    public void save(final DraftRequestModel draftRequest);

    /**
     * Saves or update the passed collection of DraftRequestModel instances.
     *
     * @param draftRequests The draftRequests collection to save
     */
    public void save(final Collection<DraftRequestModel> draftRequests);

    /**
     * Return the persistent instance of DraftRequestModel class with the given primary key value, 
     * or null if there is no such persistent instance. 
     *
     * @param draftRequestId the primary key value
     * @return the corresponding DraftRequestModel persistent instance or null
     */
    public DraftRequestModel get(final String draftRequestId);

    /**
     * Delete a DraftRequestModel using the primary key value
     *
     * @param draftRequestId the primary key value
     */
    public void delete(final String draftRequestId);

    /**
     * Return the persistent instance of DraftRequestModel class given the primary key of the passed DraftRequestModel instance, 
     * or null if there is no such persistent instance. 
     *
     * @param draftRequest the DraftRequestModel instance with its primary key set
     * @return the corresponding DraftRequestModel persistant instance or null
     */
    public DraftRequestModel get(final DraftRequestModel draftRequest);

    /**
     * Delete a DraftRequestModel given the primary key value of the passed isntance.
     *
     * @param draftRequest The draftRequest to delete
     */
    public void delete(final DraftRequestModel draftRequest);

    /**
     * Delete a collection of DraftRequestModel given the primary key value of the passed isntances.
     *
     * @param draftRequests The collection of DraftRequestModel to delete
     */
    public void delete(final Collection<DraftRequestModel> draftRequests);

    /**
     * Return the unique persistent instance of DraftRequestModel matching exactly the given sample,
     * or throw an exception if zero or more than one instance match the sample.
     * Each non-null property of the passed sample is used and must exactly match the desired instance.
     *
     * @param draftRequest the sample to match exactly
     * @return the matching DraftRequestModel persistant instance
     * @throws an IllegalStateException if zero or more than one instance match the sample
     */
    public DraftRequestModel findUnique(final DraftRequestModel draftRequest);

    /**
     * Return the unique persistent instance of DraftRequestModel matching exactly the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     * Each non-null property of the passed sample is used and must exactly match the desired instance.
     *
     * @param draftRequest the sample to match exactly
     * @return the matching DraftRequestModel persistant instance or null
     * @throws an IllegalStateException if more than one instance match the sample
     */
    public DraftRequestModel findUniqueOrNone(final DraftRequestModel draftRequest);

    /**
     * Return the persistent instances of DraftRequestModel matching exactly the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     *
     * @param draftRequest the sample to match exactly
     */
    public Collection<DraftRequestModel> find(final DraftRequestModel draftRequest);

    /**
     * Return the persistent instances of DraftRequestModel matching the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     * 
     * @param draftRequest the sample to match
     * @param searchTemplate the specific criteria such as SearchMode, orderBy, etc.
     */
    public Collection<DraftRequestModel> find(final DraftRequestModel draftRequest, SearchTemplate searchTemplate);

    /**
     * Return the number of persistent instances of DraftRequestModel matching exactly the given sample.
     * Each non-null property of the passed sample is used and compared exactly.
     * 
     * @param draftRequest the sample to match
     * @return the resulting count
     */
    public int findCount(final DraftRequestModel draftRequest);

    /**
     * Return the number of persistent instances of DraftRequestModel matching the given sample.
     * 
     * @param draftRequest the model to match
     * @param searchTemplate the specific criteria such as SearchMode
     * @return the resulting count
     */
    public int findCount(final DraftRequestModel draftRequest, final SearchTemplate searchTemplate);
}
