package com.liferay.portal.kernel.dao.orm;

import java.io.Serializable;
import java.sql.Connection;

/**
 * <a href="Session.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface Session {

    public void clear() throws ORMException;

    public Connection close() throws ORMException;

    public boolean contains(Object object) throws ORMException;

    public Query createQuery(String queryString) throws ORMException;

    public SQLQuery createSQLQuery(String queryString) throws ORMException;

    public void delete(Object object) throws ORMException;

    public void evict(Object object) throws ORMException;

    public void flush() throws ORMException;

    public Object get(Class clazz, Serializable id) throws ORMException;

    public Object get(Class clazz, Serializable id, LockMode lockMode) throws ORMException;

    public Object load(Class clazz, Serializable id) throws ORMException;

    public Object merge(Object object) throws ORMException;

    public Serializable save(Object object) throws ORMException;

    public void saveOrUpdate(Object object) throws ORMException;
}
