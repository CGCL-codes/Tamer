package org.modelibra;

import java.io.Serializable;
import org.modelibra.config.ConceptConfig;

/**
 * <p>
 * IEntity interface to represent an entity.
 * </p>
 * 
 * <p>
 * An entity is a concept with properties and neighbors. It has a predefined
 * property: oid (object artificial identifier). An entity may be related to
 * other concepts that are called neighbors. A neighbor is either of the IEntity
 * (max. cardinality of 1: parent) or IEntities (max. cardinality of N: child)
 * type. The entity semantical id (identifier) consists of one or more
 * properties and/or one or more parent neighbors.
 * </p>
 * 
 * <p>
 * The entity concept is configured in one of three XML configuration files.
 * </p>
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-08-23
 */
public interface IEntity<T extends IEntity<T>> extends Serializable, Comparable<T>, ISelectable<T> {

    /**
	 * Gets the model.
	 * 
	 * @return model
	 */
    public IDomainModel getModel();

    /**
	 * Gets the concept configuration.
	 * 
	 * @return concept configuration
	 */
    public ConceptConfig getConceptConfig();

    /**
	 * Sets the oid.
	 * 
	 * @param oid
	 *            oid
	 */
    public void setOid(Oid oid);

    /**
	 * Gets the oid.
	 * 
	 * @return the oid
	 */
    public Oid getOid();

    /**
	 * Gets the unique combination.
	 * 
	 * @return unique combination
	 */
    public UniqueCombination getUniqueCombination();

    /**
	 * Gets the index combination.
	 * 
	 * @return index combination
	 */
    public IndexCombination getIndexCombination();

    /**
	 * Updates the entity with the given entity.
	 * 
	 * @param entity
	 *            entity
	 * @return <code>true</code> if the entity is updated with the given entity
	 */
    public boolean update(T entity);

    /**
	 * Updates only the properties of the entity with a given entity.
	 * 
	 * @param entity
	 *            entity
	 * @return <code>true</code> if the entity is updated with a given entity
	 */
    public boolean updateProperties(T entity);

    /**
	 * Copies the entity.
	 * 
	 * @return copied entity
	 */
    public T copy();

    /**
	 * Copies only the properties of the entity.
	 * 
	 * @return copied entity
	 */
    public T copyProperties();

    /**
	 * Copies the entity by copying the whole internal tree with the entity as
	 * the root. The copied entity may be a part of another model.
	 * 
	 * @param model
	 *            another model
	 * @return deep copied entity
	 */
    public T deepCopy(IDomainModel model);

    /**
	 * Checks if the entity has the same oid as the given entity. Used in the
	 * overriden implementation of the equals method.
	 * 
	 * @param entity
	 *            entity
	 * @return <code>true</code> if the entity has the same oid as the given
	 *         entity
	 */
    public boolean equalOid(T entity);

    /**
	 * Checks if the entity has the same unique combination (id) as the given
	 * entity.
	 * 
	 * @param entity
	 *            entity
	 * @return <code>true</code> if the entity has the same unique combination
	 *         (id) as the given entity
	 */
    public boolean equalUnique(T entity);

    /**
	 * Checks if the entity has the same properties as the given entity.
	 * 
	 * @param entity
	 *            entity
	 * @return <code>true</code> if the entity has the same properties as the
	 *         given entity
	 */
    public boolean equalProperties(T entity);

    /**
	 * Checks if the entity has the same parent neighbors as the given entity.
	 * 
	 * @param entity
	 *            entity
	 * @return <code>true</code> if the entity has the same parent neighbors as
	 *         the given entity
	 */
    public boolean equalParentNeighbors(T entity);

    /**
	 * Checks if the entity has the same content (properties and neighbors) as
	 * the given entity.
	 * 
	 * @param entity
	 *            entity
	 * @return <code>true</code> if the entity has the same content as the given
	 *         entity
	 */
    public boolean equalContent(T entity);

    /**
	 * Sets the entity property given a property code.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property object
	 */
    public void setProperty(String propertyCode, Object property);

    /**
	 * Gets the entity property given a property code.
	 * 
	 * @param propertyCode
	 *            property code
	 * @return property
	 */
    public Object getProperty(String propertyCode);

    /**
	 * Sets the parent neighbor given a neighbor code.
	 * 
	 * @param neighborCode
	 *            neighbor code
	 * @param neighborEntity
	 *            neighbor entity
	 */
    public void setParentNeighbor(String neighborCode, IEntity<? extends IEntity<?>> neighborEntity);

    /**
	 * Gets the parent neighbor given a neighbor code.
	 * 
	 * @param neighborCode
	 *            neighbor code
	 * @return neighbor entity
	 */
    public IEntity<? extends IEntity<?>> getParentNeighbor(String neighborCode);

    /**
	 * Sets the child neighbor given a neighbor code.
	 * 
	 * @param neighborCode
	 *            neighbor code
	 * @param neighborEntities
	 *            neighbor entities
	 */
    public void setChildNeighbor(String neighborCode, IEntities<?> neighborEntities);

    /**
	 * Gets the child neighbor given a neighbor code.
	 * 
	 * @param neighborCode
	 *            neighbor code
	 * @return neighbor entities
	 */
    public IEntities<?> getChildNeighbor(String neighborCode);
}
