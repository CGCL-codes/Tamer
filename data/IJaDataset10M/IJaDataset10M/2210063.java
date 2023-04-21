package org.infoeng.x2006.x05.ictp;

import org.apache.ws.resource.ResourceContext;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlException;
import org.infoeng.ictp.ICTPServerImpl;
import org.infoeng.ictp.documents.TradeOffer;
import javax.xml.rpc.JAXRPCException;

/**
 * A ICTP service.
 *
 * This class is generated ONCE and never overwritten.
 * If there is a change to the WSDL, then the generated implemented interfaces
 * representing the implemented portTypes will change, thus showing a compile error to the
 * user.
 * <p />
 * NOTE: This file is generated, but is meant to be modified.
 *       It will NOT be overwritten by subsequent runs of Wsdl2Java.
 */
public class ICTPService extends AbstractICTPService implements ICTPCustomOperationsPortType {

    private ICTPServerImpl ictpsi;

    /**
     * Creates a new {@link ICTPService } object.
     *
     * @param resourceContext DOCUMENT_ME
     */
    public ICTPService(ResourceContext resourceContext) {
        super(resourceContext);
        init();
        ictpsi = new ICTPServerImpl();
    }

    public org.infoeng.x2006.x05.ictp.SubmitOfferResponseTypeDocument submitOffer(org.infoeng.x2006.x05.ictp.SubmitOfferRequestTypeDocument requestDoc) {
        org.infoeng.x2006.x05.ictp.SubmitOfferResponseTypeDocument responseDocument = org.infoeng.x2006.x05.ictp.SubmitOfferResponseTypeDocument.Factory.newInstance();
        TradeOffer toDoc = null;
        ictpsi.submitOffer(toDoc);
        return responseDocument;
    }

    public org.infoeng.x2006.x05.ictp.ListOffersResponseTypeDocument listOffers(org.infoeng.x2006.x05.ictp.ListOffersRequestTypeDocument requestDoc) {
        org.infoeng.x2006.x05.ictp.ListOffersResponseTypeDocument responseDocument = org.infoeng.x2006.x05.ictp.ListOffersResponseTypeDocument.Factory.newInstance();
        return responseDocument;
    }

    public org.infoeng.x2006.x05.ictp.SubmitRequestResponseTypeDocument submitRequest(org.infoeng.x2006.x05.ictp.SubmitRequestRequestTypeDocument requestDoc) {
        org.infoeng.x2006.x05.ictp.SubmitRequestResponseTypeDocument responseDocument = org.infoeng.x2006.x05.ictp.SubmitRequestResponseTypeDocument.Factory.newInstance();
        return responseDocument;
    }

    public org.infoeng.x2006.x05.ictp.ListRequestsRequestTypeDocument listRequests(org.infoeng.x2006.x05.ictp.ListRequestsRequestTypeDocument requestDoc) {
        org.infoeng.x2006.x05.ictp.ListRequestsRequestTypeDocument responseDocument = org.infoeng.x2006.x05.ictp.ListRequestsRequestTypeDocument.Factory.newInstance();
        return responseDocument;
    }
}
