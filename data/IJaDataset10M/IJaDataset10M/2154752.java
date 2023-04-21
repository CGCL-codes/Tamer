package edu.univalle.lingweb.persistence;

import java.util.List;

/**
 * Interface for CoParagraphBaseKnowledgeDAO.
 * 
 * @author LingWeb
 */
public interface ICoParagraphBaseKnowledgeDAO {

    /**
	 * Perform an initial save of a previously unsaved CoParagraphBaseKnowledge
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoParagraphBaseKnowledgeDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoParagraphBaseKnowledge entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoParagraphBaseKnowledge entity);

    /**
	 * Delete a persistent CoParagraphBaseKnowledge entity. This operation must
	 * be performed within the a database transaction context for the entity's
	 * data to be permanently deleted from the persistence store, i.e.,
	 * database. This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoParagraphBaseKnowledgeDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoParagraphBaseKnowledge entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoParagraphBaseKnowledge entity);

    /**
	 * Persist a previously saved CoParagraphBaseKnowledge entity and return it
	 * or a copy of it to the sender. A copy of the CoParagraphBaseKnowledge
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently saved to the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ICoParagraphBaseKnowledgeDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoParagraphBaseKnowledge entity to update
	 * @returns CoParagraphBaseKnowledge the persisted CoParagraphBaseKnowledge
	 *          entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoParagraphBaseKnowledge update(CoParagraphBaseKnowledge entity);

    public CoParagraphBaseKnowledge findById(Long id);

    /**
	 * Find all CoParagraphBaseKnowledge entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the CoParagraphBaseKnowledge property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoParagraphBaseKnowledge> found by query
	 */
    public List<CoParagraphBaseKnowledge> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CoParagraphBaseKnowledge> findByRowNumber(Object rowNumber, int... rowStartIdxAndCount);

    public List<CoParagraphBaseKnowledge> findByKnowledge(Object knowledge, int... rowStartIdxAndCount);

    /**
	 * Find all CoParagraphBaseKnowledge entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoParagraphBaseKnowledge> all CoParagraphBaseKnowledge
	 *         entities
	 */
    public List<CoParagraphBaseKnowledge> findAll(int... rowStartIdxAndCount);
}
