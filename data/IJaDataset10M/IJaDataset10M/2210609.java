package org.personalsmartspace.cm.broker.impl.osgi;

import org.personalsmartspace.cm.api.pss3p.ContextBrokerException;
import org.personalsmartspace.cm.api.pss3p.ContextDBException;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.broker.impl.CtxBrokerPlatformImpl;
import org.personalsmartspace.cm.broker.impl.api.ICtxBrokerServiceFinder;
import org.personalsmartspace.cm.db.api.platform.ICtxDBManager;
import org.personalsmartspace.cm.db.impl.CtxDBManager;
import org.personalsmartspace.cm.db.impl.util.HibernateUtil;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.spm.identity.api.platform.IdentityMgmtException;
import org.personalsmartspace.spm.identity.impl.StubIdentityManagement;

/**
 * Simple implementation of {@link ICtxBrokerServiceFinder} for unit testing.
 * 
 * Only one instance of this class should be created per unit test due to the
 * overhead of creating db manager instances when create new Hibernate
 * sessions.
 *
 * @author <a href="mailto:phdn@users.sourceforge.net">phdn</a>
 * @see <a href="http://www.knopflerfish.org/osgi_service_tutorial.html">Knopflerfish Service Tutorial</a>
 */
public class CtxBrokerTestServiceFinder extends BaseServiceFinder {

    private final PSSLog log = new PSSLog(this);

    {
        HibernateUtil.shutdown();
        try {
            this.setIdentityManagementService(new StubIdentityManagement());
            final ICtxDBManager ctxDBManager = new CtxDBManager();
            ctxDBManager.getConfigurator().setResetDbEnabled(true);
            ctxDBManager.getConfigurator().setOperatorId(this.getIdentityManagementService().getPrivateDigitalPersonalIdentifier().toUriString());
            ctxDBManager.start();
            this.setCtxDBManagerService(ctxDBManager);
        } catch (IdentityMgmtException e) {
            log.error("error creating new StubIdentityManagement", e);
        } catch (ContextDBException e) {
            log.error("error creating new context db manager", e);
        } catch (ContextBrokerException e) {
            log.error("error getting identity management service", e);
        }
    }

    /**
     * Gets a platform context broker implementation.
     * 
     * @return An instance of {@link ICtxBroker}.
     * @throws ContextBrokerException Wraps the {@link Throwable} thrown by the OSGi container.
     */
    @Override
    public ICtxBroker getPlatformCtxBrokerService() throws ContextBrokerException {
        try {
            super.getPlatformCtxBrokerService();
        } catch (ContextBrokerException e) {
            setCtxBrokerService(new CtxBrokerPlatformImpl(this));
        }
        return super.getPlatformCtxBrokerService();
    }
}
