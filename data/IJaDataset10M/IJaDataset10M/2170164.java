package com.wm.wmsopera.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.sopware.library.Constants;
import org.sopware.library.CreateLendingHandler;
import org.sopware.papi.ParticipantIdentity;
import org.sopware.papi.SBB;
import org.sopware.papi.SBBFactory;
import org.sopware.papi.untyped.IncomingMessageHandler;
import org.sopware.papi.untyped.provider.OnewayOperationSkeleton;
import org.sopware.papi.untyped.provider.RequestResponseOperationSkeleton;
import org.sopware.papi.untyped.provider.ServiceSkeleton;
import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;
import com.wm.wmsopera.util.SimpleIDataUtil;
import junit.framework.TestCase;

public class EnrichShipmentLoadTest extends TestCase {

    private ServiceSkeleton serviceSkeleton;

    private SBB mySBB;

    private Context context;

    protected void setUp() throws Exception {
        Logger.getLogger("").setLevel(Level.OFF);
        System.setProperty("org.sopware.transport.http.port", Integer.toString(38888));
        ParticipantIdentity myParticipantID = new ParticipantIdentity() {

            public String getApplicationID() {
                return ValidateShipmentTest.class.getCanonicalName();
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

    public void testMultiThreadedThreadedPerformanceOfCreateLendingLoop() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        RequestResponseOperationSkeleton operation = null;
        try {
            operation = serviceSkeleton.getRequestResponseOperation("enrichShipment");
            IncomingMessageHandler handler = new EnrichShipmentHandler(operation);
            operation.registerMessageHandler(handler);
            int MAX = 5;
            long before = System.currentTimeMillis();
            int i = 0;
            for (; i < MAX; i++) {
                pool.execute(new Runnable() {

                    public void run() {
                        try {
                            context.invoke("TestWmSOPERA.sopera.services.shipment", "enrichFlowRepeat", null);
                        } catch (ServiceException e) {
                        }
                    }
                });
            }
            pool.shutdown();
            try {
                pool.awaitTermination(10000, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            long after = System.currentTimeMillis();
            System.err.println("Execution time in ms: " + (after - before));
            System.err.println("Execution time in ms: " + ((after - before) / (MAX * 1000)));
        } finally {
            if (operation != null) assertTrue(operation.releaseMessageHandler());
        }
    }
}
