package com.google.code.japa.repository.collection;

import java.io.*;

/**
 * Represents a strategy to implement a persistent collection interacting with the repository.
 * 
 * @param <T> Type of the entity whose strategy manipulates the extent.
 * 
 * @author Leandro Aparecido
 * @since 1.0
 * @see com.google.code.japa.repository.collection.jpa.JpaPersistentCollectionStrategyFactory
 */
public interface PersistentCollectionStrategy<T> extends Serializable {

    /**
	 * Adds the specified entity to the extent.
	 * 
	 * @param index Position of the entity in the extent ordered by entity identity.
	 * @param entity Entity to add.
	 */
    void add(int index, T entity);

    /**
	 * Updates the specified position of the extent with the passed entity.
	 * 
	 * @param index Position of the extent the entity must replace.
	 * @param entity New entity to store.
	 * @return Entity previously at the specified position.
	 */
    T set(int index, T entity);

    /**
	 * Retrieves the entity at the specified position in the extent.
	 * 
	 * @param index Position to retrieve the entity.
	 * @return Entity at the specified position.
	 */
    T get(int index);

    /**
	 * Removes the entity at the specified position in the extent.
	 * 
	 * @param index Position to remove the entity from.
	 * @return Entity previously at the specified position.
	 */
    T remove(int index);

    /**
	 * Removes the entity from the extent using entity identity.
	 * 
	 * @param entity Entity to remove.
	 * @return True if removed the entity successfully.
	 */
    boolean remove(T entity);

    /**
	 * Indicates if the specified entity exists in the extent.
	 * 
	 * @param entity Entity to verify.
	 * @return True if the extent contains the entity.
	 */
    boolean contains(T entity);

    /**
	 * @return The number of entities in the extent.
	 */
    int size();

    /**
	 * Flushes the changes made to the extent since the last flush.
	 */
    void flush();

    /**
	 * Type of the entity the strategy manipulates the extent.
	 * 
	 * @return Type of the entity.
	 */
    Class<T> getEntityType();
}
