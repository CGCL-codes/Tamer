package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jkook.vetan.model.hrm.HsHrCurrencyType;

/**
 *
 * @author kirank
 */
@Stateless
public class HsHrCurrencyTypeFacade implements HsHrCurrencyTypeFacadeLocal, HsHrCurrencyTypeFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    /** Creates a new instance of HsHrCurrencyTypeFacade */
    public HsHrCurrencyTypeFacade() {
    }

    public void create(HsHrCurrencyType hsHrCurrencyType) {
        em.persist(hsHrCurrencyType);
    }

    public void edit(HsHrCurrencyType hsHrCurrencyType) {
        em.merge(hsHrCurrencyType);
    }

    public void destroy(HsHrCurrencyType hsHrCurrencyType) {
        em.merge(hsHrCurrencyType);
        em.remove(hsHrCurrencyType);
    }

    public HsHrCurrencyType find(Object pk) {
        return (HsHrCurrencyType) em.find(HsHrCurrencyType.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from HsHrCurrencyType as o").getResultList();
    }
}
