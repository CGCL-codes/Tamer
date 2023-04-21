package org.ist_spice.mdcs.rds;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

/**
 * This class queries a remote Oscar knowledge source for
 * device information.
 * @author Wout.Slakhorst
 *
 */
public class RemoteStore implements CapabilityStore {

    private static Logger logger = Logger.getLogger(RemoteStore.class);

    /**
	 * 
	 * @param sparql
	 */
    public String queryRDF(String sparql) {
        logger.info("CapabilityStore.queryRDF");
        String endpoint = "http://localhost:8081/ksoap2/SpiceCaps";
        Service service = new org.apache.axis.client.Service();
        try {
            Call call = (Call) service.createCall();
            URL url = new URL(endpoint);
            call.setTargetEndpointAddress(url);
            QName opName = new QName("http://spice.telin.nl/", "query");
            call.setOperationName(opName);
            return (String) call.invoke(new Object[] { sparql });
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
