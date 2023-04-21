package com.express.dao;

import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * Trivial Generic base class for Dao.
 * @author Adam Boas
 *
 * Created on Oct 5, 2007
 */
public interface GenericDao<T> {

    /**
    * Looks up and returns the object of Generic type T identified by the primary key provided
    * @param id the primary key of the object to find.
    * @return the Object of type T uniquely identified by the id.
    * @throws ObjectRetrievalFailureException if their is no domain object with the id provided.
    */
    T findById(Long id);

    /**
    * Generic method to save an object.
    * @param object the object to save
    */
    void save(T object);

    /**
    * Generic method to delete an object based on class and id
    * @param id the identifier (primary key) of the object to remove
    */
    void remove(Long id);
}
