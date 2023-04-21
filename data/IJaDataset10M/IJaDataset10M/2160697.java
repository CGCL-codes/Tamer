package com.eptica.ias.models.accounts.accounttype;

import java.util.*;
import com.eptica.ias.util.*;

/**
 * A AccountTypeManager provides a higher level of data manipulation than the AccountTypeDAO<br>
 * As a developper you should be using only managers.<br>
 * Please note that if you did not set a cacheRegion, cacheEnabled and maxResultsAuthorized in searchTemplate, these values will be set by the DAO implementation.
 */
public interface AccountTypeManager {

    /**
     * Saves or update the passed AccountTypeModel instance.
     *
     * @param accountType The AccountTypeModel to save or update
     */
    public void save(final AccountTypeModel accountType);

    /**
     * Saves or update the passed collection of AccountTypeModel instances.
     *
     * @param accountTypes The accountTypes collection to save
     */
    public void save(final Collection<AccountTypeModel> accountTypes);

    /**
     * Return the persistent instance of AccountTypeModel class with the given primary key value, 
     * or null if there is no such persistent instance. 
     *
     * @param accountTypeId the primary key value
     * @return the corresponding AccountTypeModel persistent instance or null
     */
    public AccountTypeModel get(final Integer accountTypeId);

    /**
     * Delete a AccountTypeModel using the primary key value
     *
     * @param accountTypeId the primary key value
     */
    public void delete(final Integer accountTypeId);

    /**
     * Return the persistent instance of AccountTypeModel class given the primary key of the passed AccountTypeModel instance, 
     * or null if there is no such persistent instance. 
     *
     * @param accountType the AccountTypeModel instance with its primary key set
     * @return the corresponding AccountTypeModel persistant instance or null
     */
    public AccountTypeModel get(final AccountTypeModel accountType);

    /**
     * Delete a AccountTypeModel given the primary key value of the passed isntance.
     *
     * @param accountType The accountType to delete
     */
    public void delete(final AccountTypeModel accountType);

    /**
     * Delete a collection of AccountTypeModel given the primary key value of the passed isntances.
     *
     * @param accountTypes The collection of AccountTypeModel to delete
     */
    public void delete(final Collection<AccountTypeModel> accountTypes);

    /**
     * Return the unique persistent instance of AccountTypeModel matching exactly the given sample,
     * or throw an exception if zero or more than one instance match the sample.
     * Each non-null property of the passed sample is used and must exactly match the desired instance.
     *
     * @param accountType the sample to match exactly
     * @return the matching AccountTypeModel persistant instance
     * @throws an IllegalStateException if zero or more than one instance match the sample
     */
    public AccountTypeModel findUnique(final AccountTypeModel accountType);

    /**
     * Return the unique persistent instance of AccountTypeModel matching exactly the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     * Each non-null property of the passed sample is used and must exactly match the desired instance.
     *
     * @param accountType the sample to match exactly
     * @return the matching AccountTypeModel persistant instance or null
     * @throws an IllegalStateException if more than one instance match the sample
     */
    public AccountTypeModel findUniqueOrNone(final AccountTypeModel accountType);

    /**
     * Return the persistent instances of AccountTypeModel matching exactly the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     *
     * @param accountType the sample to match exactly
     */
    public Collection<AccountTypeModel> find(final AccountTypeModel accountType);

    /**
     * Return the persistent instances of AccountTypeModel matching the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     * 
     * @param accountType the sample to match
     * @param searchTemplate the specific criteria such as SearchMode, orderBy, etc.
     */
    public Collection<AccountTypeModel> find(final AccountTypeModel accountType, SearchTemplate searchTemplate);

    /**
     * Return the number of persistent instances of AccountTypeModel matching exactly the given sample.
     * Each non-null property of the passed sample is used and compared exactly.
     * 
     * @param accountType the sample to match
     * @return the resulting count
     */
    public int findCount(final AccountTypeModel accountType);

    /**
     * Return the number of persistent instances of AccountTypeModel matching the given sample.
     * 
     * @param accountType the model to match
     * @param searchTemplate the specific criteria such as SearchMode
     * @return the resulting count
     */
    public int findCount(final AccountTypeModel accountType, final SearchTemplate searchTemplate);
}
