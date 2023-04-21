package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * CoEditorReviewerPairT2 entities. Transaction control of the save(), update()
 * and delete() operations must be handled externally by senders of these
 * methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 * 
 * @see edu.univalle.lingweb.persistence.CoEditorReviewerPairT2
 * @author LingWeb
 */
public class CoEditorReviewerPairT2DAO implements ICoEditorReviewerPairT2DAO {

    public static final String STUDENT_A = "studentA";

    public static final String STUDENT_B = "studentB";

    public static final String ID_A = "idA";

    public static final String ID_B = "idB";

    public static final String TIME_TO_SWITCH = "timeToSwitch";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
	 * Perform an initial save of a previously unsaved CoEditorReviewerPairT2
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoEditorReviewerPairT2DAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoEditorReviewerPairT2 entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoEditorReviewerPairT2 entity) {
        EntityManagerHelper.log("saving CoEditorReviewerPairT2 instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent CoEditorReviewerPairT2 entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently deleted from the persistence store, i.e., database.
	 * This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoEditorReviewerPairT2DAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoEditorReviewerPairT2 entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoEditorReviewerPairT2 entity) {
        EntityManagerHelper.log("deleting CoEditorReviewerPairT2 instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(CoEditorReviewerPairT2.class, entity.getEditorReviewerPairTeacherId());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Persist a previously saved CoEditorReviewerPairT2 entity and return it or
	 * a copy of it to the sender. A copy of the CoEditorReviewerPairT2 entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently saved to the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = CoEditorReviewerPairT2DAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoEditorReviewerPairT2 entity to update
	 * @returns CoEditorReviewerPairT2 the persisted CoEditorReviewerPairT2
	 *          entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoEditorReviewerPairT2 update(CoEditorReviewerPairT2 entity) {
        EntityManagerHelper.log("updating CoEditorReviewerPairT2 instance", Level.INFO, null);
        try {
            CoEditorReviewerPairT2 result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public CoEditorReviewerPairT2 findById(Long id) {
        EntityManagerHelper.log("finding CoEditorReviewerPairT2 instance with id: " + id, Level.INFO, null);
        try {
            CoEditorReviewerPairT2 instance = getEntityManager().find(CoEditorReviewerPairT2.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all CoEditorReviewerPairT2 entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoEditorReviewerPairT2 property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<CoEditorReviewerPairT2> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<CoEditorReviewerPairT2> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding CoEditorReviewerPairT2 instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from CoEditorReviewerPairT2 model where model." + propertyName + "= :propertyValue";
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

    public List<CoEditorReviewerPairT2> findByStudentA(Object studentA, int... rowStartIdxAndCount) {
        return findByProperty(STUDENT_A, studentA, rowStartIdxAndCount);
    }

    public List<CoEditorReviewerPairT2> findByStudentB(Object studentB, int... rowStartIdxAndCount) {
        return findByProperty(STUDENT_B, studentB, rowStartIdxAndCount);
    }

    public List<CoEditorReviewerPairT2> findByIdA(Object idA, int... rowStartIdxAndCount) {
        return findByProperty(ID_A, idA, rowStartIdxAndCount);
    }

    public List<CoEditorReviewerPairT2> findByIdB(Object idB, int... rowStartIdxAndCount) {
        return findByProperty(ID_B, idB, rowStartIdxAndCount);
    }

    public List<CoEditorReviewerPairT2> findByTimeToSwitch(Object timeToSwitch, int... rowStartIdxAndCount) {
        return findByProperty(TIME_TO_SWITCH, timeToSwitch, rowStartIdxAndCount);
    }

    /**
	 * Find all CoEditorReviewerPairT2 entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoEditorReviewerPairT2> all CoEditorReviewerPairT2 entities
	 */
    @SuppressWarnings("unchecked")
    public List<CoEditorReviewerPairT2> findAll(final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding all CoEditorReviewerPairT2 instances", Level.INFO, null);
        try {
            final String queryString = "select model from CoEditorReviewerPairT2 model";
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
