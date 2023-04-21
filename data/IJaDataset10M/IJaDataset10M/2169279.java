package com.intel.gpe.services.vtsf.gtk;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.Stub;
import org.apache.axis.MessageContext;
import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.axis.gsi.GSIConstants;
import org.globus.gsi.jaas.JaasGssUtil;
import org.globus.workspace.WorkspaceConstants;
import org.globus.workspace.generated.WorkspaceFactoryPortType;
import org.globus.workspace.generated.WorkspaceFactoryServiceAddressingLocator;
import org.globus.workspace.generated.WorkspacePortType;
import org.globus.workspace.generated.WorkspaceServiceAddressingLocator;
import org.globus.workspace.generated.metadata.AggregateVirtualWorkspace_Type;
import org.globus.workspace.generated.metadata.logistics.Logistics;
import org.globus.workspace.generated.negotiable.AggregateWorkspaceDeployment_Type;
import org.globus.workspace.generated.types.WorkspaceFault;
import org.globus.workspace.generated.types.WorkspaceInformation;
import org.globus.workspace.generated.types.WorkspaceMetadataFault;
import org.globus.workspace.generated.types.WorkspaceReference;
import org.globus.workspace.generated.types.WorkspaceResourceRequestDeniedFault;
import org.globus.workspace.generated.types.WorkspaceSchedulingFault;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.encoding.SerializationException;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.HostAuthorization;
import org.globus.wsrf.utils.AddressingUtils;
import org.ietf.jgss.GSSCredential;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.unigrids.services.atomic.tsf.TSRNotCreatedFaultType;
import org.unigrids.services.atomic.vtsf.CreateVTSR;
import org.unigrids.services.atomic.vtsf.CreateVTSRResponse;
import org.w3c.dom.Element;
import com.intel.gpe.clients.api.RegistryClient;
import com.intel.gpe.clients.api.TargetSystemClient;
import com.intel.gpe.clients.api.WSLTClient;
import com.intel.gpe.clients.gpe.DummyCache;
import com.intel.gpe.clients.gpe4gtk.GPE4GTKConstants;
import com.intel.gpe.clients.gpe4gtk.ISecuritySetup;
import com.intel.gpe.clients.gpe4gtk.registry.RegistryClientImpl;
import com.intel.gpe.clients.gtk.TargetSystemRegistrationImpl;
import com.intel.gpe.constants.ResourceTypes;
import com.intel.gpe.idb.RegistryType;
import com.intel.gpe.idb.StaticPropertiesType;
import com.intel.gpe.services.common.gtk.DelegatedSecuritySetup;
import com.intel.gpe.services.osrepository.Build;
import com.intel.gpe.services.osrepository.Deploy;
import com.intel.gpe.services.osrepository.DeployResponse;
import com.intel.gpe.services.osrepository.DeploymentDefinition;
import com.intel.gpe.services.osrepository.FileTransferServiceType;
import com.intel.gpe.services.osrepository.GetBuildsFaultType;
import com.intel.gpe.services.osrepository.GetBuildsRequest;
import com.intel.gpe.services.osrepository.GetBuildsResponse;
import com.intel.gpe.services.osrepository.OSDiskTransferType;
import com.intel.gpe.services.osrepository.OSRepository;
import com.intel.gpe.services.osrepository.OSRespositoryServiceAddressingLocator;
import com.intel.gpe.services.osrepository.TargetSystemType;
import com.intel.gpe.services.tss.gtk.BaseTargetSystemResource;
import com.intel.gpe.services.vtsf.workspace.BuildAggregateVirtualWorkspaceType;
import com.intel.gpe.services.vtsf.workspace.BuildAggregateWorkspaceDeployment;
import com.intel.gpe.util.FaultUtil;
import com.intel.util.soap.SOAPUtil;

/**
 * @author Sai Srinivas Dharanikota
 * @version $Id: BaseVirtualTargetSystemFactory.java,v 1.13 2006/10/24 10:30:45
 *          sdharani Exp $
 */
public abstract class BaseVirtualTargetSystemFactory {

    private static Logger logger = Logger.getLogger("com.intel.gpe");

    protected abstract ResourceKey createVirtualTargetSystemResource(Element spe, BaseVTSFConfiguration configuration, Calendar tt, EndpointReferenceType workspRef, EndpointReferenceType osImageTransferEPR) throws Exception;

