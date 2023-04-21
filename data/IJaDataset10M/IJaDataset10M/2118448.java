package org.etexascode.transceiver.transmit;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.etexascode.j2735.IntersectionState;
import org.etexascode.j2735.SPAT;
import org.etexascode.transceiver.ServerAPI;

public class SPATTransmitterTest extends TestCase {

    Thread serverThread = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                ServerAPI.main(null);
            } catch (Exception ex) {
                Logger.getLogger(SPATTransmitterTest.class.getName()).log(Level.SEVERE, "Exception in server thread: ", ex);
            }
        }
    });

    public SPATTransmitterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serverThread.start();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        serverThread.interrupt();
    }

    public void testSendSPAT() {
        System.out.println("sendSPAT");
        SPAT data = new SPAT();
        data.setName("Test SPAT");
        data.setMsgID("Test MsgID");
        SPAT.Intersections intersections = new SPAT.Intersections();
        intersections.getIntersectionState().add(new IntersectionState());
        data.setIntersections(intersections);
        SPATTransmitter instance = new SPATTransmitter("http://localhost:8888");
        Future<Void> broadcastThread = instance.broadcast(data);
        if (broadcastThread != null) {
            try {
                broadcastThread.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(BSMTransmitterTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(BSMTransmitterTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fail("The test case is a prototype.");
    }
}
