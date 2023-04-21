package com.kiasolutions.common.dalc.jpa;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Interface genérica que define un servicio de acceso a datos basado en JPA. <br/>
 * Donde:<br/>
 * <li>K - Tipo de la Clave Primaria</li>
 * <li>T - Tipo de la entidad (POJO) marcado con <code>@Entity</code></li>
 * @param <K>
 * @param <T>
 * @author Rolando Steep Quezada Martínez <rquezada@kiasolutions.com>
 * @version 1.0
 * @see com.kiasolutions.common.dalc.jpa.jta.JtaDao
 * @see javax.persistence.Query
 * @see com.kiasolutions.common.dalc.jpa.NotFoundException
 * @see com.kiasolutions.common.dalc.jpa.BadUpdateException
 * @since JDK 1.5
 * @since Java Persistence 2.0
 */
public interface DaoServices<K, T> {

    /**
     *
     */
    public static final String SELECT_TEMPLATE = "SELECT obj FROM %1$s AS obj";

    /**
     *
     */
    public static final String COUNT_TEMPLATE = "SELECT COUNT(obj) FROM %1$s AS obj";

    /** 
     * @param type 
     * @return 
     * @throws NotFoundException
     * @see jta.JtaDao#findAll(Class type)
     */
    public List<T> findAll(Class type) throws NotFoundException;

    /** 
     * @param jpql 
     * @return 
     * @throws NotFoundException
     * @see jta.JtaDao#find(String jpql)
     */
    public List<T> find(String jpql) throws NotFoundException;

    /** 
     * @param type 
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#countAll(Class type)
     */
    public int countAll(Class type) throws NotFoundException;

    /** 
     * @param type
     * @param batchSize
     * @param index
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#findAll(Class type, int batchSize, int index)
     */
    public List<T> findAll(Class type, int batchSize, int index) throws NotFoundException;

    /** 
     * @param jpql
     * @param index
     * @param batchSize 
     * @param parameters 
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#find(String jpql, int batchSize, int index, Map parameters)
     */
    public List<T> find(String jpql, int batchSize, int index, Map parameters) throws NotFoundException;

    /** 
     * @param jpql 
     * @param parameters 
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#find(String jpql, Map parameters)
     */
    public List<T> find(String jpql, Map parameters) throws NotFoundException;

    /** 
     * @param jpql
     * @param parameters
     * @return
     * @see jta.JtaDao#createQuery(String jpql, Map parameters)
     */
    public Query createQuery(String jpql, Map parameters);

    /** 
     * @param jpql
     * @param parameters
     * @return
     * @see jta.JtaDao#createNamedQuery(String jpql, Map parameters)
     */
    public Query createNamedQuery(String jpql, Map parameters);

    /** 
     * @param jpql
     * @param parameters
     * @return
     * @see jta.JtaDao#createNativeQuery(String jpql, Map parameters)
    
     */
    public Query createNativeQuery(String jpql, Map parameters);

    /** 
     * @param jpql 
     * @param parameters 
     * @param resultClass
     * @return
     * @see jta.JtaDao#createNativeQuery(String jpql, Map parameters, Class resultClass)
     */
    public Query createNativeQuery(String jpql, Map parameters, Class resultClass);

    /** 
     * @param jpql
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#getSingleResult(String jpql)
     */
    public Object getSingleResult(String jpql) throws NotFoundException;

    /** 
     * @param jpql
     * @param parameters
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#getSingleResult(String jpql, Map parameters)
     */
    public Object getSingleResult(String jpql, Map parameters) throws NotFoundException;

    /** 
     * @param jpql 
     * @return
     * @throws BadUpdateException
     * @throws NotFoundException
     * @see jta.JtaDao#update(String jpql)
     */
    public int update(String jpql) throws BadUpdateException, NotFoundException;

    /** 
     * @param jpql 
     * @param parameters 
     * @return
     * @throws BadUpdateException
     * @throws NotFoundException
     * @see jta.JtaDao#update(String jpql, Map parameters)
     */
    public int update(String jpql, Map parameters) throws BadUpdateException, NotFoundException;

    /** 
     * @param <V> 
     * @param command
     * @return
     * @throws Exception
     * @see com.kiasolutions.comun.jpa.jta.JtaDao#execute(Command<V> command)
     */
    public <V> V execute(Command<V> command) throws Exception;

