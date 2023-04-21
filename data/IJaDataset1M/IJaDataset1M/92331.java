package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;

/**
 * Interface for CoParagraphDAO.
 * 
 * @author LingWeb
 */
public interface ICoParagraphDAO {

    /**
	 * Perform an initial save of a previously unsaved CoParagraph entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoParagraphDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoParagraph entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoParagraph entity);

    /**
	 * Delete a persistent CoParagraph entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoParagraphDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoParagraph entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoParagraph entity);

    /**
	 * Persist a previously saved CoParagraph entity and return it or a copy of
	 * it to the sender. A copy of the CoParagraph entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ICoParagraphDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoParagraph entity to update
	 * @returns CoParagraph the persisted CoParagraph entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoParagraph update(CoParagraph entity);

    public CoParagraph findById(Long id);

    /**
	 * Find all CoParagraph entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoParagraph property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoParagraph> found by query
	 */
    public List<CoParagraph> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CoParagraph> findByExplainText(Object explainText, int... rowStartIdxAndCount);

    public List<CoParagraph> findByEditorText(Object editorText, int... rowStartIdxAndCount);

    public List<CoParagraph> findByNumRows(Object numRows, int... rowStartIdxAndCount);

    public List<CoParagraph> findBySizeY(Object sizeY, int... rowStartIdxAndCount);

    public List<CoParagraph> findBySizeX(Object sizeX, int... rowStartIdxAndCount);

    public List<CoParagraph> findByScore(Object score, int... rowStartIdxAndCount);

    public List<CoParagraph> findByTitleText(Object titleText, int... rowStartIdxAndCount);

    /**
	 * Find all CoParagraph entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoParagraph> all CoParagraph entities
	 */
    public List<CoParagraph> findAll(int... rowStartIdxAndCount);
}
