package com.plexobject.docusearch.persistence;

import com.plexobject.docusearch.index.IndexPolicy;
import com.plexobject.docusearch.query.LookupPolicy;
import com.plexobject.docusearch.query.QueryPolicy;

/**
 * 
 * @author Shahzad Bhatti
 * 
 */
public interface ConfigurationRepository {

    /**
     * 
     * @param id
     * @param policy
     * @return
     * @throws PersistenceException
     */
    public IndexPolicy saveIndexPolicy(final String id, final IndexPolicy policy) throws PersistenceException;

    /**
     * 
     * @param id
     * @return
     * @throws PersistenceException
     */
    public IndexPolicy getIndexPolicy(final String id) throws PersistenceException;

    /**
     * 
     * @param id
     * @param policy
     * @return
     * @throws PersistenceException
     */
    public LookupPolicy saveLookupPolicy(final String id, final LookupPolicy policy) throws PersistenceException;

    /**
     * 
     * @param id
     * @return
     * @throws PersistenceException
     */
    public LookupPolicy getLookupPolicy(final String id) throws PersistenceException;

    /**
     * 
     * @param id
     * @param policy
     * @return
     * @throws PersistenceException
     */
    public QueryPolicy saveQueryPolicy(final String id, final QueryPolicy policy) throws PersistenceException;

    /**
     * 
     * @param id
     * @return
     * @throws PersistenceException
     */
    public QueryPolicy getQueryPolicy(final String id) throws PersistenceException;
}
