package com.liferay.portal.spring.hibernate;

import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import java.io.Serializable;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.sql.Connection;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.SessionStatistics;

/**
 * <a href="LiferaySession.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * See http://support.liferay.com/browse/LEP-2996.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class LiferaySession implements Session {

    public LiferaySession(Session session) {
        _session = session;
    }

    public Transaction beginTransaction() throws HibernateException {
        return _session.beginTransaction();
    }

    public void cancelQuery() throws HibernateException {
        _session.cancelQuery();
    }

    public void clear() {
        _session.clear();
    }

    public Connection close() throws HibernateException {
        return _session.close();
    }

    /**
	 * @deprecated
	 */
    public Connection connection() throws HibernateException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader portalClassLoader = PortalClassLoaderUtil.getClassLoader();
            Thread.currentThread().setContextClassLoader(portalClassLoader);
            Connection connection = _session.connection();
            return connection;
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    public boolean contains(Object object) {
        return _session.contains(object);
    }

    public Criteria createCriteria(Class persistentClass) {
        return _session.createCriteria(persistentClass);
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        return _session.createCriteria(persistentClass, alias);
    }

    public Criteria createCriteria(String entityName) {
        return _session.createCriteria(entityName);
    }

    public Criteria createCriteria(String entityName, String alias) {
        return _session.createCriteria(entityName, alias);
    }

    public Query createFilter(Object collection, String queryString) throws HibernateException {
        return _session.createFilter(collection, queryString);
    }

    public Query createQuery(String queryString) throws HibernateException {
        return _session.createQuery(queryString);
    }

    public SQLQuery createSQLQuery(String queryString) throws HibernateException {
        return _session.createSQLQuery(queryString);
    }

    public void delete(Object object) throws HibernateException {
        _session.delete(object);
    }

    public void delete(String entityName, Object object) throws HibernateException {
        _session.delete(entityName, object);
    }

    public void disableFilter(String filterName) {
        _session.disableFilter(filterName);
    }

    public Connection disconnect() throws HibernateException {
        return _session.disconnect();
    }

    public Filter enableFilter(String filterName) {
        return _session.enableFilter(filterName);
    }

    public void evict(Object object) throws HibernateException {
        _session.evict(object);
    }

    public void flush() throws HibernateException {
        _session.flush();
    }

    public Object get(Class clazz, Serializable id) throws HibernateException {
        return _session.get(clazz, id);
    }

    public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
        return _session.get(clazz, id, lockMode);
    }

    public Object get(String entityName, Serializable id) throws HibernateException {
        return _session.get(entityName, id);
    }

    public Object get(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return _session.get(entityName, id, lockMode);
    }

    public CacheMode getCacheMode() {
        return _session.getCacheMode();
    }

    public LockMode getCurrentLockMode(Object object) throws HibernateException {
        return _session.getCurrentLockMode(object);
    }

    public Filter getEnabledFilter(String filterName) {
        return _session.getEnabledFilter(filterName);
    }

    public EntityMode getEntityMode() {
        return _session.getEntityMode();
    }

    public String getEntityName(Object object) throws HibernateException {
        return _session.getEntityName(object);
    }

    public FlushMode getFlushMode() {
        return _session.getFlushMode();
    }

    public Serializable getIdentifier(Object object) throws HibernateException {
        return _session.getIdentifier(object);
    }

    public Query getNamedQuery(String queryName) throws HibernateException {
        return _session.getNamedQuery(queryName);
    }

    public Session getSession(EntityMode entityMode) {
        return _session.getSession(entityMode);
    }

    public SessionFactory getSessionFactory() {
        return _session.getSessionFactory();
    }

    public SessionStatistics getStatistics() {
        return _session.getStatistics();
    }

    public Transaction getTransaction() {
        return _session.getTransaction();
    }

    public boolean isConnected() {
        return _session.isConnected();
    }

    public boolean isDirty() throws HibernateException {
        return _session.isDirty();
    }

    public boolean isOpen() {
        return _session.isOpen();
    }

    public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
        return _session.load(theClass, id, lockMode);
    }

    public Object load(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return _session.load(entityName, id, lockMode);
    }

    public Object load(Class theClass, Serializable id) throws HibernateException {
        return _session.load(theClass, id);
    }

    public Object load(String entityName, Serializable id) throws HibernateException {
        return _session.load(entityName, id);
    }

    public void load(Object object, Serializable id) throws HibernateException {
        _session.load(object, id);
    }

    public void lock(Object object, LockMode lockMode) throws HibernateException {
        _session.lock(object, lockMode);
    }

    public void lock(String entityName, Object object, LockMode lockMode) throws HibernateException {
        _session.lock(entityName, object, lockMode);
    }

    public Object merge(Object object) throws HibernateException {
        return _session.merge(object);
    }

    public Object merge(String entityName, Object object) throws HibernateException {
        return _session.merge(entityName, object);
    }

    public void persist(Object object) throws HibernateException {
        _session.persist(object);
    }

    public void persist(String entityName, Object object) throws HibernateException {
        _session.persist(entityName, object);
    }

    /**
	 * @deprecated
	 */
    public void reconnect() throws HibernateException {
        _session.reconnect();
    }

    public void reconnect(Connection connection) throws HibernateException {
        _session.reconnect(connection);
    }

    public void refresh(Object object) throws HibernateException {
        _session.refresh(object);
    }

    public void refresh(Object object, LockMode lockMode) throws HibernateException {
        _session.refresh(object, lockMode);
    }

    public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
        _session.replicate(object, replicationMode);
    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) throws HibernateException {
        _session.replicate(entityName, object, replicationMode);
    }

    public Serializable save(Object object) throws HibernateException {
        return _session.save(object);
    }

    public Serializable save(String entityName, Object object) throws HibernateException {
        return _session.save(entityName, object);
    }

    public void saveOrUpdate(Object object) throws HibernateException {
        _session.saveOrUpdate(object);
    }

    public void saveOrUpdate(String entityName, Object object) throws HibernateException {
        _session.saveOrUpdate(entityName, object);
    }

    public void setCacheMode(CacheMode cacheMode) {
        _session.setCacheMode(cacheMode);
    }

    public void setFlushMode(FlushMode flushMode) {
        _session.setFlushMode(flushMode);
    }

    public void setReadOnly(Object entity, boolean readOnly) {
        _session.setReadOnly(entity, readOnly);
    }

    public void update(Object object) throws HibernateException {
        _session.update(object);
    }

    public void update(String entityName, Object object) throws HibernateException {
        _session.update(entityName, object);
    }

    private Session _session;
}
