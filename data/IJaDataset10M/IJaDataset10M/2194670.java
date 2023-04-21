package au.edu.uq.itee.maenad.dataaccess.jpa;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import au.edu.uq.itee.maenad.dataaccess.Dao;
import au.edu.uq.itee.maenad.dataaccess.Page;

/**
 * A generic implementation of DAOs for use with the Java Persistence API (JPA).
 *
 * This class implements all methods of the Dao interface using the JPA. It thus
 * provides the means of implementing a specific DAO by just binding the type
 * parameter to a JPA annotated entity.
 *
 * The actual work is done by an EntityManager object from the JPA. This is
 * fetched from the EntityManagerSource passed in as a parameter to the constructor.
 * Every operation done through an instance of this class is handled in an automatic
 * transaction.
 *
 * @param <T> The entity type we manage.
 */
public abstract class JpaDao<T> implements Dao<T> {

    protected final EntityManagerSource entityManagerSource;

    public JpaDao(EntityManagerSource entityManagerSource) {
        this.entityManagerSource = entityManagerSource;
    }

    @Override
    public T load(Object id) {
        try {
            boolean ourTx = beginTransaction();
            @SuppressWarnings("unchecked") Class<T> actualClassParameter = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            EntityManager entityManager = entityManagerSource.getEntityManager();
            T result = entityManager.find(actualClassParameter, id);
            if (result != null) {
                entityManager.refresh(result);
            }
            commitTransaction(ourTx);
            return result;
        } catch (RuntimeException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Error err) {
            rollbackTransaction();
            throw err;
        }
    }

    @Override
    public void save(T object) {
        try {
            boolean ourTx = beginTransaction();
            entityManagerSource.getEntityManager().persist(object);
            commitTransaction(ourTx);
        } catch (RuntimeException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Error err) {
            rollbackTransaction();
            throw err;
        }
    }

    @Override
    public T update(T object) {
        try {
            boolean ourTx = beginTransaction();
            T result = entityManagerSource.getEntityManager().merge(object);
            commitTransaction(ourTx);
            return result;
        } catch (RuntimeException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Error err) {
            rollbackTransaction();
            throw err;
        }
    }

    @Override
    public void delete(T object) {
        try {
            boolean ourTx = beginTransaction();
            entityManagerSource.getEntityManager().remove(object);
            commitTransaction(ourTx);
        } catch (RuntimeException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Error err) {
            rollbackTransaction();
            throw err;
        }
    }

    @Override
    public void refresh(T object) {
        try {
            boolean ourTx = beginTransaction();
            entityManagerSource.getEntityManager().refresh(object);
            commitTransaction(ourTx);
        } catch (RuntimeException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Error err) {
            rollbackTransaction();
            throw err;
        }
    }

    @Override
    public List<T> getAll() {
        try {
            boolean ourTx = beginTransaction();
            @SuppressWarnings("unchecked") List<T> result = getQueryForAll().getResultList();
            commitTransaction(ourTx);
            return result;
        } catch (RuntimeException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Error err) {
            rollbackTransaction();
            throw err;
        }
    }

    @Override
    public Page<T> getPage(int offset, int limit) {
        try {
            boolean ourTx = beginTransaction();
            Query query = getQueryForAll();
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            @SuppressWarnings("unchecked") List<T> result = query.getResultList();
            int count = ((Number) entityManagerSource.getEntityManager().createQuery("SELECT COUNT(o) " + "FROM " + getEntityName() + " o").getSingleResult()).intValue();
            commitTransaction(ourTx);
            return new Page<T>(result, offset, limit, count);
        } catch (RuntimeException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Error err) {
            rollbackTransaction();
            throw err;
        }
    }

    private Query getQueryForAll() {
        Class<T> actualClassParameter = getActualClassParameter();
        String orderExpression = "";
        try {
            if (actualClassParameter.getField("id") != null) {
                orderExpression = "ORDER BY id";
            }
        } catch (Exception ex) {
        }
        Query query = entityManagerSource.getEntityManager().createQuery("SELECT o " + "FROM " + getEntityName() + " o " + orderExpression);
        return query;
    }

    protected Class<T> getActualClassParameter() {
        @SuppressWarnings("unchecked") Class<T> actualClassParameter = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return actualClassParameter;
    }

    protected String getEntityName() {
        Class<T> actualClassParameter = getActualClassParameter();
        Entity annotation = actualClassParameter.getAnnotation(Entity.class);
        String tableName = (annotation.name() != null) && (annotation.name().length() > 0) ? annotation.name() : actualClassParameter.getSimpleName();
        return tableName;
    }

    /**
     * Starts a new transaction unless one is already running.
     *
     * @return true iff a new transaction was started.
     */
    protected boolean beginTransaction() {
        EntityTransaction transaction = this.entityManagerSource.getEntityManager().getTransaction();
        if (transaction.isActive()) {
            return false;
        } else {
            transaction.begin();
            return true;
        }
    }

    /**
     * Commits the transaction if we opened it.
     *
     * @param ourTx Flag denoting if we opened the current transaction.
     */
    protected void commitTransaction(boolean ourTx) {
        if (ourTx) {
            this.entityManagerSource.getEntityManager().getTransaction().commit();
        }
    }

    protected void rollbackTransaction() {
        final EntityTransaction transaction = this.entityManagerSource.getEntityManager().getTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}
