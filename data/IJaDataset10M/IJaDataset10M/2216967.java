package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jkook.vetan.model.hrm.HsHrCompstructtree;

/**
 *
 * @author kirank
 */
@Stateless
public class HsHrCompstructtreeFacade implements HsHrCompstructtreeFacadeLocal, HsHrCompstructtreeFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    /** Creates a new instance of HsHrCompstructtreeFacade */
    public HsHrCompstructtreeFacade() {
    }

    public void create(HsHrCompstructtree hsHrCompstructtree) {
        em.persist(hsHrCompstructtree);
    }

    public void edit(HsHrCompstructtree hsHrCompstructtree) {
        em.merge(hsHrCompstructtree);
    }

    public void destroy(HsHrCompstructtree hsHrCompstructtree) {
        em.merge(hsHrCompstructtree);
        em.remove(hsHrCompstructtree);
    }

    public HsHrCompstructtree find(Object pk) {
        return (HsHrCompstructtree) em.find(HsHrCompstructtree.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from HsHrCompstructtree as o").getResultList();
    }
}
