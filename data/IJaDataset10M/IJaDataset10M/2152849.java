package com.wm.wmsopera.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import junit.framework.TestCase;
import org.sopware.papi.ParticipantIdentity;
import org.sopware.papi.SBB;
import org.sopware.papi.SBBFactory;
import org.sopware.papi.untyped.provider.RequestResponseOperationSkeleton;
import org.sopware.papi.untyped.provider.ServiceSkeleton;
import com.wm.app.b2b.client.Context;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.util.Values;
import com.wm.wmsopera.util.SimpleIDataUtil;

public class EnrichShipmentTest extends TestCase {

    private ServiceSkeleton serviceSkeleton;

    private SBB mySBB;

    private Context context;

    protected void setUp() throws Exception {
        Logger.getLogger("").setLevel(Level.OFF);
        System.setProperty("org.sopware.transport.http.port", Integer.toString(38888));
        ParticipantIdentity myParticipantID = new ParticipantIdentity() {

            public String getApplicationID() {
                return EnrichShipmentTest.class.getCanonicalName();
            }

            public String getInstanceID() {
                return "single";
            }
        };
        mySBB = SBBFactory.getSBB(myParticipantID);
        serviceSkeleton = mySBB.lookupServiceSkeleton(QName.valueOf("{http://services.dgf.com/prototype/Shipment/1.0}Shipment"), QName.valueOf("{http://services.dgf.com/prototype/Shipment/1.0/ShipmentProvider}ShipmentProvider"));
        String server = "mchame.eur.ad.sag:5555";
        context = new Context();
        context.connect(server, "Administrator", "manage");
    }

    public void testEnrich() throws Exception {
        RequestResponseOperationSkeleton operation = null;
        try {
            operation = serviceSkeleton.getRequestResponseOperation("enrichShipment");
            EnrichShipmentHandler handler = new EnrichShipmentHandler(operation);
            operation.registerMessageHandler(handler);
            Values r = context.invoke("TestWmSOPERA.sopera.services.shipment", "enrichFlow", null);
            assertTrue(r.containsKey("responseMessage"));
            IData response = (IData) r.get("responseMessage");
            IDataCursor respCursor = response.getCursor();
            assertTrue(respCursor.first("Shipment"));
            IData shipment = (IData) respCursor.getValue();
            IDataCursor sCursor = shipment.getCursor();
            assertTrue(sCursor.first("ShipmentHeader"));
            assertTrue(handler.wasSuccessful());
        } finally {
            if (operation != null) assertTrue(operation.releaseMessageHandler());
        }
    }

    public void testEnrichRepeat() throws Exception {
        RequestResponseOperationSkeleton operation = null;
        try {
            operation = serviceSkeleton.getRequestResponseOperation("enrichShipment");
            EnrichShipmentHandler handler = new EnrichShipmentHandler(operation);
            operation.registerMessageHandler(handler);
            context.invoke("TestWmSOPERA.sopera.services.shipment", "enrichFlowRepeat", null);
            assertTrue(handler.wasSuccessful());
        } finally {
            if (operation != null) assertTrue(operation.releaseMessageHandler());
        }
    }

    protected void tearDown() throws Exception {
        mySBB.release();
        context.disconnect();
    }
}
