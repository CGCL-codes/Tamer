package org.obe.server.j2ee.ejb;

import org.obe.spi.service.ServerConfig;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * An abstract base class for session bean implementations.
 *
 * @author Adrian Price.
 * @ejb:bean generate="false"
 */
public abstract class AbstractSessionEJB extends AbstractEJB implements SessionBean {

    private static final long serialVersionUID = -733508684414612478L;

    protected SessionContext _ctx;

    /**
     * This method is called when the container picks this session object
     * and assigns it to a specific session object. Insert code here to
     * acquire any additional resources that it needs when it is in the
     * ready state.
     */
    public void ejbActivate() {
        if (getLogger().isDebugEnabled() && ServerConfig.isVerbose()) getLogger().debug("ejbActivate");
    }

    /**
     * This method is called when the container diassociates the bean
     * from the session object identity and puts the instance back into
     * the pool of available instances. Insert code to release any
     * resources that should not be held while the instance is in the
     * pool.
     */
    public void ejbPassivate() {
        if (getLogger().isDebugEnabled() && ServerConfig.isVerbose()) getLogger().debug("ejbPassivate");
    }

    public void ejbRemove() {
        if (getLogger().isDebugEnabled() && ServerConfig.isVerbose()) getLogger().debug("ejbRemove");
    }

    /**
     * Marks the current transaction for rollback only. N.B. must only be
     * called when the EJB has a valid transactional context.
     */
    protected void setRollbackOnly() {
        try {
            _ctx.setRollbackOnly();
        } catch (IllegalStateException e) {
            getLogger().error("Unable to set transaction to rollback only", e);
        }
    }

    /**
     * Sets the context of the bean.
     *
     * @param ctx The Bean's Context
     */
    public void setSessionContext(SessionContext ctx) {
        if (getLogger().isDebugEnabled() && ServerConfig.isVerbose()) getLogger().debug("setSessionContext");
        _ctx = ctx;
    }
}
