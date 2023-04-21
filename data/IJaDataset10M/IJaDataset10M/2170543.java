package dsb.bar.tks.server.testbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import dsb.bar.tks.server.testbeans.AbstractStatelessBeanInterface;

@Stateless
public abstract class AbstractStatelessBean implements AbstractStatelessBeanInterface {

    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
