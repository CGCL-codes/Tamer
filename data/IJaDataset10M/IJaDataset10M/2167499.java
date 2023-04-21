package org.wfp.rita.pojo.face;

import java.sql.Date;
import java.sql.Timestamp;
import org.wfp.rita.base.RitaException;
import org.wfp.rita.dao.ArrivalType;
import org.wfp.rita.dao.Contact;
import org.wfp.rita.dao.CustOrder;
import org.wfp.rita.dao.Location;
import org.wfp.rita.dao.Product;
import org.wfp.rita.dao.ProjectSite;
import org.wfp.rita.dao.Request;
import org.wfp.rita.dao.RequestStatus;
import org.wfp.rita.dao.ServiceType;

/**
 * A common interface for both {@link org.wfp.rita.dao.Request} and
 * {@link org.wfp.rita.dao.CustOrder}, so that we can share code that
 * works with both of them.
 * 
 * @author Chris Wilson
 */
public interface Requestable extends Identifiable, UserCreatable {

    /**
     * @return Owner's Reference
     */
    String getOwnersRef();

    /**
     * @param ownersRef Owner's Reference
     */
    void setOwnersRef(String ownersRef);

    /**
     * @return Point of arrival into system for {@link CustOrder} and 
     * {@link Request}. This is the first location through which the goods 
     * transit which is controlled by us, immediately after delivery or 
     * collection from the customer or a port location.
     */
    Location getArrivalLocation();

    /**
     * @param arrivalLocation Point of arrival into system for 
     * {@link CustOrder} and {@link Request}. This is the first location 
     * through which the goods transit which is controlled by us, immediately 
     * after delivery or collection from the customer or a port location.
     */
    void setArrivalLocation(Location arrivalLocation);

    /**
     * @return The contact (and hence organisation) which should receive
     * the invoice for the order.
     */
    Contact getInvoice();

    /**
     * @param invoice The contact (and hence organisation) which should
     * receive the invoice for the order.
     */
    void setInvoice(Contact invoice);

    /**
     * @return The name of the organisation that the order is being processed
     * on behalf of, if not specified by the invoice contact.
     */
    String getOnBehalfOf();

    /**
     * @param onBehalfOf The name of the organisation that the order is
     * being processed on behalf of, if not specified by the invoice contact.
     */
    void setOnBehalfOf(String onBehalfOf);

    /**
     * @return The product type for the whole order.
     */
    Product getProduct();

    /**
     * @param product The product type for the whole order.
     */
    void setProduct(Product product);

    /**
     * @return Identifies the way in which the cargo will enter the 
     * tracking system:
     * <pre>
     * X=Direct Delivery by Client (Location Type = ''S''); 
     * C=Pickup from Client Site (Location Type = ''C''); 
     * P=Transshipment (Location Type = ''P''); 
     * I=Internal (Location Type = ''S'' or ''W'')
     * </pre>
     */
    ArrivalType getArrivalType();

    /**
     * @param arrivalType Identifies the way in which the cargo will enter the 
     * tracking system:
     * <pre>
     * X=Direct Delivery by Client (Location Type = ''S''); 
     * C=Pickup from Client Site (Location Type = ''C''); 
     * P=Transshipment (Location Type = ''P''); 
     * I=Internal (Location Type = ''S'' or ''W'')
     * </pre>
     */
    void setArrivalType(ArrivalType arrivalType);

    /**
     * @return Additional instructions for the arrival of goods
     */
    String getArrivalInstructions();

    /**
     * @param arrivalInstructions Additional instructions for the arrival of 
     * goods
     */
    void setArrivalInstructions(String arrivalInstructions);

    /**
     * @return Additional contact details (eg. name & tel. of supplier)
     */
    String getArrivalContact();

    /**
     * @param arrivalContact Additional contact details (eg. name & tel. of 
     * supplier)
     */
    void setArrivalContact(String arrivalContact);

    /**
     * @return Additional handling instructions for the final delivery
     * (if appropriate) of the goods
     */
    String getFinalInstructions();

    /**
     * @param finalInstructions Additional handling instructions for the
     * final delivery (if appropriate) of the goods
     */
    void setFinalInstructions(String finalInstructions);

    /**
     * @return The contact who will collect the goods from the
     * Destination Site ({@link #getDestination()}).
     */
    Contact getReceiver();

    /**
     * @param receiver The contact who will collect the goods from the
     * Destination Site ({@link #getDestination()}).
     */
    void setReceiver(Contact receiver);

    /**
     * @return The contact who will deliver the goods to the ICLP.
     */
    Contact getSender();

    /**
     * @param sender The contact who will deliver the goods to the ICLP.
     */
    void setSender(Contact sender);

    /**
     * @return The waybill number or other unique identifier of the
     * goods package.
     */
    String getShipmentDocIdentifier();

    /**
     * @param shipmentDocIdentifier The waybill number or other unique
     * identifier of the goods package.
     */
    void setShipmentDocIdentifier(String shipmentDocIdentifier);

    /**
     * @return The status of the request or customer order.
     */
    RequestStatus getStatus();

    /**
     * @param status The status of the request or customer order.
     */
    void setStatus(RequestStatus status);

    /**
     * @return The type of service requested (Movement or Storage).
     */
    ServiceType getServiceType();

    /**
     * @param type The type of service requested (Movement or Storage).
     */
    void setServiceType(ServiceType type);

    /**
     * @return The follow-up date agreed with the customer.
     */
    Date getFollowupDate();

    /**
     * @param followupDate The follow-up date agreed with the customer.
     */
    void setFollowupDate(Date followupDate);

    /**
     * @return The project-site linked to this request's project and
     * owning site, as indicated by the request_site_id column, which is
     * usually the site where the goods will arrive first.
     */
    ProjectSite getRequestProjectSite();

    /**
     * @param requestProjectSite The project-site linked to this request's
     * project and owning site, as indicated by the request_site_id column,
     * which is usually the site where the goods will arrive first.
     *
     * @deprecated Warning: this would change the primary key of
     * {@link Request} objects, and will therefore not be allowed unless
     * the ID is currently null or no number has been assigned, i.e.
     * the {@link Request} has not yet been saved.
     */
    void setRequestProjectSite(ProjectSite requestProjectSite) throws RitaException;

    /**
     * @return The estimated time that the goods will be received from
     * the customer.
     */
    Timestamp getEstDispatchTime();

    /**
     * @param estDispatchTime The estimated time that the goods will be
     * received from the customer.
     */
    void setEstDispatchTime(Timestamp estDispatchTime);

    /**
     * @return The estimated time that the goods will be delivered back to
     * the customer.
     */
    Timestamp getEstDeliverTime();

    /**
     * @param estDeliverTime The estimated time that the goods will be
     * delivered back to the customer.
     */
    void setEstDeliverTime(Timestamp estDeliverTime);

    /**
     * This is a really ugly way to get the CMR number, which doesn't
     * exist for {@link CustOrder}s, so it returns null there.
     */
    String getCmrNumberIfKnown();

    /**
     * This is a really ugly way to get the sequence number, which doesn't
     * exist for {@link CustOrder}s, so it returns null there.
     */
    Integer getSeqnoIfKnown();
}
