package edu.univalle.lingweb.persistence;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * ToForum entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see edu.univalle.lingweb.persistence.ToForum
 * @author LingWeb
 */
public class ToForumDAO implements IToForumDAO {

    public static final String TITLE = "title";

    public static final String DESCRIPTION = "description";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
	 * Perform an initial save of a previously unsaved ToForum entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ToForumDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            ToForum entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(ToForum entity) {
        EntityManagerHelper.log("saving ToForum instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent ToForum entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ToForumDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            ToForum entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(ToForum entity) {
        EntityManagerHelper.log("deleting ToForum instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(ToForum.class, entity.getForumId());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Persist a previously saved ToForum entity and return it or a copy of it
	 * to the sender. A copy of the ToForum entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ToForumDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            ToForum entity to update
	 * @returns ToForum the persisted ToForum entity instance, may not be the
	 *          same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public ToForum update(ToForum entity) {
        EntityManagerHelper.log("updating ToForum instance", Level.INFO, null);
        try {
            ToForum result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public ToForum findById(Long id) {
        EntityManagerHelper.log("finding ToForum instance with id: " + id, Level.INFO, null);
        try {
            ToForum instance = getEntityManager().find(ToForum.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all ToForum entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ToForum property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<ToForum> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<ToForum> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding ToForum instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from ToForum model where model." + propertyName + "= :propertyValue";
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

    public List<ToForum> findByTitle(Object title, int... rowStartIdxAndCount) {
        return findByProperty(TITLE, title, rowStartIdxAndCount);
    }

    public List<ToForum> findByDescription(Object description, int... rowStartIdxAndCount) {
        return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
    }

    /**
	 * Find all ToForum entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ToForum> all ToForum entities
	 */
    @SuppressWarnings("unchecked")
    public List<ToForum> findAll(final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding all ToForum instances", Level.INFO, null);
        try {
            final String queryString = "select model from ToForum model";
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
