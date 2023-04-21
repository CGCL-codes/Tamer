package org.isurf.spmiddleware.callback;

import org.apache.log4j.Logger;
import org.isurf.spmintegration.webservice.ReceiveEpcisDocumentRequest;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import epcglobal.epcis.xsd._1.EPCISDocumentType;

/**
 * Implementation of EPCISCallback which sends EPCISDocuments to the DIM over
 * JMS.
 */
public class EPCISCallbackImpl extends WebServiceGatewaySupport implements EPCISCallback {

    public static final String RECEIVE_EPCIS_DOCUMENT_REQUEST = "ReceiveEpcisDocumentRequest";

    public static final String NAMESPACE = "urn:org.isurf.spmintegration.webservice";

    private Logger logger = Logger.getLogger(EPCISCallbackImpl.class);

    /**
	 * Sends an {@link EPCISDocument} to the DIM WS via JMS.
	 *
	 * @param epcisDocument The document to be sent.
	 */
    public void epcisCallback(EPCISDocumentType epcisDocument) {
        System.out.println("epcisCallback: epcisDocument = " + epcisDocument);
        ReceiveEpcisDocumentRequest request = new ReceiveEpcisDocumentRequest();
        request.setEpcisDocument(epcisDocument);
        getWebServiceTemplate().marshalSendAndReceive(request, new WebServiceMessageCallback() {

            public void doWithMessage(WebServiceMessage message) {
                ((SoapMessage) message).setSoapAction(NAMESPACE + "/" + RECEIVE_EPCIS_DOCUMENT_REQUEST);
            }
        });
        System.out.println("epcisCallback: EPCIS document sent");
    }
}
