package com.common.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Filter;
import org.hibernate.LockMode;
import org.hibernate.ReplicationMode;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.common.dao.IBaseDB;

public interface IBaseHibernateDao extends IBaseDB {

    public void executeByTransaction(Object[] entitys) throws DataAccessException;

    public Serializable save(Object entity) throws DataAccessException;

    public Serializable save(String entityName, Object entity) throws DataAccessException;

    public void saveOrUpdate(Object entity) throws DataAccessException;

    public void saveOrUpdate(String entityName, Object entity) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public void saveOrUpdateAll(Collection entities) throws DataAccessException;

    public void update(Object entity) throws DataAccessException;

    public void update(Object entity, LockMode lockMode) throws DataAccessException;

    public void update(String entityName, Object entity) throws DataAccessException;

    public void update(String entityName, Object entity, LockMode lockMode) throws DataAccessException;

    public int bulkUpdate(String queryString) throws DataAccessException;

    public int bulkUpdate(String queryString, Object value) throws DataAccessException;

    public int bulkUpdate(String queryString, Object[] values) throws DataAccessException;

    public void clear() throws DataAccessException;

    @SuppressWarnings("unchecked")
    public void closeIterator(Iterator it) throws DataAccessException;

    public boolean contains(Object entity) throws DataAccessException;

    public void delete(Object entity) throws DataAccessException;

    public void delete(Object entity, LockMode lockMode) throws DataAccessException;

    public void delete(String entityName, Object entity) throws DataAccessException;

    public void delete(String entityName, Object entity, LockMode lockMode) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public void deleteAll(Collection entities) throws DataAccessException;

    public void flush() throws DataAccessException;

    public Filter enableFilter(String filterName) throws IllegalStateException;

    public void evict(Object entity) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List executeFind(HibernateCallback action) throws DataAccessException;

    public Object executeWithNativeSession(HibernateCallback action) throws DataAccessException;

    public Object executeWithNewSession(HibernateCallback action) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List find(String queryString) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List find(String queryString, Object value) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List find(String queryString, Object[] values) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedParam(String queryString, String paramName, Object value) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedParam(String queryString, String[] paramNames, Object[] values) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByValueBean(String queryString, Object valueBean) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedQuery(String queryName) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedQuery(String queryName, Object value) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedQuery(String queryName, Object[] values) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedQueryAndNamedParam(String queryName, String paramName, Object value) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByNamedQueryAndValueBean(String queryName, Object valueBean) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByCriteria(DetachedCriteria criteria) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByExample(Object exampleEntity) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByExample(String entityName, Object exampleEntity) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByExample(Object exampleEntity, int firstResult, int maxResults) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List findByExample(String entityName, Object exampleEntity, int firstResult, int maxResults) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public Iterator iterate(String queryString) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public Iterator iterate(String queryString, Object value) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public Iterator iterate(String queryString, Object[] values) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public Object get(Class entityClass, Serializable id) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public Object get(Class entityClass, Serializable id, LockMode lockMode) throws DataAccessException;

    public Object get(String entityName, Serializable id) throws DataAccessException;

    public Object get(String entityName, Serializable id, LockMode lockMode) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public Object load(Class entityClass, Serializable id) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public Object load(Class entityClass, Serializable id, LockMode lockMode) throws DataAccessException;

    public Object load(String entityName, Serializable id) throws DataAccessException;

    public Object load(String entityName, Serializable id, LockMode lockMode) throws DataAccessException;

    @SuppressWarnings("unchecked")
    public List loadAll(Class entityClass) throws DataAccessException;

    public void load(Object entity, Serializable id) throws DataAccessException;

    public void refresh(Object entity) throws DataAccessException;

    public void refresh(Object entity, LockMode lockMode) throws DataAccessException;

    public int getFetchSize();

    public void setMaxResults(int maxResults);

    public int getMaxResults();

    public String getQueryCacheRegion();

    public void initialize(Object proxy) throws DataAccessException;

    public boolean isAllowCreate();

    public boolean isAlwaysUseNewSession();

    public boolean isCacheQueries();

    public boolean isCheckWriteOperations();

    public boolean isExposeNativeSession();

    public void lock(Object entity, LockMode lockMode) throws DataAccessException;

    public void lock(String entityName, Object entity, LockMode lockMode) throws DataAccessException;

    public Object merge(Object entity) throws DataAccessException;

    public Object merge(String entityName, Object entity) throws DataAccessException;

    public void persist(Object entity) throws DataAccessException;

    public void persist(String entityName, Object entity) throws DataAccessException;

    public void replicate(Object entity, ReplicationMode replicationMode) throws DataAccessException;

    public void replicate(String entityName, Object entity, ReplicationMode replicationMode) throws DataAccessException;

    public void setAllowCreate(boolean allowCreate);

    public void setAlwaysUseNewSession(boolean alwaysUseNewSession);

    public void setCacheQueries(boolean cacheQueries);

    public void setCheckWriteOperations(boolean checkWriteOperations);

    public void setExposeNativeSession(boolean exposeNativeSession);

    public void setQueryCacheRegion(String queryCacheRegion);

    public Object executeHibernateCallback(HibernateCallback hibernateCallback) throws DataAccessException;
}
