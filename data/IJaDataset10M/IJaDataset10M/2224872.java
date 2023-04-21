package org.isurf.spmbl.workitems;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import org.fosstrak.epcis.model.ActionType;
import org.fosstrak.epcis.model.BusinessLocationType;
import org.fosstrak.epcis.model.EPC;
import org.fosstrak.epcis.model.EPCISBodyType;
import org.fosstrak.epcis.model.EPCISDocumentType;
import org.fosstrak.epcis.model.EPCListType;
import org.fosstrak.epcis.model.ObjectEventType;
import org.fosstrak.epcis.model.ReadPointType;
import org.isurf.spmiddleware.model.epcis.EPCISDocument;
import org.isurf.spmiddleware.utils.XMLUtils;
import org.junit.Test;
import com.isurf.epcclient.api.EPCClientException;
import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;

/**
 * Unit tests the {@link DSPublisher}.
 */
public class DSPublisherIT {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DSPublisherIT.class);

    /**
	 * Test method for {@link org.isurf.spmbl.workitems.DSPublisher#publish(java.lang.String, org.fosstrak.epcis.model.EPCISDocumentType)}.
	 * @throws EPCClientException 
	 * @throws MalformedURIException 
	 */
    @Test
    public void testPublish() throws EPCClientException {
        DSPublisher publisher = new DSPublisher(null);
        EPCISDocumentType epcisDocument = createEpcisDocument();
        try {
            URI uriAddress = new URI("http://localhost:8090/epcis-repository-0.4.2");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 1; i++) {
            publisher.publish(epcisDocument, "http://88.34.148.230:8090/IS-DS_interface", "Piacenza Warehouse", "http://localhost:8090/epcis-repository-0.4.2");
        }
    }

    /**
	 * Creates an {@link EPCISDocument}.
	 *
	 * @param ecReports The ALE.
	 * @param action The action.
	 * @param bizSteps The business steps.
	 * @param disposition The disposition.
	 * @param readPointId The read point ID.
	 * @param bizLocationId The location.
	 * @return The document.
	 */
    public EPCISDocumentType createEpcisDocument() {
        Set<EPC> epcs = new HashSet<EPC>();
        EPC epc1 = new EPC();
        epc1.setValue("urn:epc:id:sgtin:403411526.123430.3001");
        EPC epc2 = new EPC();
        epc2.setValue("urn:epc:id:sgtin:403411526.123430.3002");
        EPC epc3 = new EPC();
        epc3.setValue("urn:epc:id:sgtin:403411526.123430.3003");
        EPC epc4 = new EPC();
        epc4.setValue("urn:epc:id:sgtin:403411526.123430.3004");
        epcs.add(epc1);
        epcs.add(epc2);
        epcs.add(epc3);
        epcs.add(epc4);
        logger.debug("createEpcisDocument: epcs = " + epcs.size());
        ObjectEventType objectEvent = createObjectEvent(epcs, ActionType.OBSERVE, "urn:epcglobal:cbv:bizstep:accepting", "urn:epcglobal:cbv:disp:active", "urn:epc:id:sgln:803411525.warehouse", "urn:epc:id:sgln:803411525.warehouse");
        EPCISDocumentType epcisDoc = new EPCISDocumentType();
        EPCISBodyType epcisBody = new EPCISBodyType();
        org.fosstrak.epcis.model.EventListType eventList = new org.fosstrak.epcis.model.EventListType();
        eventList.getObjectEventOrAggregationEventOrQuantityEvent().add(objectEvent);
        epcisBody.setEventList(eventList);
        epcisDoc.setEPCISBody(epcisBody);
        epcisDoc.setSchemaVersion(new BigDecimal("1.0"));
        epcisDoc.setCreationDate(XMLUtils.getCurrent());
        return epcisDoc;
    }

    /**
	 * Creates an {@link ObjectEventType}.
	 *
	 * @param ecReports The ALE
	 * @param action The action
	 * @param bizSteps The business steps
	 * @param disposition The disposition
	 * @param readPointId The read point
	 * @param bizLocationId The business location
	 */
    private ObjectEventType createObjectEvent(Set<EPC> epcs, ActionType action, String bizSteps, String disposition, String readPointId, String bizLocationId) {
        logger.debug("createObjectEvent: epcs = " + epcs.size());
        EPCListType epcList = new EPCListType();
        for (EPC epc : epcs) {
            epcList.getEpc().add(epc);
        }
        ObjectEventType objEvent = new ObjectEventType();
        objEvent.setEpcList(epcList);
        objEvent.setEventTime(XMLUtils.getCurrent());
        objEvent.setEventTimeZoneOffset(XMLUtils.getTimeOffset(objEvent.getEventTime()));
        objEvent.setAction(action);
        objEvent.setBizStep(bizSteps);
        objEvent.setDisposition(disposition);
        ReadPointType readPoint = new ReadPointType();
        readPoint.setId(readPointId);
        objEvent.setReadPoint(readPoint);
        BusinessLocationType bizLocation = new BusinessLocationType();
        bizLocation.setId(bizLocationId);
        objEvent.setBizLocation(bizLocation);
        return objEvent;
    }
}
