package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * CoMetacognitiveVariable entities. Transaction control of the save(), update()
 * and delete() operations must be handled externally by senders of these
 * methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 * 
 * @see edu.univalle.lingweb.persistence.CoMetacognitiveVariable
 * @author LingWeb
 */
public class CoMetacognitiveVariableDAO implements ICoMetacognitiveVariableDAO {

    public static final String METACOGNITIVE_VARIABLE_NAME = "metacognitiveVariableName";

    public static final String METACOGNITIVE_VARIABLE_NAME_EN = "metacognitiveVariableNameEn";

    public static final String METACOGNITIVE_VARIABLE_NAME_FR = "metacognitiveVariableNameFr";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
	 * Perform an initial save of a previously unsaved CoMetacognitiveVariable
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoMetacognitiveVariableDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoMetacognitiveVariable entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoMetacognitiveVariable entity) {
        EntityManagerHelper.log("saving CoMetacognitiveVariable instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent CoMetacognitiveVariable entity. This operation must
	 * be performed within the a database transaction context for the entity's
	 * data to be permanently deleted from the persistence store, i.e.,
	 * database. This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoMetacognitiveVariableDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoMetacognitiveVariable entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoMetacognitiveVariable entity) {
        EntityManagerHelper.log("deleting CoMetacognitiveVariable instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(CoMetacognitiveVariable.class, entity.getMetacognitiveVariableId());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Persist a previously saved CoMetacognitiveVariable entity and return it
	 * or a copy of it to the sender. A copy of the CoMetacognitiveVariable
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
	 * entity = CoMetacognitiveVariableDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoMetacognitiveVariable entity to update
	 * @returns CoMetacognitiveVariable the persisted CoMetacognitiveVariable
	 *          entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoMetacognitiveVariable update(CoMetacognitiveVariable entity) {
        EntityManagerHelper.log("updating CoMetacognitiveVariable instance", Level.INFO, null);
        try {
            CoMetacognitiveVariable result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public CoMetacognitiveVariable findById(Long id) {
        EntityManagerHelper.log("finding CoMetacognitiveVariable instance with id: " + id, Level.INFO, null);
        try {
            CoMetacognitiveVariable instance = getEntityManager().find(CoMetacognitiveVariable.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all CoMetacognitiveVariable entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoMetacognitiveVariable property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<CoMetacognitiveVariable> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<CoMetacognitiveVariable> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding CoMetacognitiveVariable instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from CoMetacognitiveVariable model where model." + propertyName + "= :propertyValue";
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

    public List<CoMetacognitiveVariable> findByMetacognitiveVariableName(Object metacognitiveVariableName, int... rowStartIdxAndCount) {
        return findByProperty(METACOGNITIVE_VARIABLE_NAME, metacognitiveVariableName, rowStartIdxAndCount);
    }

    public List<CoMetacognitiveVariable> findByMetacognitiveVariableNameEn(Object metacognitiveVariableNameEn, int... rowStartIdxAndCount) {
        return findByProperty(METACOGNITIVE_VARIABLE_NAME_EN, metacognitiveVariableNameEn, rowStartIdxAndCount);
    }

    public List<CoMetacognitiveVariable> findByMetacognitiveVariableNameFr(Object metacognitiveVariableNameFr, int... rowStartIdxAndCount) {
        return findByProperty(METACOGNITIVE_VARIABLE_NAME_FR, metacognitiveVariableNameFr, rowStartIdxAndCount);
    }

    /**
	 * Find all CoMetacognitiveVariable entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoMetacognitiveVariable> all CoMetacognitiveVariable
	 *         entities
	 */
    @SuppressWarnings("unchecked")
    public List<CoMetacognitiveVariable> findAll(final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding all CoMetacognitiveVariable instances", Level.INFO, null);
        try {
            final String queryString = "select model from CoMetacognitiveVariable model";
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
