package com.icteam.fiji.test;

import java.io.Serializable;
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

public class MockSession implements Session {

    public Transaction beginTransaction() throws HibernateException {
        return null;
    }

    public void cancelQuery() throws HibernateException {
    }

    public void clear() {
    }

    public Connection close() throws HibernateException {
        return null;
    }

    public Connection connection() throws HibernateException {
        return null;
    }

    public boolean contains(Object object) {
        return false;
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        return null;
    }

    public Criteria createCriteria(Class persistentClass) {
        return null;
    }

    public Criteria createCriteria(String entityName, String alias) {
        return null;
    }

    public Criteria createCriteria(String entityName) {
        return null;
    }

    public Query createFilter(Object collection, String queryString) throws HibernateException {
        return null;
    }

    public Query createQuery(String queryString) throws HibernateException {
        return new MockQuery(queryString);
    }

    public SQLQuery createSQLQuery(String queryString) throws HibernateException {
        return null;
    }

    public void delete(Object object) throws HibernateException {
    }

    public void delete(String entityName, Object object) throws HibernateException {
    }

    public void disableFilter(String filterName) {
    }

    public Connection disconnect() throws HibernateException {
        return null;
    }

    public Filter enableFilter(String filterName) {
        return null;
    }

    public void evict(Object object) throws HibernateException {
    }

    public void flush() throws HibernateException {
    }

    public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
        return null;
    }

    public Object get(Class clazz, Serializable id) throws HibernateException {
        return null;
    }

    public Object get(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return null;
    }

    public Object get(String entityName, Serializable id) throws HibernateException {
        return null;
    }

    public CacheMode getCacheMode() {
        return null;
    }

    public LockMode getCurrentLockMode(Object object) throws HibernateException {
        return null;
    }

    public Filter getEnabledFilter(String filterName) {
        return null;
    }

    public EntityMode getEntityMode() {
        return null;
    }

    public String getEntityName(Object object) throws HibernateException {
        return null;
    }

    public FlushMode getFlushMode() {
        return null;
    }

    public Serializable getIdentifier(Object object) throws HibernateException {
        return null;
    }

    public Query getNamedQuery(String queryName) throws HibernateException {
        return null;
    }

    public Session getSession(EntityMode entityMode) {
        return null;
    }

    public SessionFactory getSessionFactory() {
        return null;
    }

    public SessionStatistics getStatistics() {
        return null;
    }

    public Transaction getTransaction() {
        return null;
    }

    public boolean isConnected() {
        return false;
    }

    public boolean isDirty() throws HibernateException {
        return false;
    }

    public boolean isOpen() {
        return false;
    }

    public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
        return null;
    }

    public Object load(Class theClass, Serializable id) throws HibernateException {
        return null;
    }

    public void load(Object object, Serializable id) throws HibernateException {
    }

    public Object load(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return null;
    }

    public Object load(String entityName, Serializable id) throws HibernateException {
        return null;
    }

    public void lock(Object object, LockMode lockMode) throws HibernateException {
    }

    public void lock(String entityName, Object object, LockMode lockMode) throws HibernateException {
    }

    public Object merge(Object object) throws HibernateException {
        return null;
    }

    public Object merge(String entityName, Object object) throws HibernateException {
        return null;
    }

    public void persist(Object object) throws HibernateException {
    }

    public void persist(String entityName, Object object) throws HibernateException {
    }

    public void reconnect() throws HibernateException {
    }

    public void reconnect(Connection connection) throws HibernateException {
    }

    public void refresh(Object object, LockMode lockMode) throws HibernateException {
    }

    public void refresh(Object object) throws HibernateException {
    }

    public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) throws HibernateException {
    }

    public Serializable save(Object object) throws HibernateException {
        return null;
    }

    public Serializable save(String entityName, Object object) throws HibernateException {
        return null;
    }

    public void saveOrUpdate(Object object) throws HibernateException {
    }

    public void saveOrUpdate(String entityName, Object object) throws HibernateException {
    }

    public void setCacheMode(CacheMode cacheMode) {
    }

    public void setFlushMode(FlushMode flushMode) {
    }

    public void setReadOnly(Object entity, boolean readOnly) {
    }

    public void update(Object object) throws HibernateException {
    }

    public void update(String entityName, Object object) throws HibernateException {
    }
}
