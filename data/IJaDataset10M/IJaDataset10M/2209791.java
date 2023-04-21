package com.tpc.control.jpa;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * ReserveItemLabAnswer entities. Transaction control of the save(), update()
 * and delete() operations must be handled externally by senders of these
 * methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 * 
 * @see com.tpc.control.jpa.ReserveItemLabAnswer
 * @author MyEclipse Persistence Tools
 */
public class ReserveItemLabAnswerDAO implements IReserveItemLabAnswerDAO {

    public static final String REMARK = "remark";

    public static final String CREATE_BY = "createBy";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
	 * Perform an initial save of a previously unsaved ReserveItemLabAnswer
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ReserveItemLabAnswerDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            ReserveItemLabAnswer entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(ReserveItemLabAnswer entity) {
        EntityManagerHelper.log("saving ReserveItemLabAnswer instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent ReserveItemLabAnswer entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently deleted from the persistence store, i.e., database.
	 * This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ReserveItemLabAnswerDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            ReserveItemLabAnswer entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(ReserveItemLabAnswer entity) {
        EntityManagerHelper.log("deleting ReserveItemLabAnswer instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(ReserveItemLabAnswer.class, entity.getId());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Persist a previously saved ReserveItemLabAnswer entity and return it or a
	 * copy of it to the sender. A copy of the ReserveItemLabAnswer entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently saved to the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#merge(Object)
	 * EntityManager#merge} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ReserveItemLabAnswerDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            ReserveItemLabAnswer entity to update
	 * @return ReserveItemLabAnswer the persisted ReserveItemLabAnswer entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public ReserveItemLabAnswer update(ReserveItemLabAnswer entity) {
        EntityManagerHelper.log("updating ReserveItemLabAnswer instance", Level.INFO, null);
        try {
            ReserveItemLabAnswer result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public ReserveItemLabAnswer findById(ReserveItemLabAnswerId id) {
        EntityManagerHelper.log("finding ReserveItemLabAnswer instance with id: " + id, Level.INFO, null);
        try {
            ReserveItemLabAnswer instance = getEntityManager().find(ReserveItemLabAnswer.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all ReserveItemLabAnswer entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ReserveItemLabAnswer property to query
	 * @param value
	 *            the property value to match
	 * @return List<ReserveItemLabAnswer> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<ReserveItemLabAnswer> findByProperty(String propertyName, final Object value) {
        EntityManagerHelper.log("finding ReserveItemLabAnswer instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from ReserveItemLabAnswer model where model." + propertyName + "= :propertyValue";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<ReserveItemLabAnswer> findByRemark(Object remark) {
        return findByProperty(REMARK, remark);
    }

    public List<ReserveItemLabAnswer> findByCreateBy(Object createBy) {
        return findByProperty(CREATE_BY, createBy);
    }

    /**
	 * Find all ReserveItemLabAnswer entities.
	 * 
	 * @return List<ReserveItemLabAnswer> all ReserveItemLabAnswer entities
	 */
    @SuppressWarnings("unchecked")
    public List<ReserveItemLabAnswer> findAll() {
        EntityManagerHelper.log("finding all ReserveItemLabAnswer instances", Level.INFO, null);
        try {
            final String queryString = "select model from ReserveItemLabAnswer model";
            Query query = getEntityManager().createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
}
