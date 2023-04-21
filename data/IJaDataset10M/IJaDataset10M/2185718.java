package org.isurf.spmbl.workitems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.HashSet;
import java.util.Set;
import org.fosstrak.ale.xsd.epcglobal.EPC;
import org.fosstrak.epcis.model.EPCISDocumentType;
import org.isurf.spmiddleware.UnitUtils;
import org.isurf.spmiddleware.utils.XMLUtils;
import org.junit.Test;

/**
 * Unit tests {@link CreateQuantityEvent}.
 */
public class CreateQuantityEventTest {

    /**
	 * Test method for {@link org.isurf.spmbl.workitems.CreateQuantityEvent#createQuantityEvents(org.fosstrak.epcis.model.EPCISDocumentType, java.util.Set)}.
	 */
    @Test
    public void testCreateQuantityEventsUsingNullEPCs() {
        CreateQuantityEvent createQuantityEvent = new CreateQuantityEvent(null);
        EPCISDocumentType epcisDocument = UnitUtils.createEPCISDocument();
        final int eventListSize = epcisDocument.getEPCISBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent().size();
        createQuantityEvent.createQuantityEvents(epcisDocument, null, null);
        assertEquals(eventListSize, epcisDocument.getEPCISBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent().size());
    }

    /**
	 * Test method for {@link org.isurf.spmbl.workitems.CreateQuantityEvent#createQuantityEvents(org.fosstrak.epcis.model.EPCISDocumentType, java.util.Set)}.
	 */
    @Test
    public void testCreateQuantityEventsUsingEmptyEPCs() {
        Set<EPC> epcs = new HashSet<EPC>();
        CreateQuantityEvent createQuantityEvent = new CreateQuantityEvent(null);
        EPCISDocumentType epcisDocument = UnitUtils.createEPCISDocument();
        final int eventListSize = epcisDocument.getEPCISBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent().size();
        createQuantityEvent.createQuantityEvents(epcisDocument, epcs, null);
        assertEquals(eventListSize, epcisDocument.getEPCISBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent().size());
    }

    /**
	 * Test method for {@link org.isurf.spmbl.workitems.CreateQuantityEvent#createQuantityEvents(org.fosstrak.epcis.model.EPCISDocumentType, java.util.Set)}.
	 */
    @Test
    public void testCreateQuantityEventsUsingValidEPCs() {
        Set<EPC> epcs = new HashSet<EPC>();
        EPC epc1 = new EPC();
        epc1.setValue("urn:epc:id:sgtin:0069000.957110.38638475");
        EPC epc2 = new EPC();
        epc2.setValue("urn:epc:id:sgtin:0069000.957110.38638476");
        EPC epc3 = new EPC();
        epc3.setValue("urn:epc:id:sgtin:0069000.957110.38638477");
        EPC epc4 = new EPC();
        epc4.setValue("urn:epc:id:sgtin:0069001.957110.18638477");
        EPC epc5 = new EPC();
        epc5.setValue("urn:epc:id:sgtin:0069001.957110.18638477");
        epcs.add(epc1);
        epcs.add(epc2);
        epcs.add(epc3);
        epcs.add(epc4);
        epcs.add(epc5);
        CreateQuantityEvent createQuantityEvent = new CreateQuantityEvent(null);
        EPCISDocumentType epcisDocument = UnitUtils.createEPCISDocument();
        final int eventListSize = epcisDocument.getEPCISBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent().size();
        createQuantityEvent.createQuantityEvents(epcisDocument, epcs, null);
        assertFalse(eventListSize == epcisDocument.getEPCISBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent().size());
        assertEquals(eventListSize + 2, epcisDocument.getEPCISBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent().size());
        System.out.println(XMLUtils.toXML(epcisDocument, "urn:epcglobal:epcis:xsd:1"));
    }
}
