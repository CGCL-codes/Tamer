package zhyi.zee.jpa;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Provides implementation of basic CRUD operations for entities. Subclasses
 * need to implement {@link #getEntityManager()} method, and are typically
 * session bean classes.
 * <p>Unlike {@link BasicEntityDao}, this class has generic methods instead of
 * being a generic class, thus this class can be used to manage all types of
 * entities.</p>
 * @author Zhao Yi
 */
public abstract class UnifiedEntityDao {

    private String findAllQuery;

    private String countQuery;

    protected UnifiedEntityDao() {
        findAllQuery = "select e from ?1 e";
        countQuery = "select count(e) from ?1 e";
    }

    /**
     * Returns the {@link EntityManager} instance for DB operation.
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Persists the specified entity.
     */
    public void persist(Object entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Finds an entity by primary key. Returns {@code null} if the entity
     * doesn't exist.
     * @param <T> Type of the entity.
     */
    public <T> T find(Class<T> entityClass, Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Finds all entities.
     * @param <T> Type of the entity.
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        return getEntityManager().createQuery(findAllQuery, entityClass).setParameter(1, entityClass.getSimpleName()).getResultList();
    }

    /**
     * Finds all entities within the specified range.
     * @param <T> Type of the entity.
     * @param first The position of the first entity to retrieve.
     * @param max The maximum number of entities to retrieve.
     */
    public <T> List<T> findRange(Class<T> entityClass, int first, int max) {
        return getEntityManager().createQuery(findAllQuery, entityClass).setParameter(1, entityClass.getSimpleName()).setFirstResult(first).setMaxResults(max).getResultList();
    }

    /**
     * Returns the total number of entities.
     * @param <T> Type of the entity.
     */
    public <T> long count(Class<T> entityClass) {
        return (Long) getEntityManager().createQuery(countQuery).setParameter(1, entityClass.getSimpleName()).getSingleResult();
    }

    /**
     * Merges the specified entity.
     * @param <T> Type of the entity.
     * @return The entity whose state was merged to.
     */
    public <T> T merge(T entity) {
        return getEntityManager().merge(entity);
    }

    /**
     * Removes the specified entity.
     */
    public void remove(Object entity) {
        getEntityManager().remove(merge(entity));
    }
}