    /** 
     * @param type
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#synchronizedFindAll(Class type)
     */
    public List<T> synchronizedFindAll(Class type) throws NotFoundException;

    /** 
     * @param jpql
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#synchronizedFind(Class type, int batchSize, int index)
     */
    public List<T> synchronizedFind(String jpql) throws NotFoundException;

    /** 
     * @param type 
     * @param batchSize
     * @param index
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#synchronizedFindAll(Class type, int batchSize, int index)
     */
    public List<T> synchronizedFindAll(Class type, int batchSize, int index) throws NotFoundException;

    /** 
     * @param jpql 
     * @param batchSize
     * @param index
     * @param parameters
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#synchronizedFind(String jpql, int batchSize, int index, Map parameters)
     */
    public List<T> synchronizedFind(String jpql, int batchSize, int index, Map parameters) throws NotFoundException;

    /** 
     * @param jpql
     * @param parameters
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#synchronizedFind(String jpql, Map parameters)
     */
    public List<T> synchronizedFind(String jpql, Map parameters) throws NotFoundException;

    /** 
     * @param type
     * @param id
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#getReference
     */
    public T getReference(Class<T> type, K id) throws NotFoundException;

    /** 
     * @param entity
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#contains
     */
    public boolean contains(T entity) throws NotFoundException;

    /** 
     * @throws BadUpdateException
     * @see jta.JtaDao#flush
     */
    public void flush() throws BadUpdateException;

    /** 
     * @param entity 
     * @throws NotFoundException
     * @see jta.JtaDao#refresh
     */
    public void refresh(T entity) throws NotFoundException;

    /** 
     * @param jpql 
     * @param parameters 
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#getNativeSingleResult(String jpql, Map parameters)
     */
    public Object getNativeSingleResult(String jpql, Map parameters) throws NotFoundException;

    /** 
     * @param jpql
     * @param parameters
     * @param resultClass 
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#getNativeSingleResult(String jpql, Map parameters, Class resultClass)
     */
    public Object getNativeSingleResult(String jpql, Map parameters, Class resultClass) throws NotFoundException;

    /** 
     * @param jpql 
     * @param batchSize 
     * @param index
     * @param parameters 
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#synchronizedNativeFind(String jpql, int batchSize, int index, Map parameters)
     */
    public List<T> synchronizedNativeFind(String jpql, int batchSize, int index, Map parameters) throws NotFoundException;

    /** 
     * @param jpql
     * @param parameters
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#executeNativeUpdate(String jpql, Map parameters)
     */
    public int executeNativeUpdate(String jpql, Map parameters) throws NotFoundException;

    /** 
     * @param jpql 
     * @param parameters 
     * @return 
     * @throws NotFoundException
     * @see jta.JtaDao#synchronizedNativeFind(String jpql, Map parameters)
     */
    public List synchronizedNativeFind(String jpql, Map parameters) throws NotFoundException;

    /** 
     * @param jpql 
     * @param parameters
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#nativeFind(String jpql, Map parameters)
     */
    public List nativeFind(String jpql, Map parameters) throws NotFoundException;

    /** 
     * @param jpql 
     * @param batchSize 
     * @param index
     * @param parameters
     * @param resultClass
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#nativeFind(String jpql, int batchSize, int index, Map parameters, Class resultClass)
     */
    public List nativeFind(String jpql, int batchSize, int index, Map parameters, Class resultClass) throws NotFoundException;

    /** 
     * @param jpql 
     * @param batchSize 
     * @param index
     * @param parameters
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#nativeFind(String jpql, int batchSize, int index, Map parameters, Class resultClass)
     */
    public List nativeFind(String jpql, int batchSize, int index, Map parameters) throws NotFoundException;

    /** 
     * @param jpql
     * @param parameters
     * @param resultClass
     * @return
     * @throws NotFoundException
     * @see jta.JtaDao#nativeFind(String jpql, Map parameters, Class resultClass)
     */
    public List nativeFind(String jpql, Map parameters, Class resultClass) throws NotFoundException;

    /**
     *
     * @return
     */
    public EntityManager getEntityManager();
}
