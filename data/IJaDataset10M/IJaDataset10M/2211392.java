package org.objectwiz.franchisemgmtDAO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.objectwiz.franchisemgmt.MoralEntity;

/**
 *
 * @author xym
 */
@Stateless
public class MoralEntityFacade extends AbstractFacade<MoralEntity> implements MoralEntityFacadeLocal {

    @PersistenceContext(unitName = "jee-franchise-mgmt")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public MoralEntityFacade() {
        super(MoralEntity.class);
    }
}
