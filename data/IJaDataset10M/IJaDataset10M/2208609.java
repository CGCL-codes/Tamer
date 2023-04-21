package org.jbpcc.domain.dao;

import java.io.Serializable;

public interface BaseDAO<T, PK extends Serializable> {

    /** Persist the newInstance object into database */
    T create(T newInstance) throws DAOInsertException;

    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key
     *
     * @param id
     * @return
     * @throws org.jbpcc.domain.dao.DAOFinderException
     */
    T find(PK id) throws DAOFinderException;

    /** Save changes made to a persistent object.  */
    void update(T transientObject) throws DAOUpdateException;

    /** Remove an object from persistent storage in the database */
    void delete(T persistentObject) throws DAODeleteException;
}
