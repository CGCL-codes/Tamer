package org.oddtake.server.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.oddtake.server.hibernate.HibernateDataObject;

/**
 * @author Onyeje Bose (digi9ten@yahoo.com)
 */
public interface Dao<T, ID extends Serializable> {

    List sqlFind(String queryString, Object[] values);

    List find(String queryString);

    List find(String queryString, Object value);

    List find(String queryString, Object[] values);

    /**
     * This method will find an object of type T by ID
     *
     * @param id
     * @return Entity
     */
    T findById(ID id);

    /**
     * This method will find all objects of type T
     *
     * @return List of entities
     */
    List<T> findAll();

    List<T> findAllSorted(String sortField);

    T loadById(ID id);

    /**
     * This method will save an object of type T
     *
     * @param entity
     */
    ID save(T entity);

    /**
     * This method will update an object of type T
     *
     * @param entity
     */
    void update(T entity);

    void saveOrUpdate(T entity);

    void merge(T entity);

    /**
     * This method will delete an object of type T
     * <p/>
     * Note: since this is may be a change is state, rather than an actual removal from the datasource, this should
     * be handled on a per-entity basis, and not in a generic fashion.
     *
     * @param id
     */
    void delete(ID id);

    void refresh(T entity);

    /**
     * Convienence method required for testing purposes.
     */
    void flush();

    void evict(T entity);

    Criteria createCriteria(Class targetCls);

    Query createQuery(String query);

    void lock(HibernateDataObject dataObject, LockMode lockMode);
}
