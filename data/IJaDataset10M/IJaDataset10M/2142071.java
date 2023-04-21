package org.openedc.web.showcase.service.cmp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.openedc.core.domain.service.exceptions.NonexistentEntityException;
import org.openedc.core.domain.service.exceptions.PreexistingEntityException;
import org.openedc.core.persistence.Persistent;
import org.openedc.core.persistence.QueryParam;

/**
 *
 * @param <E> 
 * @author openedc
 */
public class StatelessEntityService<E extends Persistent> {

    @PersistenceContext
    private EntityManager em;

    private Class<E> entityType;

    public StatelessEntityService(Class<E> entityType) {
        this.entityType = entityType;
    }

    protected void create(E e) throws PreexistingEntityException, Exception {
        if (e == null) {
            throw new IllegalStateException("Entity reference should not be null..");
        }
        try {
            em.persist(e);
        } catch (Exception ex) {
            if (find((Long) e.getId()) != null) {
                throw new PreexistingEntityException("The " + e.getClass().getSimpleName() + " with id " + " already exists.", ex);
            }
            throw ex;
        }
    }

    protected void edit(E e) throws NonexistentEntityException, Exception {
        if (e == null) {
            throw new IllegalStateException("Entity reference should not be null..");
        }
        try {
            e = em.merge(e);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = (Long) e.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The " + e.getClass().getSimpleName() + " with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    protected void destroy(Long id) throws NonexistentEntityException {
        E e = null;
        try {
            e = (E) em.getReference(entityType, id);
            e.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The " + e.getClass().getSimpleName() + " with id " + id + " no longer exists.", enfe);
        }
        em.remove(e);
    }

    protected List<E> findAll() throws Exception {
        return findAll(true, -1, -1);
    }

    protected List<E> findAll(int maxResults, int firstResult) throws Exception {
        return findAll(false, maxResults, firstResult);
    }

    protected List<E> findAll(boolean all, int maxResults, int firstResult) throws Exception {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityType));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    protected E find(Long id) throws Exception {
        return (E) em.find(entityType, id);
    }

    protected void store(E e) throws Exception {
        if (!exists(e)) {
            create(e);
        } else {
            edit(e);
        }
    }

    protected boolean exists(E e) {
        if (e == null) {
            throw new IllegalStateException("Entity reference should not be null..");
        }
        boolean exists = false;
        Object id = null;
        id = e.getId();
        if (id == null) {
            return exists;
        }
        try {
            em.getReference(entityType, id);
            exists = true;
        } catch (EntityNotFoundException enfe) {
            exists = false;
        }
        return exists;
    }

    protected E findByNamedQuery(String namedQueryName, QueryParam[] params) throws Exception {
        Query query = em.createNamedQuery(namedQueryName);
        for (QueryParam each : params) {
            query.setParameter(each.getName(), each.getValue());
        }
        return (E) query.getSingleResult();
    }

    protected int getCount() throws Exception {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<E> rt = cq.from(entityType);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    protected E getNextInstance() throws Exception {
        E instance = (E) entityType.newInstance();
        instance.setId(new Long(getCount() + 1));
        return instance;
    }

    protected E getInstance() throws Exception {
        E instance = (E) entityType.newInstance();
        return instance;
    }
}
