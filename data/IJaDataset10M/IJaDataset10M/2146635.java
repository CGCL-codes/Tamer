package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jkook.vetan.model.hrm.HsHrLocation;
import jkook.vetan.utils.HrSequenceFacadeLocal;

/**
 *
 * @author kirank
 */
@Stateless
public class HsHrLocationFacade implements HsHrLocationFacadeLocal, HsHrLocationFacadeRemote {

    @EJB
    private HrSequenceFacadeLocal hrSequenceFacade;

    @PersistenceContext
    private EntityManager em;

    /** Creates a new instance of HsHrLocationFacade */
    public HsHrLocationFacade() {
    }

    public void create(HsHrLocation hsHrLocation) {
        hsHrLocation.setLocCode("LOC" + hrSequenceFacade.getNextID("HsHrLocation"));
        em.persist(hsHrLocation);
    }

    public void edit(HsHrLocation hsHrLocation) {
        em.merge(hsHrLocation);
    }

    public void destroy(HsHrLocation hsHrLocation) {
        em.merge(hsHrLocation);
        em.remove(hsHrLocation);
    }

    public HsHrLocation find(Object pk) {
        return (HsHrLocation) em.find(HsHrLocation.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from HsHrLocation as o").getResultList();
    }
}
