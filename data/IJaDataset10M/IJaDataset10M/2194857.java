package phex.msg;

import java.net.Socket;
import junit.framework.TestCase;
import phex.common.address.DefaultDestAddress;
import phex.common.bandwidth.BandwidthController;
import phex.host.Host;
import phex.net.connection.Connection;
import phex.net.repres.def.DefaultSocketFacade;
import phex.utils.QueryGUIDRoutingPair;

public class MsgManagerTest extends TestCase {

    private MsgManager msgmanager;

    public MsgManagerTest(String s) {
        super(s);
    }

    protected void setUp() {
        msgmanager = MsgManager.getInstance();
    }

    protected void tearDown() {
    }

    public void testAddToPushRoutingTable() throws Exception {
        GUID pushClientGUID = new GUID();
        Host pushHost = new Host(new DefaultDestAddress("1.1.1.1", 1111));
        pushHost.setConnection(new Connection(new DummySocket(), BandwidthController.acquireBandwidthController("JUnitText", Long.MAX_VALUE)));
        msgmanager.addToPushRoutingTable(pushClientGUID, pushHost);
        Host host = msgmanager.getPushRouting(pushClientGUID);
        assertEquals(pushHost, host);
    }

    public void testCheckAndAddToPingRoutingTable() throws Exception {
        GUID pingGUID = new GUID();
        Host pingHost = new Host(new DefaultDestAddress("2.2.2.2", 2222));
        pingHost.setConnection(new Connection(new DummySocket(), BandwidthController.acquireBandwidthController("JUnitText", Long.MAX_VALUE)));
        boolean pingCheckValue = msgmanager.checkAndAddToPingRoutingTable(pingGUID, pingHost);
        assertEquals(true, pingCheckValue);
        pingCheckValue = msgmanager.checkAndAddToPingRoutingTable(pingGUID, pingHost);
        assertEquals(false, pingCheckValue);
        Host host = msgmanager.getPingRouting(pingGUID);
        assertEquals(pingHost, host);
    }

    public void testCheckAndAddToQueryRoutingTable() throws Exception {
        GUID queryGUID = new GUID();
        Host queryHost = new Host(new DefaultDestAddress("3.3.3.3", 3333));
        queryHost.setConnection(new Connection(new DummySocket(), BandwidthController.acquireBandwidthController("JUnitText", Long.MAX_VALUE)));
        boolean queryCheckValue = msgmanager.checkAndAddToQueryRoutingTable(queryGUID, queryHost);
        assertEquals(true, queryCheckValue);
        queryCheckValue = msgmanager.checkAndAddToQueryRoutingTable(queryGUID, queryHost);
        assertEquals(false, queryCheckValue);
        QueryGUIDRoutingPair pair = msgmanager.getQueryRouting(queryGUID, 0);
        assertEquals(queryHost, pair.getHost());
    }

    private class DummySocket extends DefaultSocketFacade {

        DummySocket() {
            super(new Socket());
        }
    }
}
