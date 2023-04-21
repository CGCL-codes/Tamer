package nl.gridshore.samples.books.integration.jpa;

import nl.gridshore.samples.books.domain.BaseDomain;
import nl.gridshore.samples.books.integration.BaseDao;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityManagerFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Mar 19, 2008
 * Time: 7:39:25 AM
 * BaseDao implementation using JPA
 */
public class BaseDaoJpa<T extends BaseDomain> implements BaseDao<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> prototype;

    private String entityName;

    public BaseDaoJpa(Class<T> prototype, String entityName) {
        this.prototype = prototype;
        this.entityName = entityName;
    }

    public T save(T entity) {
        if (entity.getId() != null) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
        return entity;
    }

    public T loadById(Long entityId) throws ObjectRetrievalFailureException {
        T entity = entityManager.find(prototype, entityId);
        if (entity == null) {
            throw new ObjectRetrievalFailureException(prototype, entityId);
        }
        return entity;
    }

    public List<T> loadAll() {
        Query query = entityManager.createQuery("select obj from " + entityName + " obj order by obj.id");
        return query.getResultList();
    }

    public void delete(final T entity) {
        T loadedEntity = loadById(entity.getId());
        entityManager.remove(loadedEntity);
    }

    protected final T newPrototype(Class<T> cl) throws IllegalArgumentException {
        try {
            return cl.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
