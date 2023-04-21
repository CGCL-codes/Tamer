package org.sltech.punchclock.server.ejb.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.sltech.punchclock.entities.ClkGroup;

/**
 *
 * @author Juanjo
 */
@Stateless
public class ClkGroupFacade implements ClkGroupFacadeLocal, ClkGroupFacadeRemote {

    @PersistenceContext(unitName = "PunchClockPU")
    private EntityManager em;

    public void create(ClkGroup clkGroup) {
        em.persist(clkGroup);
    }

    public void edit(ClkGroup clkGroup) {
        em.merge(clkGroup);
    }

    public void remove(ClkGroup clkGroup) {
        em.remove(em.merge(clkGroup));
    }

    public ClkGroup find(Object id) {
        return em.find(ClkGroup.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ClkGroup> findAll() {
        return em.createQuery("select object(o) from ClkGroup as o").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ClkGroup> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from ClkGroup as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from ClkGroup as o").getSingleResult()).intValue();
    }
}
