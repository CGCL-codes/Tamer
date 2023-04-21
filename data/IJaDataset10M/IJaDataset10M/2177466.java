package ejb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Facade for entity Criterio.
 * 
 * @see ejb.Criterio
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CriterioFacade implements CriterioFacadeRemote {

    public static final String CRITERIO = "criterio";

    static final String JDBC_DRIVER = "org.postgresql.Driver";

    static final String DATABASE_URL = "jdbc:postgresql://gaia.uel.br:5432/ads";

    @PersistenceContext
    private EntityManager entityManager;

    /**
	 * Perform an initial save of a previously unsaved Criterio entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            Criterio entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(Criterio entity) {
        LogUtil.log("saving Criterio instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent Criterio entity.
	 * 
	 * @param entity
	 *            Criterio entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(Criterio entity) {
        LogUtil.log("deleting Criterio instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(Criterio.class, entity.getIdCriterio());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    public void delete(Integer idCriterio) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, "postgres", "cpsaiag");
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM criterio WHERE id_criterio=" + idCriterio.toString());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException classNotFound) {
            classNotFound.printStackTrace();
            System.exit(1);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (Exception exception) {
                exception.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
	 * Persist a previously saved Criterio entity and return it or a copy of it
	 * to the sender. A copy of the Criterio entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            Criterio entity to update
	 * @return Criterio the persisted Criterio entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public Criterio update(Criterio entity) {
        LogUtil.log("updating Criterio instance", Level.INFO, null);
        try {
            Criterio result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public Criterio findById(Integer id) {
        LogUtil.log("finding Criterio instance with id: " + id, Level.INFO, null);
        try {
            Criterio instance = entityManager.find(Criterio.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all Criterio entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Criterio property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Criterio> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<Criterio> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        LogUtil.log("finding Criterio instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from Criterio model where model." + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
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
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<Criterio> findByCriterio(Object criterio, int... rowStartIdxAndCount) {
        return findByProperty(CRITERIO, criterio, rowStartIdxAndCount);
    }

    /**
	 * Find all Criterio entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Criterio> all Criterio entities
	 */
    @SuppressWarnings("unchecked")
    public List<Criterio> findAll(final int... rowStartIdxAndCount) {
        LogUtil.log("finding all Criterio instances", Level.INFO, null);
        try {
            final String queryString = "select model from Criterio model";
            Query query = entityManager.createQuery(queryString);
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
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
}
