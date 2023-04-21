package org.sltech.punchclock.server.ejb.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.sltech.punchclock.entities.ClkPersonPicture;

/**
 *
 * @author Juanjo
 */
@Stateless
public class ClkPersonPictureFacade implements ClkPersonPictureFacadeLocal, ClkPersonPictureFacadeRemote {

    @PersistenceContext(unitName = "PunchClockPU")
    private EntityManager em;

    public void create(ClkPersonPicture clkPersonPicture) {
        em.persist(clkPersonPicture);
    }

    public void edit(ClkPersonPicture clkPersonPicture) {
        em.merge(clkPersonPicture);
    }

    public void remove(ClkPersonPicture clkPersonPicture) {
        em.remove(em.merge(clkPersonPicture));
    }

    public ClkPersonPicture find(Object id) {
        return em.find(ClkPersonPicture.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ClkPersonPicture> findAll() {
        return em.createQuery("select object(o) from ClkPersonPicture as o").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ClkPersonPicture> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from ClkPersonPicture as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from ClkPersonPicture as o").getSingleResult()).intValue();
    }
}
