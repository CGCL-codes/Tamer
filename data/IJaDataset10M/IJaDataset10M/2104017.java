package com.intel.gpe.clients.gpe4gtk.vtsf;

import java.net.URI;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.soap.SOAPException;
import org.apache.axis.message.MessageElement;
import org.unigrids.services.atomic.tsf.TSRNotCreatedFaultType;
import org.unigrids.services.atomic.vtsf.CreateVTSR;
import org.unigrids.services.atomic.vtsf.VirtualTargetSystemFactory;
import org.unigrids.services.atomic.vtsf.VirtualTargetSystemFactoryServiceAddressingLocator;
import org.w3c.dom.Element;
import com.intel.gpe.clients.api.Application;
import com.intel.gpe.clients.api.cache.ICache;
import com.intel.gpe.clients.api.cache.IdChain;
import com.intel.gpe.clients.api.exceptions.GPEMarshallingException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareRemoteException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareServiceException;
import com.intel.gpe.clients.api.exceptions.GPESecurityException;
import com.intel.gpe.clients.api.exceptions.GPETSRNotCreatedException;
import com.intel.gpe.clients.api.virtualworkspace.Build;
import com.intel.gpe.clients.api.virtualworkspace.GPEVirtualTargetSystemFactoryClient;
import com.intel.gpe.clients.api.virtualworkspace.WorkspaceClient;
import com.intel.gpe.clients.gpe.GPEClientProperties;
import com.intel.gpe.clients.gpe4gtk.GPE4GTKConstants;
import com.intel.gpe.clients.gpe4gtk.GPE4GTKFaultWrapper;
import com.intel.gpe.clients.gpe4gtk.ISecuritySetup;
import com.intel.gpe.clients.gpe4gtk.tss.ApplicationImpl;
import com.intel.gpe.clients.gpe4gtk.virtualworkspace.BuildImpl;
import com.intel.gpe.clients.gpe4gtk.virtualworkspace.ImageConfigurationImpl;
import com.intel.gpe.clients.gpe4gtk.virtualworkspace.WorkspaceClientImpl;
import com.intel.gpe.clients.gtk.CachedClientImpl;
import com.intel.gpe.clients.gtk.keys.URLKey;
import com.intel.gpe.globus.constants.TSSConstants;
import com.intel.util.soap.SOAPUtil;
import com.intel.util.xml.axis.MessageElementUtils;

/**
 * @author Sai Srinivas Dharanikota
 * @version $Id: VirtualTargetSystemFactoryClient.java,v 1.13 2006/10/24
 *          10:30:45 sdharani Exp $
 */
public class VirtualTargetSystemFactoryClient extends CachedClientImpl implements GPEVirtualTargetSystemFactoryClient {

    private URL url;

    private GPEClientProperties props;

    public VirtualTargetSystemFactoryClient(ISecuritySetup ss, URL url, ICache cache, GPEClientProperties props) {
        super(ss, new IdChain(new URLKey(url.toString())), cache);
        this.url = url;
        this.props = props;
    }

    public VirtualTargetSystemFactory getVirtualTargetSystemFactoryPort() throws GPEMiddlewareServiceException, GPESecurityException {
        VirtualTargetSystemFactory virtualTargetSystemFactory_port = getStub(GPE4GTKConstants.VirtualTargetSystemFactory.PORTTYPE);
        if (virtualTargetSystemFactory_port == null) {
            try {
                virtualTargetSystemFactory_port = new VirtualTargetSystemFactoryServiceAddressingLocator().getVirtualTargetSystemFactoryPort(url);
            } catch (ServiceException e) {
                throw new GPEMiddlewareServiceException("Service exception", e, this);
            }
            try {
                setupDelegation((Stub) virtualTargetSystemFactory_port);
            } catch (Exception e) {
                throw new GPESecurityException("Cannot setup security", e, this);
            }
            putStub(GPE4GTKConstants.VirtualTargetSystemFactory.PORTTYPE, virtualTargetSystemFactory_port);
        }
        return virtualTargetSystemFactory_port;
    }

    public <WorkspaceClientType extends WorkspaceClient> WorkspaceClientType createVTSR(Element staticProperties, Calendar initialTerminationTime, String workspaceServiceURL, String imageName, String gridFTPURI) throws GPEMarshallingException, GPEMiddlewareServiceException, GPESecurityException, GPETSRNotCreatedException, GPEMiddlewareRemoteException {
        CreateVTSR createVTSR = new CreateVTSR();
        createVTSR.setTerminationTime(initialTerminationTime);
        MessageElement wsURLElement;
        MessageElement imageNameElement;
        MessageElement gridFTPURIElement;
        try {
            wsURLElement = SOAPUtil.toMessageElement(TSSConstants.UNIGRIDS_NS, "WorkspaceServiceURL", workspaceServiceURL);
            imageNameElement = SOAPUtil.toMessageElement(TSSConstants.UNIGRIDS_NS, "ImageName", imageName);
            gridFTPURIElement = SOAPUtil.toMessageElement(TSSConstants.UNIGRIDS_NS, "GridFTPURL", gridFTPURI);
        } catch (Exception e) {
            throw new GPEMarshallingException("Could not convert to message element", e, this);
        }
        try {
            createVTSR.set_any(new MessageElement[] { MessageElementUtils.importElement(staticProperties), wsURLElement, imageNameElement, gridFTPURIElement });
        } catch (SOAPException e) {
            throw new GPEMarshallingException("Could not create bindings for configuration files", e, this);
        }
        try {
            WorkspaceClientImpl ts = new WorkspaceClientImpl(getSecuritySetup(), getVirtualTargetSystemFactoryPort().createVTSR(createVTSR).getVtsrReference(), null, getCache(), props);
            return ((WorkspaceClientType) new WorkspaceClientImpl(ts));
        } catch (TSRNotCreatedFaultType e) {
            throw new GPETSRNotCreatedException("VirtualTargetSystem not created", new GPE4GTKFaultWrapper(e), this);
        } catch (RemoteException e) {
            throw new GPEMiddlewareRemoteException("Remote exception", e, this);
        }
    }

    public Build[] getBuilds() throws GPEMiddlewareServiceException, GPESecurityException, GPEMiddlewareRemoteException {
        try {
            com.intel.gpe.services.osrepository.Build builds[] = getVirtualTargetSystemFactoryPort().getOSBuilds(null).getBuild();
            BuildImpl buildimpl[] = new BuildImpl[builds.length];
            for (int i = 0; i < builds.length; i++) {
                Application applications[] = new Application[builds[i].getImageConfiguration().getApplication().length];
                for (int j = 0; j < builds[i].getImageConfiguration().getApplication().length; j++) {
                    applications[j] = new ApplicationImpl(builds[i].getImageConfiguration().getApplication()[j]);
                }
                ImageConfigurationImpl imageConfiguration = new ImageConfigurationImpl(builds[i].getImageConfiguration().getName(), applications);
                BuildImpl build = new BuildImpl(builds[i].getBuildID(), builds[i].getBuildName(), builds[i].getComments(), imageConfiguration, builds[i].getRepositoryURL());
                buildimpl[i] = build;
            }
            return buildimpl;
        } catch (RemoteException e) {
            throw new GPEMiddlewareRemoteException("Remote exception", e, this);
        }
    }
}
