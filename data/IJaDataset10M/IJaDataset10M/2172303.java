package org.imogene.ws.dao;

import java.util.List;

/**
 * Generic dao 
 * @author Medes-IMPS 
 */
public interface GenericDao {

    /**
	 * Save or update teh specified entity
	 * @param entity the entity to save or to update
	 */
    public void saveOrUpdate(Object entity);

    /**
	 * Load an entity
	 * @param entityClass the entity class
	 * @param id the entity id
	 * @return the entity if it exists
	 */
    public Object loadEntity(Class<?> entityClass, String id);

    /**
	 * Get list of entities of the specified class
	 * @param entityClass the entities class
	 * @param count max number of results
	 * @return the list of entities
	 */
    public List<?> listBeans(Class<?> entityClass);
}
