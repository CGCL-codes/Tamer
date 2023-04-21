package com.intel.gpe.clients.gpe4gtk.virtualworkspace;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.unigrids.services.atomic.vtss.VirtualTargetSystem;
import org.unigrids.services.atomic.vtss.VirtualTargetSystemServiceAddressingLocator;
import com.intel.gpe.clients.api.cache.ICache;
import com.intel.gpe.clients.api.cache.IdChain;
import com.intel.gpe.clients.api.exceptions.GPEInvalidResourcePropertyQNameException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareRemoteException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareServiceException;
import com.intel.gpe.clients.api.exceptions.GPEResourceUnknownException;
import com.intel.gpe.clients.api.exceptions.GPESecurityException;
import com.intel.gpe.clients.api.exceptions.GPEUnmarshallingException;
import com.intel.gpe.clients.api.virtualworkspace.Logistics;
import com.intel.gpe.clients.api.virtualworkspace.ResourceAllocation;
import com.intel.gpe.clients.api.virtualworkspace.State;
import com.intel.gpe.clients.api.virtualworkspace.WorkspaceClient;
import com.intel.gpe.clients.gpe.GPEClientProperties;
import com.intel.gpe.clients.gpe4gtk.GPE4GTKConstants;
import com.intel.gpe.clients.gpe4gtk.ISecuritySetup;
import com.intel.gpe.clients.gpe4gtk.tss.ManagedTargetSystemClientImpl;
import com.intel.gpe.clients.gtk.WSRFClientImpl;

/**
 * @author Sai Srinivas Dharanikota
 * @version $Id: WorkspaceClientImpl.java,v 1.13 2006/10/24 10:30:45 sdharani
 *          Exp $
 */
public class WorkspaceClientImpl extends ManagedTargetSystemClientImpl implements WorkspaceClient {

    public WorkspaceClientImpl(WSRFClientImpl client) {
        super(client);
    }

    public WorkspaceClientImpl(ISecuritySetup ss, EndpointReferenceType epr, IdChain parent, ICache cache, GPEClientProperties props) {
        super(ss, epr, parent, cache, props);
    }

    public VirtualTargetSystem getVirtualTargetSystemPort() throws GPEMiddlewareServiceException, GPESecurityException {
        VirtualTargetSystem targetSystem_port = (VirtualTargetSystem) getStub(GPE4GTKConstants.VirtualTargetSystem.PORTTYPE);
        if (targetSystem_port == null) {
            VirtualTargetSystemServiceAddressingLocator locator = new VirtualTargetSystemServiceAddressingLocator();
            try {
                targetSystem_port = locator.getVirtualTargetSystemPort(getEndpointReference());
            } catch (ServiceException e) {
                throw new GPEMiddlewareServiceException("Service exception", e, this);
            }
            try {
                getSecuritySetup().setupDelegation((Stub) targetSystem_port);
            } catch (Exception e) {
                throw new GPESecurityException("Cannot setup security", e, this);
            }
            putStub(GPE4GTKConstants.VirtualTargetSystem.PORTTYPE, (Stub) targetSystem_port);
        }
        return targetSystem_port;
    }

    public void shutdown() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, Exception {
        try {
            getVirtualTargetSystemPort().shutdown(null);
        } catch (Exception e) {
            throw new Exception("Cannot shutdown virtual workpace", e);
        }
    }

    public void start() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, Exception {
        try {
            getVirtualTargetSystemPort().start(null);
        } catch (Exception e) {
            throw new Exception("Cannot start virtual workpace", e);
        }
    }

    public void pause() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, Exception {
        try {
            getVirtualTargetSystemPort().pause(null);
        } catch (Exception e) {
            throw new Exception("Cannot pause virtual workpace", e);
        }
    }

    public State getState() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, Exception {
        return new StateImpl(getVirtualTargetSystemPort().getState(null).getState());
    }

    public Logistics getLogistics() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, Exception {
        return new LogisticsImpl(getVirtualTargetSystemPort().getLogistics(null).getLogistics().getIPAddress());
    }

    public ResourceAllocation getResourceAllocation() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, Exception {
        return new ResourceAllocationImpl(getVirtualTargetSystemPort().getResourceAllocation(null).getResourceAllocation().getPhysicalMemory(), getVirtualTargetSystemPort().getResourceAllocation(null).getResourceAllocation().getCPUType());
    }
}
