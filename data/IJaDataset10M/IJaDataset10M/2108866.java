package co.edu.dataaccess.dao;

import co.edu.dataaccess.entityManager.EntityManagerHelper;
import co.edu.usbcali.modeloo.Usuarios;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Usuarios entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 *
 * @see lidis.Usuarios
 */
public class UsuariosDAO implements IUsuariosDAO {

    public static final String IDUSUARIO = "idusuario";

    public static final String LOGIN = "login";

    public static final String PASSWORD = "password";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
     * Perform an initial save of a previously unsaved Usuarios entity. All
     * subsequent persist actions of this entity should use the #update()
     * method. This operation must be performed within the a database
     * transaction context for the entity's data to be permanently saved to the
     * persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
     * operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * UsuariosDAO.save(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity
     *            Usuarios entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(Usuarios entity) {
        EntityManagerHelper.log("saving Usuarios instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent Usuarios entity. This operation must be performed
     * within the a database transaction context for the entity's data to be
     * permanently deleted from the persistence store, i.e., database. This
     * method uses the
     * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
     * operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * UsuariosDAO.delete(entity);
     * EntityManagerHelper.commit();
     * entity = null;
     * </pre>
     *
     * @param entity
     *            Usuarios entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(Usuarios entity) {
        EntityManagerHelper.log("deleting Usuarios instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(Usuarios.class, entity.getIdusuario());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved Usuarios entity and return it or a copy of it
     * to the sender. A copy of the Usuarios entity parameter is returned when
     * the JPA persistence mechanism has not previously been tracking the
     * updated entity. This operation must be performed within the a database
     * transaction context for the entity's data to be permanently saved to the
     * persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
     * operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * entity = UsuariosDAO.update(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity
     *            Usuarios entity to update
     * @return Usuarios the persisted Usuarios entity instance, may not be the
     *         same
     * @throws RuntimeException
     *             if the operation fails
     */
    public Usuarios update(Usuarios entity) {
        EntityManagerHelper.log("updating Usuarios instance", Level.INFO, null);
        try {
            Usuarios result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public Usuarios findById(Integer id) {
        EntityManagerHelper.log("finding Usuarios instance with id: " + id, Level.INFO, null);
        try {
            Usuarios instance = getEntityManager().find(Usuarios.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all  Usuarios entities with a specific property value.
     *
     * @param propertyName
     *            the metaData.name of the  Usuarios property to query
     * @param value
     *            the property value to match
     * @return List< Usuarios> found by query
     */
    @SuppressWarnings("unchecked")
    public List<Usuarios> findByProperty(String propertyName, final Object value) {
        EntityManagerHelper.log("finding  Usuarios instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from  Usuarios model where model." + propertyName + "= :propertyValue";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find by property metaData.name failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all Usuarios entities with a specific property value.
     *
     * @param propertyName
     *            the name of the Usuarios property to query
     * @param value
     *            the property value to match
     * @param rowStartIdxAndCount
     *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
     *            row index in the query result-set to begin collecting the
     *            results. rowStartIdxAndCount[1] specifies the the maximum
     *            number of results to return.
     * @return List<Usuarios> found by query
     */
    @SuppressWarnings("unchecked")
    public List<Usuarios> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding Usuarios instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from Usuarios model where model." + propertyName + "= :propertyValue";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("propertyValue", value);
            if ((rowStartIdxAndCount != null) && (rowStartIdxAndCount.length > 0)) {
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

    public List<Usuarios> findByIdusuario(Object idusuario, int... rowStartIdxAndCount) {
        return findByProperty(IDUSUARIO, idusuario, rowStartIdxAndCount);
    }

    public List<Usuarios> findByIdusuario(Object idusuario) {
        return findByProperty(IDUSUARIO, idusuario);
    }

    public List<Usuarios> findByLogin(Object login, int... rowStartIdxAndCount) {
        return findByProperty(LOGIN, login, rowStartIdxAndCount);
    }

    public List<Usuarios> findByLogin(Object login) {
        return findByProperty(LOGIN, login);
    }

    public List<Usuarios> findByPassword(Object password, int... rowStartIdxAndCount) {
        return findByProperty(PASSWORD, password, rowStartIdxAndCount);
    }

    public List<Usuarios> findByPassword(Object password) {
        return findByProperty(PASSWORD, password);
    }

    /**
     * Find all Usuarios entities.
     *
     * @param rowStartIdxAndCount
     *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
     *            row index in the query result-set to begin collecting the
     *            results. rowStartIdxAndCount[1] specifies the the maximum
     *            count of results to return.
     * @return List<Usuarios> all Usuarios entities
     */
    @SuppressWarnings("unchecked")
    public List<Usuarios> findAll(final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding all Usuarios instances", Level.INFO, null);
        try {
            final String queryString = "select model from Usuarios model";
            Query query = getEntityManager().createQuery(queryString);
            if ((rowStartIdxAndCount != null) && (rowStartIdxAndCount.length > 0)) {
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

    public List<Usuarios> findByCriteria(String whereCondition) {
        try {
            String where = ((whereCondition == null) || (whereCondition.length() == 0)) ? "" : ("where " + whereCondition);
            final String queryString = "select model from Usuarios model " + where;
            Query query = getEntityManager().createQuery(queryString);
            List<Usuarios> entitiesList = query.getResultList();
            return entitiesList;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find By Criteria in Usuarios failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<Usuarios> findPageUsuarios(String sortColumnName, boolean sortAscending, int startRow, int maxResults) {
        if ((sortColumnName != null) && (sortColumnName.length() > 0)) {
            try {
                String queryString = "select model from Usuarios model order by model." + sortColumnName + " " + (sortAscending ? "asc" : "desc");
                return getEntityManager().createQuery(queryString).setFirstResult(startRow).setMaxResults(maxResults).getResultList();
            } catch (RuntimeException re) {
                throw re;
            }
        } else {
            try {
                String queryString = "select model from Usuarios model";
                return getEntityManager().createQuery(queryString).setFirstResult(startRow).setMaxResults(maxResults).getResultList();
            } catch (RuntimeException re) {
                throw re;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Long findTotalNumberUsuarios() {
        try {
            String queryString = "select count(*) from Usuarios model";
            return (Long) getEntityManager().createQuery(queryString).getSingleResult();
        } catch (RuntimeException re) {
            throw re;
        }
    }
}
