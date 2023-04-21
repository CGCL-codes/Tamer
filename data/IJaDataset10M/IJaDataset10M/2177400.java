package genesis.common.persistence.api;

import java.io.Serializable;
import java.util.Set;

/**
 * Provides the interface that all Data Access Object(DAO) must implement. The 
 * basic CRUD methods are defined in here. For each persistent entity, must 
 * exists its correlated DAO interface, that must extends this interface and 
 * define the entity specific manipulation methods.
 * 
 * This interface is part of Genesis Persistence API
 * 
 * @param <K> The entity key type.
 * @param <E> The entity type.
 * 
 * @author Bruno Cartaxo
 * @since 1.0
 */
public interface DAO<K extends Serializable, E> {

    /**
	 * Persists the given entity.
	 * 
	 * @throws PersistenceException When an error occurred on the persistence layer.
	 */
    public abstract void create(E entity) throws PersistenceException;

    /**
	 * Retrieve the entity that have the given key. Returns null if does not 
	 * exists an entity with the given key.
	 * 
	 * @param key The key of the entity that will be retrieved.
	 * @return The entity that have the given key, or null if does not exists an
	 * entity with the given key.
	 * @throws PersistenceException When an error occurred on the persistence layer.
	 */
    public abstract E retrieve(K key) throws PersistenceException;

    /**
	 * Update the given entity.
	 * 
	 * @param entity The entity that will be updated.
	 * @throws PersistenceException When an error occurred on the persistence layer.
	 */
    public abstract void update(E entity) throws PersistenceException;

    /**
	 * Delete the given entity.
	 * 
	 * @param entity The entity that will be deleted.
	 * @throws PersistenceException When an error occurred on the persistence layer.
	 */
    public abstract void delete(E entity) throws PersistenceException;

    /**
	 * Retrieve all entities that belongs the "E" type.
	 * 
	 * @return All entities that belongs the "E" type.
	 * @throws PersistenceException When an error occurred on the persistence layer.
	 */
    public abstract Set<E> retrieveAll() throws PersistenceException;
}
