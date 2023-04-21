package eu.mpower.framework.interoperability.sipmanagement.sipstateclient;

import eu.mpower.framework.interoperability.sipstateclient.soap.ISIPStateClient;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fuxreiter
 */
@WebService(serviceName = "SIPStateClient", portName = "iSIPStateClient", endpointInterface = "eu.mpower.framework.interoperability.sipstateclient.soap.ISIPStateClient", targetNamespace = "http://soap.SIPStateClient.interoperability.framework.mpower.eu", wsdlLocation = "WEB-INF/wsdl/SIPStateClient/SIPStateClient.wsdl")
public class SIPStateClient implements ISIPStateClient {

    private static Log logger = LogFactory.getLog(SIPStateClient.class);

    /** MPOWER web service name; has to be consistent with the MPOWER database */
    private static final String SERVICE_UID = "SIPStateClient";

    /** Timeout for requests to remote web services */
    private static final int REQUESTTIMEOUT = 5000;

    public eu.mpower.framework.interoperability.sipstateclient.soap.Status updateContactState(eu.mpower.framework.interoperability.sipstateclient.soap.UpdateContactStateMessage message) {
        eu.mpower.framework.interoperability.sipstateclient.soap.Status status = new eu.mpower.framework.interoperability.sipstateclient.soap.Status();
        status.setMessageId(1);
        status.setTimestamp(java.lang.System.currentTimeMillis());
        String contactSipId = message.getContactSipId();
        int state = message.getState();
        status.setErrorCause("Servas Nico! Die methods tut nix!");
        status.setResult(0);
        return status;
    }

    public eu.mpower.framework.interoperability.sipstateclient.soap.Status sipServerUnreachable(eu.mpower.framework.interoperability.sipstateclient.soap.SipServerUnreachableMessage message) {
        eu.mpower.framework.interoperability.sipstateclient.soap.Status status = new eu.mpower.framework.interoperability.sipstateclient.soap.Status();
        status.setMessageId(1);
        status.setTimestamp(java.lang.System.currentTimeMillis());
        String sipId = message.getSipId();
        status.setErrorCause("Servas Nico! Die methods tut nix!");
        status.setResult(0);
        return status;
    }
}
