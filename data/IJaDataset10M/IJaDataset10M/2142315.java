package org.objectwiz.franchisemgmtDAO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.objectwiz.franchisemgmt.Planning;

/**
 *
 * @author xym
 */
@Stateless
public class PlanningFacade extends AbstractFacade<Planning> implements PlanningFacadeLocal {

    @PersistenceContext(unitName = "jee-franchise-mgmt")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanningFacade() {
        super(Planning.class);
    }
}
