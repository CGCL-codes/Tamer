package uk.org.ogsadai.service.gt.session;

import javax.xml.rpc.server.ServiceLifecycle;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.session.SessionResource;
import uk.org.ogsadai.service.gt.context.GTOGSADAIContextInitializer;
import uk.org.ogsadai.service.gt.intrinsics.GTIntrinsicsProvider;
import uk.org.ogsadai.service.gt.resolver.GTWSEPRResolverProvider;

/**
 * GT-compliant session management service. Service operation
 * implementations are provided in the associated provider class -
 * this class just passes operation invocations on to the provider. 
 * <p>
 * WS-ResourceLifetime and WS-ResourceProperties operations are
 * provided by GT providers hence there are no methods for
 * these operations in this class.
 *
 * @author The OGSA-DAI Project Team.
 */
public class GTSessionManagementService implements ServiceLifecycle {

    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2002-2010.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(GTSessionManagementService.class);

    /** 
     * Invokes {@link GTOGSADAIContextInitializer#initialize}.
     */
    @Override
    public void init(Object context) {
        LOG.debug("Initializing GTSessionManagementService...");
        try {
            GTOGSADAIContextInitializer.initialize();
        } catch (Throwable e) {
            LOG.fatal(e, true);
        }
        LOG.debug("...initialized!");
    }

    /** No-op. */
    @Override
    public void destroy() {
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.gt.intrinsics.GTIntrinsicsProvider#getVersion(uk.org.ogsadai.service.gt.intrinsics.GetVersion)
     */
    public uk.org.ogsadai.service.gt.intrinsics.GetVersionResponse getVersion(uk.org.ogsadai.service.gt.intrinsics.GetVersion parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.gt.intrinsics.ServerFaultType {
        GTIntrinsicsProvider provider = new GTIntrinsicsProvider(SessionResource.class);
        return provider.getVersion(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.gt.intrinsics.GTIntrinsicsProvider#listResources(uk.org.ogsadai.service.gt.intrinsics.ListResources)
     */
    public uk.org.ogsadai.service.gt.intrinsics.ListResourcesResponse listResources(uk.org.ogsadai.service.gt.intrinsics.ListResources parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.gt.intrinsics.ServerFaultType {
        GTIntrinsicsProvider provider = new GTIntrinsicsProvider(SessionResource.class);
        return provider.listResources(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.gt.resolver.GTWSEPRResolverProvider#resolve(uk.org.ogsadai.service.gt.resolver.Resolve)
     */
    public uk.org.ogsadai.service.gt.resolver.ResolveResponse resolve(uk.org.ogsadai.service.gt.resolver.Resolve parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.gt.resolver.ServerFaultType, uk.org.ogsadai.service.gt.resolver.ResourceUnknownFaultType {
        GTWSEPRResolverProvider provider = new GTWSEPRResolverProvider();
        return provider.resolve(parameters);
    }
}
