package ru.jcorp.smartstreets.core;

import ru.jcorp.smartstreets.entity.BaseEntity;
import ru.jcorp.smartstreets.services.DataService;
import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id: DataServiceImpl.java 134 2012-01-10 20:47:24Z jreznot $</p>
 *
 * @author Artamonov Yuriy
 */
public class DataServiceImpl implements DataService {

    private final EntityManagerFactory factory;

    public DataServiceImpl() {
        factory = Persistence.createEntityManagerFactory("smart-streets", System.getProperties());
    }

    @Override
    public EntityManager getEntityManager() {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.setFlushMode(FlushModeType.COMMIT);
        return entityManager;
    }

    @Override
    public void commit(BaseEntity entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (entity.getVersion() == null) em.persist(entity); else em.merge(entity);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(BaseEntity entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            BaseEntity managed = em.merge(entity);
            em.remove(managed);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public List loadEntities(String query) {
        EntityManager em = getEntityManager();
        EntityTransaction tr = em.getTransaction();
        List result;
        tr.begin();
        try {
            Query q = em.createQuery(query);
            result = q.getResultList();
            tr.commit();
        } catch (Exception ex) {
            if (tr.isActive()) tr.rollback();
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
        if (result != null) return result; else return Collections.emptyList();
    }

    @Override
    public List loadEntities(String query, Map<String, Object> params) {
        EntityManager em = getEntityManager();
        EntityTransaction tr = em.getTransaction();
        List result;
        tr.begin();
        try {
            Query q = em.createQuery(query);
            if (params != null) for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
                q.setParameter(paramEntry.getKey(), paramEntry.getValue());
            }
            result = q.getResultList();
            tr.commit();
        } catch (Exception ex) {
            if (tr.isActive()) tr.rollback();
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
        if (result != null) return result; else return Collections.emptyList();
    }
}
