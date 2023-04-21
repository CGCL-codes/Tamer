package edu.harvard.iq.safe.citest1.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author asone
 */
@Stateless
public class CachesFacade {

    @PersistenceContext(unitName = "safe-archive-project_MavenEnterpriseApp-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    public void create(Caches caches) {
        em.persist(caches);
    }

    public void edit(Caches caches) {
        em.merge(caches);
    }

    public void remove(Caches caches) {
        em.remove(em.merge(caches));
    }

    public Caches find(Object id) {
        return em.find(Caches.class, id);
    }

    public List<Caches> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Caches.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Caches> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Caches.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Caches> rt = cq.from(Caches.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
