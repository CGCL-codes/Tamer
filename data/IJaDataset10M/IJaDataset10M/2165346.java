package com.asl.library.persistance;

import java.io.Serializable;
import java.util.List;

/**
 * @author asl
 *
 */
public interface Repository {

    String SERVICE_NAME = "RepositoryService";

    /**
     * Creates or update some entity.
     *
     * @param entity the entity instance
     */
    void createOrUpdate(Object entity);

    /**
     * Deletes the given entity
     *
     * @param entity
     */
    void delete(Object entity);

    /**
     * TODO describe the method
     *
     * @param clazz
     * @param id
     * @return
     */
    Object find(Class<?> clazz, Serializable id);

    /**
     * TODO describe the method
     *
     * @param clazz
     * @param id
     * @param properties
     * @return
     */
    List<Object> getPropertiesOfEntity(Class<?> clazz, Serializable id, String... properties);

    /**
     * TODO describe the method
     *
     * @param clazz
     * @param properties
     * @param params
     * @return
     */
    Object findBy(Class<?> clazz, String properties, Object... params);

    /**
     * Returns some specific implementation of query builder (Lucene based search engine might be
     * backed by the implementation)
     *
     * @return
     */
    QueryBuilder getQueryBuilder();
}
