package uk.ac.ncl.neresc.dynasoar.client;

import org.apache.axis.types.URI;
import uk.ac.ncl.neresc.dynasoar.client.codestore.codestore.CodeStorePortType;
import uk.ac.ncl.neresc.dynasoar.client.codestore.codestore.CodeStoreService;
import uk.ac.ncl.neresc.dynasoar.client.codestore.codestore.CodeStoreServiceLocator;
import uk.ac.ncl.neresc.dynasoar.client.codestore.faults.DynasoarExceptionType;
import uk.ac.ncl.neresc.dynasoar.client.codestore.messages.AddServiceMsgType;
import uk.ac.ncl.neresc.dynasoar.client.codestore.messages.QualifiedServiceNameType;
import uk.ac.ncl.neresc.dynasoar.dataObjects.Constants;
import uk.ac.ncl.neresc.dynasoar.dataObjects.ServiceObject;
import uk.ac.ncl.neresc.dynasoar.exceptions.ClientInitialisationException;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Collection;

/**
 * @author Charles Kubicek
 */
public class CodeStoreClient {

    private CodeStorePortType csc = null;

    private String endpoint = null;

    public CodeStoreClient(String endpoint) throws ClientInitialisationException {
        try {
            this.endpoint = endpoint;
            CodeStoreService css = new CodeStoreServiceLocator();
            csc = css.getCodeStoreService(new URL(endpoint));
        } catch (ServiceException e) {
            throw new ClientInitialisationException(e.getMessage());
        } catch (MalformedURLException e) {
            throw new ClientInitialisationException(e.getMessage());
        }
    }

    public String getServiceIdForName(String name) throws RemoteException {
        try {
            return csc.getServiceIDForName(new QualifiedServiceNameType(null, name, null, null, null));
        } catch (DynasoarExceptionType dynasoarExceptionType) {
            throw new RemoteException(dynasoarExceptionType.getDescription());
        }
    }

    public String getServiceCodeLocation(String id) throws RemoteException {
        try {
            return csc.getServiceCode(id).getCodeStoreEndpoint().toString();
        } catch (DynasoarExceptionType dynasoarExceptionType) {
            System.out.println("dynasoarExceptionType.getDescription() = " + dynasoarExceptionType.getDescription());
            dynasoarExceptionType.printStackTrace();
            throw new RemoteException(dynasoarExceptionType.getDescription());
        }
    }

    public String addSerivce(String sid, URI codeAvailabilityURI) throws RemoteException {
        AddServiceMsgType addServiceMsgType = new AddServiceMsgType();
        addServiceMsgType.setServiceID(sid);
        addServiceMsgType.setServiceURL(codeAvailabilityURI);
        return csc.addData(addServiceMsgType);
    }

    public uk.ac.ncl.neresc.dynasoar.dataObjects.ServiceObject getServiceInfoFromCodeStore(String serviceName) throws RemoteException {
        String codeLocation = null;
        return new ServiceObject(serviceName, endpoint, serviceName, null, null, Constants.NOT_DEPLOYED, Constants.TOMCAT_AXIS_WAR);
    }
}
