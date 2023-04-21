package com.jxva.dao;

import java.util.List;

/**
 * DAO核心接口<br>
 * 相当于Hibernate中的Session, 但是:<br>
 * <pre>
 * 1. 基于泛型的,MVC调用请参考DAOHolder;
 * 2. 此接口是通过DAOPrxoy动态代理提供给用户使用的;
 * 3. 通过DAOFactory建立DAO是超轻量级的.
 * </pre>
 * <b>Usage:</b>
 * <pre>
 * DAOFactory factory=DAOFactory.getInstance(...);
 * DAO dao=factory.createDao();
 * dao.doSomething();
 * dao.close();//关闭dao,是一个优秀程序员的表现
 * </pre>
 * @author  The Jxva Framework
 * @since   1.0
 * @version 2008-11-27 10:45:05 by Jxva
 */
public interface DAO {

    public void afterAnyInvocation() throws DAOException;

    public void beforeAnyInvocation() throws DAOException;

    public Transaction beginTransaction() throws DAOException;

    public void close() throws DAOException;

    public void onException();

    public JdbcTemplate getJdbcTemplate() throws DAOException;

    public <T> int update(T entity) throws DAOException;

    public int update(String jql) throws DAOException;

    public int update(String jql, Object arg) throws DAOException;

    public int update(String jql, Object[] args) throws DAOException;

    public <T> int save(T entity) throws DAOException;

    public <T> int saveOrUpdate(T entity) throws DAOException;

    public <T> int delete(T entity) throws DAOException;

    public <T> int delete(Class<T> entityClass, Object firstPrimaryKeyValue) throws DAOException;

    public int delete(String jql) throws DAOException;

    public int delete(String jql, Object arg) throws DAOException;

    public int delete(String jql, Object[] args) throws DAOException;

    public <T> T get(Class<T> entityClass, Object firstPrimaryKeyValue) throws DAOException;

    public Object get(String jql) throws DAOException;

    public Object get(String jql, Object arg) throws DAOException;

    public Object get(String jql, Object[] args) throws DAOException;

    public Object findUnique(String jql) throws DAOException;

    public Object findUnique(String jql, Object arg) throws DAOException;

    public Object findUnique(String jql, Object[] args) throws DAOException;

    public <T> List<T> find(Class<T> entityClass) throws DAOException;

    public List find(String jql) throws DAOException;

    public List find(String jql, Object arg) throws DAOException;

    public List find(String jql, Object[] args) throws DAOException;

    public List findByNamedParam(String jql, String paramName, Object arg) throws DAOException;

    public List findByNamedParam(String jql, String[] paramNames, Object[] args) throws DAOException;

    public <T> List<T> findPageBean(Class<T> entityClass, PageBean pageBean) throws DAOException;

    public List findPageBean(String jql, PageBean pageBean) throws DAOException;

    public List findPageBean(String jql, Object arg, PageBean pageBean) throws DAOException;

    public List findPageBean(String jql, Object[] args, PageBean pageBean) throws DAOException;

    public <T> List<T> findPaginated(Class<T> entityClass, int index, int size) throws DAOException;

    public List findPaginated(String jql, int index, int size) throws DAOException;

    public List findPaginated(String jql, Object arg, int index, int size) throws DAOException;

    public List findPaginated(String jql, Object[] args, int index, int size) throws DAOException;

    public Query query(String jql) throws DAOException;
}
