package org.etexascode.transceiver.transmit;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.etexascode.j2735.Intersection;
import org.etexascode.j2735.MapData;
import org.etexascode.transceiver.ServerAPI;

public class MapDataTransmitterTest extends TestCase {

    Thread serverThread = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                ServerAPI.main(null);
            } catch (Exception ex) {
                Logger.getLogger(MapDataTransmitterTest.class.getName()).log(Level.SEVERE, "Exception in server thread: ", ex);
            }
        }
    });

    public MapDataTransmitterTest(String testName) {
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

    public void testSendMapData() {
        System.out.println("sendMapData");
        MapData data = new MapData();
        data.setName("Test SPAT");
        data.setMsgID("Test MsgID");
        MapData.Intersections intersections = new MapData.Intersections();
        intersections.getIntersection().add(new Intersection());
        data.setIntersections(intersections);
        MapDataTransmitter instance = new MapDataTransmitter("http://localhost:8888");
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
