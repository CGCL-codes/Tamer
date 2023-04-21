package org.isurf.spmiddleware.client;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fosstrak.ale.wsdl.ale.epcglobal.EmptyParms;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

/**
 * Web service client for the getVendorVersion method of an ALE server.
 */
public class GetVendorVersionClient extends WebServiceGatewaySupport {

    private static final Log logger = LogFactory.getLog(GetVendorVersionClient.class);

    /**
	 * Constructs a GetVendorVersionClient.
	 * 
	 * @param messageFactory
	 *            The {@link WebServiceMessageFactory}.
	 */
    public GetVendorVersionClient(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }

    /**
	 * @return The vendor version.
	 */
    public String getVendorVersion() {
        JAXBElement<EmptyParms> getVendorVersionRequest = new JAXBElement<EmptyParms>(new QName(ALENamespaces.ALE_NAMESPACE, ALENamespaces.GET_VENDOR_VERSION), EmptyParms.class, new EmptyParms());
        JAXBElement<String> vendorVersionResult = (JAXBElement<String>) getWebServiceTemplate().marshalSendAndReceive(getVendorVersionRequest, new WebServiceMessageCallback() {

            public void doWithMessage(WebServiceMessage message) {
                ((SoapMessage) message).setSoapAction(ALENamespaces.ALE_NAMESPACE + "/" + ALENamespaces.GET_VENDOR_VERSION);
            }
        });
        logger.debug("getVendorVersion: vendorVersionResult = " + vendorVersionResult);
        logger.debug("getVendorVersion: vendorVersionResult = " + vendorVersionResult.getClass());
        logger.debug("getVendorVersion: vendorVersionResult = " + vendorVersionResult.getDeclaredType());
        logger.debug("getVendorVersion: value = " + vendorVersionResult.getValue());
        String vendorVersion = vendorVersionResult.getValue();
        logger.debug("getVendorVersion: version = " + vendorVersion);
        return vendorVersion;
    }
}
