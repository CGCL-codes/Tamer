package com.markatta.hund.service;

import com.google.inject.Inject;
import com.markatta.hund.model.Status;
import com.markatta.hund.model.StatusType;
import com.markatta.hund.util.GenericsUtil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author johan
 */
public class AbstractEntityService<T> implements EntityService<T> {

    private Class<T> entityClass = GenericsUtil.getTypeClass(getClass(), 0);

    @Inject
    protected Database database;

    public T find(long id) {
        EntityManager em = database.createEntityManager();
        T result = (T) em.find(entityClass, id);
        return result;
    }

    public List<T> getAllNonOk() {
        EntityManager em = database.createEntityManager();
        Query hostQuery = em.createQuery("SELECT a FROM " + entityClass.getName() + " a " + "WHERE a.status.type != :status ORDER BY a.status.type");
        hostQuery.setParameter("status", StatusType.OK);
        return hostQuery.getResultList();
    }

    public long totalCount() {
        EntityManager em = database.createEntityManager();
        return (Long) em.createQuery("SELECT COUNT(h) FROM " + entityClass.getName() + " h").getSingleResult();
    }

    public long countStatus(StatusType type) {
        EntityManager em = database.createEntityManager();
        Query okHostQuery = em.createQuery("SELECT COUNT(h) FROM " + entityClass.getName() + " h WHERE h.status.type = :status");
        okHostQuery.setParameter("status", type);
        return (Long) okHostQuery.getSingleResult();
    }

    public List<T> getAll() {
        EntityManager em = database.createEntityManager();
        Query query = em.createQuery("SELECT h FROM " + entityClass.getName() + " h ORDER BY h.label");
        List<T> hosts = query.getResultList();
        hosts.size();
        return hosts;
    }

    public T saveOrUpdate(T host) {
        EntityManager em = database.createEntityManager();
        em.getTransaction().begin();
        host = em.merge(host);
        em.getTransaction().commit();
        return host;
    }
}
