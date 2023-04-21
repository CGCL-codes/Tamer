package org.isurf.spmiddleware.client.lrm;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fosstrak.ale.wsdl.alelr.epcglobal.EmptyParms;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

/**
 * Web service client for the getStandardVersion method of a logical reader manager.
 */
public class GetStandardVersionClient extends WebServiceGatewaySupport {

    private static final Log logger = LogFactory.getLog(GetStandardVersionClient.class);

    /**
	 * Construct a {@link GetStandardVersionClient}.
	 * 
	 * @param messageFactory The spring message factory
	 */
    public GetStandardVersionClient(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }

    /**
	 * @return The lrm standard version.
	 */
    public String getStandardVersion() {
        JAXBElement<EmptyParms> getVendorVersionRequest = new JAXBElement<EmptyParms>(new QName(LRMNamespaces.NAMESPACE, LRMNamespaces.GET_STANDARD_VERSION), EmptyParms.class, new EmptyParms());
        JAXBElement<String> standardVersionResult = (JAXBElement<String>) getWebServiceTemplate().marshalSendAndReceive(getVendorVersionRequest, new WebServiceMessageCallback() {

            public void doWithMessage(WebServiceMessage message) {
                ((SoapMessage) message).setSoapAction(LRMNamespaces.NAMESPACE + "/" + LRMNamespaces.GET_STANDARD_VERSION);
            }
        });
        String standardVersion = standardVersionResult.getValue();
        logger.debug("getStandardVersion: version = " + standardVersion);
        return standardVersion;
    }

    /**
	 * Invokes the getStandardVersion method of the configured LRM.
	 * 
	 * @param args none
	 */
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("org/isurf/spmiddleware/client/ClientApplicationContext.xml");
        GetStandardVersionClient getStandardVersion = (GetStandardVersionClient) applicationContext.getBean("getLRMStandardVersion", GetStandardVersionClient.class);
        getStandardVersion.getStandardVersion();
    }
}
