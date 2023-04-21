package co.edu.dataaccess.dao;

import co.edu.usbcali.modeloo.CostosPorProyecto;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Interface for CostosPorProyectoDAO.
 *
*/
public interface ICostosPorProyectoDAO {

    /**
     * Perform an initial save of a previously unsaved CostosPorProyecto entity. All
     * subsequent persist actions of this entity should use the #update()
     * method. This operation must be performed within the a database
     * transaction context for the entity's data to be permanently saved to the
     * persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
     * operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * ICostosPorProyectoDAO.save(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity
     *            CostosPorProyecto entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(CostosPorProyecto entity);

    /**
     * Delete a persistent CostosPorProyecto entity. This operation must be performed
     * within the a database transaction context for the entity's data to be
     * permanently deleted from the persistence store, i.e., database. This
     * method uses the
     * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
     * operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * ICostosPorProyectoDAO.delete(entity);
     * EntityManagerHelper.commit();
     * entity = null;
     * </pre>
     *
     * @param entity
     *            CostosPorProyecto entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(CostosPorProyecto entity);

    /**
     * Persist a previously saved CostosPorProyecto entity and return it or a copy of it
     * to the sender. A copy of the CostosPorProyecto entity parameter is returned when
     * the JPA persistence mechanism has not previously been tracking the
     * updated entity. This operation must be performed within the a database
     * transaction context for the entity's data to be permanently saved to the
     * persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
     * operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * entity = ICostosPorProyectoDAO.update(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity
     *            CostosPorProyecto entity to update
     * @return CostosPorProyecto the persisted CostosPorProyecto entity instance, may not be the
     *         same
     * @throws RuntimeException
     *             if the operation fails
     */
    public CostosPorProyecto update(CostosPorProyecto entity);

    public CostosPorProyecto findById(Integer id);

    /**
     * Find all CostosPorProyecto entities with a specific property value.
     *
     * @param propertyName
     *            the name of the CostosPorProyecto property to query
     * @param value
     *            the property value to match
     * @param rowStartIdxAndCount
     *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
     *            row index in the query result-set to begin collecting the
     *            results. rowStartIdxAndCount[1] specifies the the maximum
     *            count of results to return.
     * @return List<CostosPorProyecto> found by query
     */
    public List<CostosPorProyecto> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CostosPorProyecto> findByCriteria(String whereCondition);

    public List<CostosPorProyecto> findById(Object id);

    public List<CostosPorProyecto> findById(Object id, int... rowStartIdxAndCount);

    /**
     * Find all CostosPorProyecto entities.
     *
     * @param rowStartIdxAndCount
     *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
     *            row index in the query result-set to begin collecting the
     *            results. rowStartIdxAndCount[1] specifies the the maximum
     *            count of results to return.
     * @return List<CostosPorProyecto> all CostosPorProyecto entities
     */
    public List<CostosPorProyecto> findAll(int... rowStartIdxAndCount);

    public List<CostosPorProyecto> findPageCostosPorProyecto(String sortColumnName, boolean sortAscending, int startRow, int maxResults);

    public Long findTotalNumberCostosPorProyecto();
}
