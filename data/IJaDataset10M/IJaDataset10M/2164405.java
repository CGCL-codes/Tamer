package uk.org.ogsadai.service.axis.datasource;

import javax.xml.rpc.server.ServiceLifecycle;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.datasource.DataSourceResource;
import uk.org.ogsadai.service.axis.context.AxisOGSADAIContextInitializer;
import uk.org.ogsadai.service.axis.datasource.AxisDataSourceProvider;
import uk.org.ogsadai.service.axis.intrinsics.AxisIntrinsicsProvider;
import uk.org.ogsadai.service.axis.lifetime.AxisWSResourceLifetimeProvider;
import uk.org.ogsadai.service.axis.properties.AxisWSResourcePropertiesProvider;
import uk.org.ogsadai.service.axis.resolver.AxisWSEPRResolverProvider;
import uk.org.ogsadai.service.axis.datasource.AxisDataSourceServicePortType;

/**
 * Axis-compliant data source service. Service operation
 * implementations are provided in the associated provider class -
 * this class just passes operation invocations on to the provider. 
 *
 * @author The OGSA-DAI Project Team.
 */
public class AxisDataSourceService implements AxisDataSourceServicePortType, ServiceLifecycle {

    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2002-2010.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(AxisDataSourceService.class);

    /** 
     * Invokes {@link AxisOGSADAIContextInitializer#initialize}.
     */
    @Override
    public void init(Object context) {
        LOG.debug("Initializing AxisDataSourceService...");
        try {
            AxisOGSADAIContextInitializer.initialize();
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
     * @see uk.org.ogsadai.service.axis.lifetime.AxisWSResourceLifetimeProvider#setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime)
     */
    public org.oasis.wsrf.lifetime.SetTerminationTimeResponse setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime setTerminationTimeRequest) throws java.rmi.RemoteException, org.oasis.wsrf.lifetime.UnableToSetTerminationTimeFaultType, org.oasis.wsrf.lifetime.ResourceUnknownFaultType, org.oasis.wsrf.lifetime.TerminationTimeChangeRejectedFaultType {
        AxisWSResourceLifetimeProvider provider = new AxisWSResourceLifetimeProvider(DataSourceResource.class);
        return provider.setTerminationTime(setTerminationTimeRequest);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.lifetime.AxisWSResourceLifetimeProvider#destroy(org.oasis.wsrf.lifetime.Destroy) 
     */
    public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy destroyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.lifetime.ResourceNotDestroyedFaultType, org.oasis.wsrf.lifetime.ResourceUnknownFaultType {
        AxisWSResourceLifetimeProvider provider = new AxisWSResourceLifetimeProvider(DataSourceResource.class);
        return provider.destroy(destroyRequest);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.properties.AxisWSResourcePropertiesProvider#queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element) 
     */
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType {
        AxisWSResourcePropertiesProvider provider = new AxisWSResourcePropertiesProvider(DataSourceResource.class);
        return provider.queryResourceProperties(queryResourcePropertiesRequest);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.properties.AxisWSResourcePropertiesProvider#getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element)
     */
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        AxisWSResourcePropertiesProvider provider = new AxisWSResourcePropertiesProvider(DataSourceResource.class);
        return provider.getMultipleResourceProperties(getMultipleResourcePropertiesRequest);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.properties.AxisWSResourcePropertiesProvider#getResourceProperty(javax.xml.namespace.QName)
     */
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        AxisWSResourcePropertiesProvider provider = new AxisWSResourcePropertiesProvider(DataSourceResource.class);
        return provider.getResourceProperty(getResourcePropertyRequest);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.intrinsics.AxisIntrinsicsProvider#getVersion(uk.org.ogsadai.service.axis.intrinsics.GetVersion)
     */
    public uk.org.ogsadai.service.axis.intrinsics.GetVersionResponse getVersion(uk.org.ogsadai.service.axis.intrinsics.GetVersion parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.axis.intrinsics.ServerFaultType {
        AxisIntrinsicsProvider provider = new AxisIntrinsicsProvider(DataSourceResource.class);
        return provider.getVersion(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.intrinsics.AxisIntrinsicsProvider#listResources(uk.org.ogsadai.service.axis.intrinsics.ListResources)
     */
    public uk.org.ogsadai.service.axis.intrinsics.ListResourcesResponse listResources(uk.org.ogsadai.service.axis.intrinsics.ListResources parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.axis.intrinsics.ServerFaultType {
        AxisIntrinsicsProvider provider = new AxisIntrinsicsProvider(DataSourceResource.class);
        return provider.listResources(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.resolver.AxisWSEPRResolverProvider#resolve(uk.org.ogsadai.service.axis.resolver.Resolve)
     */
    public uk.org.ogsadai.service.axis.resolver.ResolveResponse resolve(uk.org.ogsadai.service.axis.resolver.Resolve parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.axis.resolver.ServerFaultType, uk.org.ogsadai.service.axis.resolver.ResourceUnknownFaultType {
        AxisWSEPRResolverProvider provider = new AxisWSEPRResolverProvider();
        return provider.resolve(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.datasource.AxisDataSourceProvider#getFully(uk.org.ogsadai.service.axis.datasource.GetFully)
     */
    public uk.org.ogsadai.service.axis.datasource.GetFullyResponse getFully(uk.org.ogsadai.service.axis.datasource.GetFully parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.axis.datasource.ServerFaultType, uk.org.ogsadai.service.axis.datasource.ClientFaultType, uk.org.ogsadai.service.axis.datasource.ResourceUnknownFaultType {
        AxisDataSourceProvider provider = new AxisDataSourceProvider();
        return provider.getFully(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.datasource.AxisDataSourceProvider#getNBlocks(uk.org.ogsadai.service.axis.datasource.GetNBlocks)
     */
    public uk.org.ogsadai.service.axis.datasource.GetNBlocksResponse getNBlocks(uk.org.ogsadai.service.axis.datasource.GetNBlocks parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.axis.datasource.ServerFaultType, uk.org.ogsadai.service.axis.datasource.ClientFaultType, uk.org.ogsadai.service.axis.datasource.ResourceUnknownFaultType {
        AxisDataSourceProvider provider = new AxisDataSourceProvider();
        return provider.getNBlocks(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.datasource.AxisDataSourceProvider#getNBlocks(uk.org.ogsadai.service.axis.datasource.GetNBlocks)
     */
    public uk.org.ogsadai.service.axis.datasource.GetNBlocksNBResponse getNBlocksNB(uk.org.ogsadai.service.axis.datasource.GetNBlocksNB parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.axis.datasource.ServerFaultType, uk.org.ogsadai.service.axis.datasource.ClientFaultType, uk.org.ogsadai.service.axis.datasource.ResourceUnknownFaultType {
        AxisDataSourceProvider provider = new AxisDataSourceProvider();
        return provider.getNBlocksNB(parameters);
    }

    /**
     * (non-JavaDoc)
     * @see uk.org.ogsadai.service.axis.datasource.AxisDataSourceProvider#getBlock(uk.org.ogsadai.service.axis.datasource.GetBlock)
     */
    public uk.org.ogsadai.service.axis.datasource.GetBlockResponse getBlock(uk.org.ogsadai.service.axis.datasource.GetBlock parameters) throws java.rmi.RemoteException, uk.org.ogsadai.service.axis.datasource.ServerFaultType, uk.org.ogsadai.service.axis.datasource.ClientFaultType, uk.org.ogsadai.service.axis.datasource.ResourceUnknownFaultType {
        AxisDataSourceProvider provider = new AxisDataSourceProvider();
        return provider.getBlock(parameters);
    }
}
