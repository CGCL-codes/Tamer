package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jkook.vetan.model.hrm.HsHrEmpEmergencyContacts;

/**
 *
 * @author kirank
 */
@Stateless
public class HsHrEmpEmergencyContactsFacade implements HsHrEmpEmergencyContactsFacadeLocal, HsHrEmpEmergencyContactsFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    /** Creates a new instance of HsHrEmpEmergencyContactsFacade */
    public HsHrEmpEmergencyContactsFacade() {
    }

    public void create(HsHrEmpEmergencyContacts hsHrEmpEmergencyContacts) {
        em.persist(hsHrEmpEmergencyContacts);
    }

    public void edit(HsHrEmpEmergencyContacts hsHrEmpEmergencyContacts) {
        em.merge(hsHrEmpEmergencyContacts);
    }

    public void destroy(HsHrEmpEmergencyContacts hsHrEmpEmergencyContacts) {
        em.merge(hsHrEmpEmergencyContacts);
        em.remove(hsHrEmpEmergencyContacts);
    }

    public HsHrEmpEmergencyContacts find(Object pk) {
        return (HsHrEmpEmergencyContacts) em.find(HsHrEmpEmergencyContacts.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from HsHrEmpEmergencyContacts as o").getResultList();
    }
}
