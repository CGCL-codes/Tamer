package jade.imtp.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.NodeDescriptor;
import jade.core.Service;
import jade.core.ServiceDescriptor;
import jade.core.ServiceException;
import jade.core.PlatformManager;
import jade.security.JADESecurityException;
import java.util.Vector;

/**
   @author Giovanni Rimassa - FRAMeTech s. r. l.
 */
class ServiceManagerRMIImpl extends UnicastRemoteObject implements ServiceManagerRMI {

    private PlatformManager impl;

    /** Creates new ServiceManagerRMIImpl */
    public ServiceManagerRMIImpl(PlatformManager pm, RMIIMTPManager mgr, int port) throws RemoteException {
        super(port, mgr.getClientSocketFactory(), mgr.getServerSocketFactory());
        impl = pm;
    }

    PlatformManager getPlatformManager() {
        return impl;
    }

    public String getPlatformName() throws RemoteException {
        try {
            return impl.getPlatformName();
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public String addNode(NodeDescriptor dsc, Vector nodeServices, boolean propagated) throws RemoteException, ServiceException, JADESecurityException {
        try {
            return impl.addNode(dsc, nodeServices, propagated);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public void removeNode(NodeDescriptor dsc, boolean propagated) throws RemoteException, ServiceException {
        try {
            impl.removeNode(dsc, propagated);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public void addSlice(ServiceDescriptor service, NodeDescriptor dsc, boolean propagated) throws RemoteException, ServiceException {
        try {
            impl.addSlice(service, dsc, propagated);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public void removeSlice(String serviceKey, String sliceKey, boolean propagated) throws RemoteException, ServiceException {
        try {
            impl.removeSlice(serviceKey, sliceKey, propagated);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public void addReplica(String newAddr, boolean propagated) throws RemoteException, ServiceException {
        try {
            impl.addReplica(newAddr, propagated);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call. " + imtpe);
        }
    }

    public void removeReplica(String address, boolean propagated) throws RemoteException, ServiceException {
        try {
            impl.removeReplica(address, propagated);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public Service.Slice findSlice(String serviceKey, String sliceKey) throws RemoteException, ServiceException {
        try {
            return impl.findSlice(serviceKey, sliceKey);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public Vector findAllSlices(String serviceKey) throws RemoteException, ServiceException {
        try {
            return impl.findAllSlices(serviceKey);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public void adopt(Node n, Node[] children) throws RemoteException {
        try {
            impl.adopt(n, children);
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }

    public void ping() throws RemoteException {
        try {
            impl.ping();
        } catch (IMTPException imtpe) {
            imtpe.printStackTrace();
            throw new RemoteException("IMTPException in local call");
        }
    }
}
