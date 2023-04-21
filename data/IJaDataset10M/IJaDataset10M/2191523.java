package com.tpc.control.jpa;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * Interface for PlantDAO.
 * 
 * @author MyEclipse Persistence Tools
 */
public interface IPlantDAO {

    /**
	 * Perform an initial save of a previously unsaved Plant entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IPlantDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Plant entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(Plant entity);

    /**
	 * Delete a persistent Plant entity. This operation must be performed within
	 * the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IPlantDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            Plant entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(Plant entity);

    /**
	 * Persist a previously saved Plant entity and return it or a copy of it to
	 * the sender. A copy of the Plant entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = IPlantDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Plant entity to update
	 * @return Plant the persisted Plant entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public Plant update(Plant entity);

    public Plant findById(String id);

    /**
	 * Find all Plant entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Plant property to query
	 * @param value
	 *            the property value to match
	 * @return List<Plant> found by query
	 */
    public List<Plant> findByProperty(String propertyName, Object value);

    public List<Plant> findByPlantName(Object plantName);

    public List<Plant> findByOnLoading(Object onLoading);

    public List<Plant> findByInputFileName(Object inputFileName);

    public List<Plant> findByInactUpload(Object inactUpload);

    /**
	 * Find all Plant entities.
	 * 
	 * @return List<Plant> all Plant entities
	 */
    public List<Plant> findAll();
}