    protected abstract Resource getVirtualTargetSystem(ResourceKey key, BaseVTSFConfiguration configuration) throws ResourceException, NamingException;

    protected abstract Object collectVirtualTargetSystemProperties(Resource resource) throws SerializationException;

    protected abstract BaseVTSFConfiguration getVTSFConfiguration(ResourceContext ctx) throws NamingException;

    public CreateVTSRResponse createVTSR(CreateVTSR createVTSRRequest) throws RemoteException {
        BaseVTSFConfiguration configuration;
        StaticPropertiesType staticProperties = null;
        String wsURL = null;
        String imageName = null;
        java.net.URI gridFTPURI = null;
        ResourceKey virtualTargetSystemKey = null;
        EndpointReferenceType epr = null;
        Resource virtualTargetSystem = null;
        WorkspaceReference workspaceReference = null;
        MessageElement wpReferenceElement[] = { new MessageElement() };
        String userDn = org.globus.wsrf.security.SecurityManager.getManager().getCaller();
        logger.info("Creating virtual target system by user: " + userDn);
        CreateVTSRResponse response = new CreateVTSRResponse();
        try {
            ResourceContext ctx = ResourceContext.getResourceContext();
            configuration = getVTSFConfiguration(ctx);
        } catch (NamingException e) {
            throw new RemoteException("Cannot get configuration", e);
        }
        if (configuration.getAdminList().size() > 0 && !configuration.getAdminList().contains(userDn)) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), "The user \"" + userDn + "\" cannot create virtual target systems");
        }
        MessageElement spe = null;
        try {
            spe = createVTSRRequest.get_any()[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), "Malformed createVTSR request");
        }
        try {
            staticProperties = (StaticPropertiesType) spe.getObjectValue(StaticPropertiesType.class);
        } catch (Exception e) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Parsing error in static properties configuration file");
        }
        MessageElement imageNameElement = null;
        try {
            imageNameElement = createVTSRRequest.get_any()[2];
        } catch (Exception e) {
            e.printStackTrace();
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), "Malformed createVTSR request");
        }
        try {
            imageName = (String) imageNameElement.getObjectValue(String.class);
        } catch (Exception e) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Cannot get the value of image name");
        }
        DeployResponse deployResponse = null;
        Build[] builds = null;
        String repositoryURLs[] = configuration.getRepositoryURLs().split(" ");
        if ((repositoryURLs.length > 1)) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), null, "multiple repositories not supported");
        } else if (repositoryURLs.length == 0) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), null, "repository service url not specified");
        } else if (repositoryURLs.length == 1) {
            try {
                builds = getOSRespositoryServicePort(new URL(repositoryURLs[0])).getBuilds(new GetBuildsRequest()).getBuild();
            } catch (Exception e) {
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Unable to get the meta data of the image");
            }
        }
        Build imageMetaData = null;
        for (Build build : builds) {
            if (build.getImageConfiguration().getName().contentEquals(imageName)) {
                imageMetaData = build;
            }
        }
        imageMetaData.getImageConfiguration().getExecutionSystem().setIndividualPhysicalMemory(staticProperties.getExecutionSystem().getIndividualPhysicalMemory());
        staticProperties.setTSIClient(imageMetaData.getImageConfiguration().getTSIClient());
        staticProperties.setApplication(imageMetaData.getImageConfiguration().getApplication());
        staticProperties.setExecutionSystem(imageMetaData.getImageConfiguration().getExecutionSystem());
        String destimageName = UUIDGenFactory.getUUIDGen().nextUUID();
        BuildAggregateVirtualWorkspaceType buildAggregateVirtualWorkspaceType = new BuildAggregateVirtualWorkspaceType(imageMetaData.getImageConfiguration(), destimageName);
        BuildAggregateWorkspaceDeployment buildAggregateWorkspaceDeployment = new BuildAggregateWorkspaceDeployment(staticProperties);
        AggregateVirtualWorkspace_Type aggregateVirtualWorkspace_Type = buildAggregateVirtualWorkspaceType.getAggregateVirtualWorkspace_Type();
        AggregateWorkspaceDeployment_Type aggregateWorkspaceDeployment_Type;
        MessageElement wsURLElement = null;
        try {
            wsURLElement = createVTSRRequest.get_any()[1];
        } catch (Exception e) {
            e.printStackTrace();
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), "Malformed createVTSR request");
        }
        try {
            wsURL = (String) wsURLElement.getObjectValue(String.class);
        } catch (Exception e) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), "Cannot get the value of workspace url");
        }
        if (wsURL.compareTo("") != 0) {
            MessageElement gridFTPURIElement = null;
            try {
                gridFTPURIElement = createVTSRRequest.get_any()[3];
            } catch (Exception e) {
                e.printStackTrace();
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), "Malformed createVTSR request");
            }
            try {
                gridFTPURI = new java.net.URI((String) gridFTPURIElement.getObjectValue(String.class));
            } catch (Exception e) {
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Cannot get the value of grid ftp url");
            }
            try {
                aggregateVirtualWorkspace_Type.getVwSet(0).getVirtualWorkspace().getDefinition().getModules().getDisk(0).setUri(new URI("file://" + gridFTPURI.getPath() + "/" + destimageName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            aggregateWorkspaceDeployment_Type = buildAggregateWorkspaceDeployment.getAggregateWorkspaceDeployment_Type();
            int gridftpport = gridFTPURI.getPort();
            String targetSystem = gridFTPURI.getHost();
            FileTransferServiceType fileTransferServiceType = new FileTransferServiceType((new Integer(gridftpport)).intValue(), "GridFTP");
            TargetSystemType targetSystemType = new TargetSystemType(fileTransferServiceType, targetSystem);
            OSDiskTransferType type = new OSDiskTransferType(imageName, gridFTPURI.getPath() + "/" + destimageName, targetSystemType);
            DeploymentDefinition definition = new DeploymentDefinition(null, type, null, null, null, null, null);
            if ((repositoryURLs.length > 1)) {
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), null, "multiple repositories not supported");
            } else if (repositoryURLs.length == 0) {
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), null, "repository service url not specified");
            } else if (repositoryURLs.length == 1) {
                try {
                    deployResponse = deploy(definition, repositoryURLs[0]);
                } catch (Exception e) {
                    throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Unable to get the meta data of the image");
                }
            }
            try {
                workspaceReference = deployWorkspace(initFactoryPort(getWorkspaceEndpointReference(wsURL)), aggregateVirtualWorkspace_Type, aggregateWorkspaceDeployment_Type);
                wpReferenceElement[0] = new MessageElement(new QName("http://www.globus.org/2005/10/workspace/types", "WorkspaceReference"), workspaceReference);
            } catch (Exception e) {
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Unable to deploy workspace");
            }
        } else if (configuration.getDeploymentType().compareTo("xenSSH") == 0) {
            try {
                aggregateVirtualWorkspace_Type.getVwSet(0).getVirtualWorkspace().getDefinition().getModules().getDisk(0).setUri(new URI(configuration.getGridftp() + "/" + imageName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            aggregateWorkspaceDeployment_Type = buildAggregateWorkspaceDeployment.getAggregateWorkspaceDeployment_Type();
            try {
                workspaceReference = deployWorkspace(initFactoryPort(getWorkspaceEPR(configuration)), aggregateVirtualWorkspace_Type, aggregateWorkspaceDeployment_Type);
                wpReferenceElement[0] = new MessageElement(new QName("http://www.globus.org/2005/10/workspace/types", "WorkspaceReference"), workspaceReference);
            } catch (Exception e) {
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Unable to deploy workspace");
            }
        } else {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), "Unable to deploy workspace, " + "select the TS to deploy workspace");
        }
        Logistics log;
        try {
            GetResourcePropertyResponse logisticsResponse;
            logisticsResponse = getWorkspaceServicePort(workspaceReference.getEndpointReference()).getResourceProperty(WorkspaceConstants.RP_LOGISTICS);
            log = (Logistics) logisticsResponse.get_any()[0].getValueAsType(WorkspaceConstants.RP_LOGISTICS, Logistics.class);
        } catch (Exception e) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Unable to get the TSI information to connect");
        }
        staticProperties.getTSIClient().getField(0).setValue(log.getNetworking().getNics().getNic(0).getIpConfig().getIpAddress());
        try {
            Element spelement = SOAPUtil.toMessageElement("http://gpe.intel.com/idb", "StaticProperties", staticProperties).getAsDOM();
            if (deployResponse != null) {
                virtualTargetSystemKey = createVirtualTargetSystemResource(spelement, configuration, createVTSRRequest.getTerminationTime(), workspaceReference.getEndpointReference(), deployResponse.getOSImageTransferEPR());
            } else {
                virtualTargetSystemKey = createVirtualTargetSystemResource(spelement, configuration, createVTSRRequest.getTerminationTime(), workspaceReference.getEndpointReference(), null);
            }
            epr = getVirtualTargetSystemEPR(configuration, virtualTargetSystemKey);
            ISecuritySetup ss = new DelegatedSecuritySetup();
            virtualTargetSystem = getVirtualTargetSystem(virtualTargetSystemKey, configuration);
        } catch (Exception e) {
            e.printStackTrace();
            getWorkspaceServicePort(workspaceReference.getEndpointReference()).destroy(null);
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Unable to initialize TSR");
        }
        try {
            Object virtualTargetSystemProperties = collectVirtualTargetSystemProperties(virtualTargetSystem);
            if (staticProperties.getRegistry() != null) {
                for (int j = 0; j < staticProperties.getRegistry().length; j++) {
                    RegistryType registry = staticProperties.getRegistry(j);
                    try {
                        System.out.println("Connecting to Registry at: " + registry.getURL());
                        EndpointReferenceType registryEPR = getRegistryEPR(registry.getURL());
                        ISecuritySetup ss = new DelegatedSecuritySetup();
                        RegistryClient registryClient = new RegistryClientImpl(ss, registryEPR, new DummyCache(), null);
                        TargetSystemRegistrationImpl tsri = new TargetSystemRegistrationImpl(epr, virtualTargetSystemProperties);
                        tsri.addResourceType(ResourceTypes.VIRTUALTARGETSYSTEM_RESOURCETYPE.toString());
                        WSLTClient registryEntry = registryClient.register(tsri);
                        ((BaseTargetSystemResource) virtualTargetSystem).addRegistryEntry(registryEntry);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.severe("Cannot register at registry, " + e);
                    }
                }
            }
        } catch (Exception e) {
            getWorkspaceServicePort(workspaceReference.getEndpointReference()).destroy(null);
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Cannot register at registry");
        }
        response.setVtsrReference(epr);
        return response;
    }

    private EndpointReferenceType getVirtualTargetSystemEPR(BaseVTSFConfiguration serviceConfiguration, ResourceKey key) throws Exception {
        return AddressingUtils.createEndpointReference(ServiceHost.getBaseURL() + serviceConfiguration.getVirtualTargetSystemServicePath(), key);
    }

    private EndpointReferenceType getWorkspaceEPR(BaseVTSFConfiguration serviceConfiguration) throws Exception {
        SimpleResourceKey defaultFactoryKey = new SimpleResourceKey(WorkspaceConstants.RESOURCE_KEY_QNAME, WorkspaceConstants.FACTORY_DEFAULT_RSRC_KEY_NAME);
        return AddressingUtils.createEndpointReference(ServiceHost.getBaseURL() + serviceConfiguration.getWorkspaceFactoryServicePath(), defaultFactoryKey);
    }

    private EndpointReferenceType getRegistryEPR(String url) throws Exception {
        ResourceKey registryKey = new SimpleResourceKey(GPE4GTKConstants.Registry.REGISTRY_CLASS, GPE4GTKConstants.Registry.DEFAULT_REGISTRY_ID);
        return AddressingUtils.createEndpointReference(url, registryKey);
    }

    private WorkspaceFactoryPortType initFactoryPort(EndpointReferenceType epr) {
        WorkspaceFactoryServiceAddressingLocator locator = new WorkspaceFactoryServiceAddressingLocator();
        WorkspaceFactoryPortType factoryPort = null;
        try {
            factoryPort = locator.getWorkspaceFactoryPortTypePort(epr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factoryPort;
    }

    private WorkspacePortType getWorkspaceServicePort(EndpointReferenceType epr) {
        WorkspaceServiceAddressingLocator locator = new WorkspaceServiceAddressingLocator();
        WorkspacePortType servicePort = null;
        try {
            servicePort = locator.getWorkspacePortTypePort(epr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servicePort;
    }

    private EndpointReferenceType getWorkspaceEndpointReference(String url) {
        String ServiceURL = url;
        SimpleResourceKey defaultFactoryKey = new SimpleResourceKey(WorkspaceConstants.RESOURCE_KEY_QNAME, WorkspaceConstants.FACTORY_DEFAULT_RSRC_KEY_NAME);
        EndpointReferenceType epr = null;
        try {
            epr = AddressingUtils.createEndpointReference(ServiceURL, defaultFactoryKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return epr;
    }

    /**
	 * Deploy Workspace.
	 */
    private WorkspaceReference deployWorkspace(WorkspaceFactoryPortType factoryPort, AggregateVirtualWorkspace_Type vw, AggregateWorkspaceDeployment_Type req) throws Exception {
        WorkspaceInformation info = new WorkspaceInformation();
        info.setMetadata(vw);
        info.setResourceRequest(req);
        System.out.print("Deploying workspace \"" + vw.getName() + "\"...");
        WorkspaceReference ref = null;
        try {
            System.out.print("trying to connect");
            ref = factoryPort.create(info);
        } catch (WorkspaceMetadataFault e) {
            e.printStackTrace();
            throw FaultUtil.buildFault(new WorkspaceSchedulingFault(), e, "Problem with workspace metadata");
        } catch (WorkspaceSchedulingFault e) {
            e.printStackTrace();
            throw FaultUtil.buildFault(new WorkspaceMetadataFault(), e, "Scheduling error");
        } catch (WorkspaceResourceRequestDeniedFault e) {
            e.printStackTrace();
            throw FaultUtil.buildFault(new WorkspaceResourceRequestDeniedFault(), e, "Deployment request denied");
        } catch (WorkspaceFault e) {
            e.printStackTrace();
            throw FaultUtil.buildFault(new WorkspaceFault(), e, "Workspace error");
        }
        return ref;
    }

    private DeployResponse deploy(DeploymentDefinition definition, String repositoryURL) throws RemoteException {
        try {
            Deploy deploy = new Deploy(null, definition);
            return getOSRespositoryServicePort(new URL(repositoryURL)).deploy(deploy);
        } catch (Exception e) {
            throw new RemoteException("unable to deploy disk image", e);
        }
    }

    private OSRepository getOSRespositoryServicePort(URL url) {
        OSRespositoryServiceAddressingLocator locator = new OSRespositoryServiceAddressingLocator();
        OSRepository servicePort = null;
        try {
            servicePort = locator.getOSRepositoryPort(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GSSCredential cred = null;
        Subject caller = (Subject) MessageContext.getCurrentContext().getProperty(Constants.PEER_SUBJECT);
        if (caller != null) {
            cred = JaasGssUtil.getCredential(caller);
        }
        Stub stub = (Stub) servicePort;
        stub._setProperty(GSIConstants.GSI_TRANSPORT, Constants.SIGNATURE);
        stub._setProperty(Constants.AUTHORIZATION, HostAuthorization.getInstance());
        stub._setProperty(GSIConstants.GSI_CREDENTIALS, cred);
        servicePort = (OSRepository) stub;
        return servicePort;
    }

    public GetBuildsResponse getOSBuilds(GetBuildsRequest getOSBuildsRequest) throws java.rmi.RemoteException, GetBuildsFaultType {
        Build[] builds = null;
        BaseVTSFConfiguration configuration;
        try {
            ResourceContext ctx = ResourceContext.getResourceContext();
            configuration = getVTSFConfiguration(ctx);
        } catch (NamingException e) {
            throw new RemoteException("Cannot get configuration", e);
        }
        String repositoryURLs[] = configuration.getRepositoryURLs().split(" ");
        if ((repositoryURLs.length > 1)) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), null, "multiple repositories not supported");
        } else if (repositoryURLs.length == 0) {
            throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), null, "repository service url not specified");
        } else if (repositoryURLs.length == 1) {
            try {
                builds = getOSRespositoryServicePort(new URL(repositoryURLs[0])).getBuilds(new GetBuildsRequest()).getBuild();
            } catch (Exception e) {
                throw FaultUtil.buildFault(new TSRNotCreatedFaultType(), e, "Unable to get the meta data of the image");
            }
        }
        GetBuildsResponse getBuildsResponse = new GetBuildsResponse();
        getBuildsResponse.setBuild(builds);
        return getBuildsResponse;
    }
}
