package com.genia.toolbox.persistence.dao.impl;

import com.genia.toolbox.basics.exception.technical.TechnicalException;
import com.genia.toolbox.basics.manager.ExceptionManager;
import com.genia.toolbox.persistence.bean.Persistable;
import com.genia.toolbox.persistence.dao.AbstractDAO;
import com.genia.toolbox.persistence.dao.SpecificDAO;
import com.genia.toolbox.persistence.exception.ObjectIsNotPersistableException;
import com.genia.toolbox.persistence.exception.PersistenceException;

/**
 * implementation of {@link SpecificDAO}.
 * 
 * @param <TYPE>
 *          the type of the publicly seen object
 */
public class AbstractSpecificDAO<TYPE> extends AbstractDAO implements SpecificDAO<TYPE> {

    /**
   * the {@link ExceptionManager} to use.
   */
    private ExceptionManager exceptionManager;

    /**
   * the class of the real type of the objects handled by this DAO.
   */
    private final transient Class<? extends TYPE> persistentTypeClass;

    /**
   * constructor.
   * 
   * @param persistentTypeClass
   *          the class of the real type of the objects handled by this DAO
   */
    protected AbstractSpecificDAO(Class<? extends TYPE> persistentTypeClass) {
        this.persistentTypeClass = persistentTypeClass;
    }

    /**
   * delete a persistent object handled by this DAO.
   * 
   * @param object
   *          the object to delete
   * @throws PersistenceException
   *           if an error occured
   * @see com.genia.toolbox.persistence.dao.SpecificDAO#delete(java.lang.Object)
   */
    public void delete(TYPE object) throws PersistenceException {
        if (!(object instanceof Persistable<?>)) {
            throw new ObjectIsNotPersistableException();
        }
        super.delete((Persistable<?>) object);
    }

    /**
   * returns a new instance of the object handled by this DAO.
   * 
   * @return a new instance of the object handled by this DAO
   * @throws TechnicalException
   *           if an error occured
   * @see com.genia.toolbox.persistence.dao.SpecificDAO#newObject()
   */
    public TYPE newObject() throws TechnicalException {
        try {
            return persistentTypeClass.newInstance();
        } catch (Exception e) {
            throw getExceptionManager().convertException(e);
        }
    }

    /**
   * save a not yet persistent object handled by this DAO.
   * 
   * @param object
   *          the object to save
   * @throws PersistenceException
   *           if an error occured
   * @see com.genia.toolbox.persistence.dao.SpecificDAO#save(java.lang.Object)
   */
    public void save(TYPE object) throws PersistenceException {
        if (!(object instanceof Persistable<?>)) {
            throw new ObjectIsNotPersistableException();
        }
        super.save((Persistable<?>) object);
    }

    /**
   * update a persistent object handled by this DAO.
   * 
   * @param object
   *          the object to update
   * @throws PersistenceException
   *           if an error occured
   * @see com.genia.toolbox.persistence.dao.SpecificDAO#update(java.lang.Object)
   */
    public void update(TYPE object) throws PersistenceException {
        if (!(object instanceof Persistable<?>)) {
            throw new ObjectIsNotPersistableException();
        }
        super.update((Persistable<?>) object);
    }

    /**
   * getter for the exceptionManager property.
   * 
   * @return the exceptionManager
   */
    public ExceptionManager getExceptionManager() {
        return exceptionManager;
    }

    /**
   * setter for the exceptionManager property.
   * 
   * @param exceptionManager
   *          the exceptionManager to set
   */
    public void setExceptionManager(ExceptionManager exceptionManager) {
        this.exceptionManager = exceptionManager;
    }
}
