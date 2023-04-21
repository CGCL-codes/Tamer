package uk.ac.ncl.neresc.dynasoar.client;

import uk.ac.ncl.neresc.dynasoar.client.serviceProvider.ServiceProviderImpl;
import uk.ac.ncl.neresc.dynasoar.client.serviceProvider.ServiceProviderImplService;
import uk.ac.ncl.neresc.dynasoar.client.serviceProvider.ServiceProviderImplServiceLocator;
import uk.ac.ncl.neresc.dynasoar.exceptions.*;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * @author Charles Kubicek
 */
public class ServiceProviderClient {

    private ServiceProviderImpl sp_client = null;

    public ServiceProviderClient(String endpoint) throws ClientInitialisationException {
        try {
            ServiceProviderImplService sp = new ServiceProviderImplServiceLocator();
            sp_client = sp.getServiceProvider(new URL(endpoint));
        } catch (ServiceException e) {
            throw new ClientInitialisationException(e.getMessage());
        } catch (MalformedURLException e) {
            throw new ClientInitialisationException(e.getMessage());
        }
    }

    public void registerHostProvider(uk.ac.ncl.neresc.dynasoar.dataObjects.HostProviderObject hp) throws RemoteException, HPAlreadyRegisteredException, ConfigurationException {
        uk.ac.ncl.neresc.dynasoar.client.dataObjects.HostProviderObject ho = new uk.ac.ncl.neresc.dynasoar.client.dataObjects.HostProviderObject(hp.getAccessPolicy(), hp.getEndpoint(), hp.getResourceDescription());
        sp_client.registerHostProvider(ho);
    }

    public void serviceDeployedNotification(String serviceId, String endpoint) throws RemoteException, ServiceAlreadyDeployedException, UnkownServiceIdException, ConfigurationException {
        sp_client.serviceDeployedNotification(serviceId, endpoint);
    }
}
