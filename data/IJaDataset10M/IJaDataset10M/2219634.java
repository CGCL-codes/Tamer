package televizorKrupkoAA.dataKrupkoAA;

import engine.model.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author dosER
 */
@Stateless
public class KineskopKrupkoAAFacade extends AbstractFacade<KineskopKrupkoAA> {

    @PersistenceContext(unitName = "Televizor-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public KineskopKrupkoAAFacade() {
        super(KineskopKrupkoAA.class);
    }
}
