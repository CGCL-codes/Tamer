package uk.ac.ncl.neresc.dynasoar.testServices.echoService;

import org.w3c.dom.Document;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPBody;
import uk.ac.ncl.neresc.dynasoar.exceptions.ConfigurationException;
import javax.xml.soap.SOAPException;

/**
 * @author Charles Kubicek
 */
public class EchoService {

    public void processMessage(SOAPEnvelope req, SOAPEnvelope resp) {
        try {
            resp.setBody(((SOAPBody) req.getBody().cloneNode(true)));
        } catch (SOAPException e) {
            req = new SOAPEnvelope();
        }
    }
}
