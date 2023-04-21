package com.divosa.eformulieren.core.service;

import java.util.List;
import com.divosa.eformulieren.util.exception.DivosaUtilException;
import com.divosa.security.exception.AuthenticationException;
import com.divosa.security.exception.ObjectNotFoundException;
import com.divosa.security.exception.RepositoryLayerException;

/**
 * This interface is the base (super) interface for all services. It delivers methods for the most high level actions, like
 * saving and loading of objects.
 * 
 * @author Bart Ottenkamp
 */
public interface BaseService {

    /**
     * Load all objects with the specified class from the repository.
     * 
     * @param persistenceClass the Class of the object to search for
     * @return a list of all objects found
     */
    public List<?> loadAll(final Class persistenceClass);

    /**
     * Get an object with the specified class and id from the repository.
     * 
     * @param persistenceClass the Class of the object to search for
     * @param id the id of the object to search for
     * @return the object found
     * @throws ObjectNotFoundException the Exception thrown if the object cannot be found
     */
    public Object getPersitentObject(final Class persistenceClass, final Long id) throws ObjectNotFoundException, AuthenticationException;

    /**
     * Save or update the specified object to the repository.
     * 
     * @param obj the object to save or update
     * @throws RepositoryLayerException the Exception thrown if saving or updating goes wrong. This Exception mostly wraps
     * the actual Exception, which is a Hibernate (Runtime-) exception
     */
    void saveOrUpdate(Object obj) throws RepositoryLayerException, AuthenticationException;
}
