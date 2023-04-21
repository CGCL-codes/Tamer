package uk.ac.ncl.neresc.dynasoar.Interfaces.ServiceProvider;

import org.apache.axis.message.SOAPEnvelope;
import uk.ac.ncl.neresc.dynasoar.dataObjects.ActiveService;
import uk.ac.ncl.neresc.dynasoar.exceptions.CantRouteMessageException;

/**
 * Given a soap message and target service, direct the message to the best deployed version of the
 * service
 *
 * @author Charles Kubicek
 */
public interface MessageRouter {

    public String getEndpointForMessage(ActiveService service, SOAPEnvelope soap) throws CantRouteMessageException;
}
