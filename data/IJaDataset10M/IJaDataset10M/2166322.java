package edu.webteach.dao;

import java.util.*;
import java.io.*;
import org.hibernate.criterion.*;

/**
 * An interface shared by all business data access objects.
 * <p>
 * All CRUD (create, read, update, delete) basic data access operations are
 * isolated in this interface and shared accross all DAO implementations.
 * The current design is for a state-management oriented persistence layer
 * (for example, there is no UDPATE statement function) that provides
 * automatic transactional dirty checking of business objects in persistent
 * state.
 *
 * @author Christian Bauer
 * @author Igor Shubovych
 */
public interface GenericDAO<T, ID extends Serializable> {

    /**
     * Looks for entity, pointed by unique identifier id
     * from data layer (from database or internal cache).
     * If there is no entity with pointed id returns null
     * @param id primary key of entity
     * @param lock flag shows if the entity should be locked
     * @return entity with specified PK
     * or null, if such an entity doesn't exist in database
     */
    public T findById(ID id, boolean lock);

    public T findById(ID id);

    public T getById(ID id);

    /**
     * Returns all entities from data layer
     * @return list of all entities
     */
    public List<T> findAll();

    /**
     * 
     * @param exampleInstance
     * @param excludeProperty
     * @return
     */
    public List<T> findByExample(T exampleInstance, String... excludeProperty);

    /**
     * Returns count of all entities from data layer
     * @return number of finded rows
     */
    public int findAllCount();

    /**
     * Returns count of all finded entities from data layer
     * @return number of finded rows
     */
    public int findByExampleCount(T exampleInstance, String... excludeProperty);

    /**
     * Stores entity to database. If entity which was not
     * persisted in database, didn't have key and PK is
     * automatically generated, then it updates PK fields
     * in entity 
     * @param entity to store
     * @return updated entity (with correct primary key)
     */
    public T makePersistent(T entity);

    /**
     * Delete entity from database layer. If entity
     * is not persisted in database, does nothing.
     * @param entity to delete
     */
    public void makeTransient(T entity);

    /**
     * Affects every managed instance in the current persistence context!
     */
    public void flush();

    /**
     * Affects every managed instance in the current persistence context!
     */
    public void clear();

    public <PersistentClass extends T> List<T> findByCriteria(Class<PersistentClass> persistentClass, Criterion... criterion);

    public List<T> findByCriteria(List<Criterion> criterion, List<Order> order);

    public <PersistentClass extends T> List<T> findByCriteria(Class<PersistentClass> persistentClass, List<Criterion> criterion, List<Order> order);

    public List<T> getPortion(int firstResult, int resultCount, List<Criterion> criterion, List<Order> order);

    public <PersistentClass extends T> List<T> getPortion(Class<PersistentClass> persistentClass, int firstResult, int resultCount, List<Criterion> criterion, List<Order> order);

    public int getRowCount(Criterion... criterion);

    public <PersistentClass extends T> int getRowCount(Class<PersistentClass> persistentClass, Criterion... criterion);
}
