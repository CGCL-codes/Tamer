package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * MaParagraphForm entities. Transaction control of the save(), update() and
 * delete() operations must be handled externally by senders of these methods or
 * must be manually added to each of these methods for data to be persisted to
 * the JPA datastore.
 * 
 * @see edu.univalle.lingweb.persistence.MaParagraphForm
 * @author MyEclipse Persistence Tools
 */
public class MaParagraphFormDAO implements IMaParagraphFormDAO {

    public static final String SIZE_X = "sizeX";

    public static final String SIZE_Y = "sizeY";

    public static final String NUM_ROWS = "numRows";

    public static final String EXPLAIN_TEXT = "explainText";

    public static final String EDITOR_TEXT = "editorText";

    public static final String TITLE_TEXT = "titleText";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
	 * Perform an initial save of a previously unsaved MaParagraphForm entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * MaParagraphFormDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            MaParagraphForm entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(MaParagraphForm entity) {
        EntityManagerHelper.log("saving MaParagraphForm instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent MaParagraphForm entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently deleted from the persistence store, i.e., database.
	 * This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * MaParagraphFormDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            MaParagraphForm entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(MaParagraphForm entity) {
        EntityManagerHelper.log("deleting MaParagraphForm instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(MaParagraphForm.class, entity.getParagraphFormId());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Persist a previously saved MaParagraphForm entity and return it or a copy
	 * of it to the sender. A copy of the MaParagraphForm entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity. This operation must be performed within the
	 * a database transaction context for the entity's data to be permanently
	 * saved to the persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = MaParagraphFormDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            MaParagraphForm entity to update
	 * @return MaParagraphForm the persisted MaParagraphForm entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public MaParagraphForm update(MaParagraphForm entity) {
        EntityManagerHelper.log("updating MaParagraphForm instance", Level.INFO, null);
        try {
            MaParagraphForm result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public MaParagraphForm findById(Long id) {
        EntityManagerHelper.log("finding MaParagraphForm instance with id: " + id, Level.INFO, null);
        try {
            MaParagraphForm instance = getEntityManager().find(MaParagraphForm.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all MaParagraphForm entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the MaParagraphForm property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<MaParagraphForm> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<MaParagraphForm> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding MaParagraphForm instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from MaParagraphForm model where model." + propertyName + "= :propertyValue";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("propertyValue", value);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }
                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<MaParagraphForm> findBySizeX(Object sizeX, int... rowStartIdxAndCount) {
        return findByProperty(SIZE_X, sizeX, rowStartIdxAndCount);
    }

    public List<MaParagraphForm> findBySizeY(Object sizeY, int... rowStartIdxAndCount) {
        return findByProperty(SIZE_Y, sizeY, rowStartIdxAndCount);
    }

    public List<MaParagraphForm> findByNumRows(Object numRows, int... rowStartIdxAndCount) {
        return findByProperty(NUM_ROWS, numRows, rowStartIdxAndCount);
    }

    public List<MaParagraphForm> findByExplainText(Object explainText, int... rowStartIdxAndCount) {
        return findByProperty(EXPLAIN_TEXT, explainText, rowStartIdxAndCount);
    }

    public List<MaParagraphForm> findByEditorText(Object editorText, int... rowStartIdxAndCount) {
        return findByProperty(EDITOR_TEXT, editorText, rowStartIdxAndCount);
    }

    public List<MaParagraphForm> findByTitleText(Object titleText, int... rowStartIdxAndCount) {
        return findByProperty(TITLE_TEXT, titleText, rowStartIdxAndCount);
    }

    /**
	 * Find all MaParagraphForm entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<MaParagraphForm> all MaParagraphForm entities
	 */
    @SuppressWarnings("unchecked")
    public List<MaParagraphForm> findAll(final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding all MaParagraphForm instances", Level.INFO, null);
        try {
            final String queryString = "select model from MaParagraphForm model";
            Query query = getEntityManager().createQuery(queryString);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }
                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
}
