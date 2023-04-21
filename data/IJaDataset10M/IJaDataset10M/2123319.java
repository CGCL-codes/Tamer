package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jkook.vetan.model.hrm.HsPrSalaryCurrencyDetail;

/**
 *
 * @author kirank
 */
@Stateless
public class HsPrSalaryCurrencyDetailFacade implements HsPrSalaryCurrencyDetailFacadeLocal, HsPrSalaryCurrencyDetailFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    /** Creates a new instance of HsPrSalaryCurrencyDetailFacade */
    public HsPrSalaryCurrencyDetailFacade() {
    }

    public void create(HsPrSalaryCurrencyDetail hsPrSalaryCurrencyDetail) {
        em.persist(hsPrSalaryCurrencyDetail);
    }

    public void edit(HsPrSalaryCurrencyDetail hsPrSalaryCurrencyDetail) {
        em.merge(hsPrSalaryCurrencyDetail);
    }

    public void destroy(HsPrSalaryCurrencyDetail hsPrSalaryCurrencyDetail) {
        em.merge(hsPrSalaryCurrencyDetail);
        em.remove(hsPrSalaryCurrencyDetail);
    }

    public HsPrSalaryCurrencyDetail find(Object pk) {
        return (HsPrSalaryCurrencyDetail) em.find(HsPrSalaryCurrencyDetail.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from HsPrSalaryCurrencyDetail as o").getResultList();
    }
}
