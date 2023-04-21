package org.mobicents.protocols.ss7.m3ua.impl;

import javolution.util.FastList;
import org.apache.log4j.Logger;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;

/**
 * 
 * @author amit bhayani
 * 
 */
public class GatewayTest {

    private static final Logger logger = Logger.getLogger(GatewayTest.class);

    private static final String SERVER_NAME = "testserver";

    private static final String SERVER_HOST = "127.0.0.1";

    private static final int SERVER_PORT = 2345;

    private static final String SERVER_ASSOCIATION_NAME = "serverAsscoiation";

    private static final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";

    private static final String CLIENT_HOST = "127.0.0.1";

    private static final int CLIENT_PORT = 2346;

    private Management sctpManagement = null;

    private M3UAManagement m3uaMgmt = null;

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    private As remAs;

    private Asp remAsp;

    private AspFactory remAspFactory;

    private As localAs;

    private Asp localAsp;

    private AspFactory localAspFactory;

    private Server server;

    private Client client;

    private Mtp3UserPartListenerImpl mtp3UserPartListener = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        mtp3UserPartListener = new Mtp3UserPartListenerImpl();
        client = new Client();
        server = new Server();
        this.sctpManagement = new ManagementImpl("GatewayTest");
        this.sctpManagement.setSingleThread(true);
        this.sctpManagement.setConnectDelay(1000 * 5);
        this.sctpManagement.start();
        this.m3uaMgmt = new M3UAManagement("GatewayTest");
        this.m3uaMgmt.setTransportManagement(this.sctpManagement);
        this.m3uaMgmt.addMtp3UserPartListener(mtp3UserPartListener);
        this.m3uaMgmt.start();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        this.sctpManagement.stop();
        this.m3uaMgmt.stop();
    }

    @Test
    public void testSingleAspInAs() throws Exception {
        System.out.println("Starting server");
        server.start();
        Thread.sleep(100);
        System.out.println("Starting Client");
        client.start();
        Thread.sleep(10000);
        assertEquals(AspState.ACTIVE, AspState.getState(remAsp.getPeerFSM().getState().getName()));
        assertEquals(AsState.ACTIVE, AsState.getState(remAs.getLocalFSM().getState().getName()));
        assertEquals(AspState.ACTIVE, AspState.getState(localAsp.getLocalFSM().getState().getName()));
        assertEquals(AsState.ACTIVE, AsState.getState(localAs.getPeerFSM().getState().getName()));
        client.sendPayload();
        server.sendPayload();
        Thread.sleep(1000);
        client.stop();
        logger.debug("Stopped Client");
        Thread.sleep(100);
        assertEquals(AsState.PENDING, AsState.getState(localAs.getPeerFSM().getState().getName()));
        assertEquals(AsState.PENDING, AsState.getState(remAs.getLocalFSM().getState().getName()));
        Thread.sleep(4000);
        logger.debug("Woke from 4000 sleep");
        assertEquals(AsState.DOWN, AsState.getState(localAs.getPeerFSM().getState().getName()));
        assertEquals(AsState.DOWN, AsState.getState(remAs.getLocalFSM().getState().getName()));
        client.stopClient();
        server.stop();
        Thread.sleep(100);
        assertEquals(2, mtp3UserPartListener.getReceivedData().size());
    }

    private class Client {

        public Client() {
        }

        public void start() throws Exception {
            sctpManagement.addAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_HOST, SERVER_PORT, CLIENT_ASSOCIATION_NAME);
            RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
            TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
            localAs = m3uaMgmt.createAs("client-testas", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, null);
            localAspFactory = m3uaMgmt.createAspFactory("client-testasp", CLIENT_ASSOCIATION_NAME);
            localAsp = m3uaMgmt.assignAspToAs("client-testas", "client-testasp");
            m3uaMgmt.addRoute(1408, -1, -1, "client-testas");
            m3uaMgmt.startAsp("client-testasp");
        }

        public void stop() throws Exception {
            m3uaMgmt.stopAsp("client-testasp");
        }

        public void stopClient() throws Exception {
            m3uaMgmt.removeRoute(1408, -1, -1, "client-testas");
            m3uaMgmt.unassignAspFromAs("client-testas", "client-testasp");
            m3uaMgmt.destroyAspFactory("client-testasp");
            m3uaMgmt.destroyAs("client-testas");
            sctpManagement.removeAssociation(CLIENT_ASSOCIATION_NAME);
        }

        public void sendPayload() throws Exception {
            Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(3, 1, 0, 123, 1408, 1, new byte[] { 1, 2, 3, 4 });
            m3uaMgmt.sendMessage(mtp3TransferPrimitive);
        }
    }

    private class Server {

        public Server() {
        }

        private void start() throws Exception {
            sctpManagement.addServer(SERVER_NAME, SERVER_HOST, SERVER_PORT);
            sctpManagement.addServerAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME);
            sctpManagement.startServer(SERVER_NAME);
            RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
            TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
            remAs = m3uaMgmt.createAs("server-testas", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, null);
            remAspFactory = m3uaMgmt.createAspFactory("server-testasp", SERVER_ASSOCIATION_NAME);
            remAsp = m3uaMgmt.assignAspToAs("server-testas", "server-testasp");
            m3uaMgmt.addRoute(123, -1, -1, "server-testas");
            m3uaMgmt.startAsp("server-testasp");
        }

        public void stop() throws Exception {
            m3uaMgmt.stopAsp("server-testasp");
            m3uaMgmt.removeRoute(123, -1, -1, "server-testas");
            m3uaMgmt.unassignAspFromAs("server-testas", "server-testasp");
            m3uaMgmt.destroyAspFactory("server-testasp");
            m3uaMgmt.destroyAs("server-testas");
            sctpManagement.removeAssociation(SERVER_ASSOCIATION_NAME);
            sctpManagement.stopServer(SERVER_NAME);
            sctpManagement.removeServer(SERVER_NAME);
        }

        public void sendPayload() throws Exception {
            Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(3, 1, 0, 1408, 123, 1, new byte[] { 1, 2, 3, 4 });
            m3uaMgmt.sendMessage(mtp3TransferPrimitive);
        }
    }

    private class Mtp3UserPartListenerImpl implements Mtp3UserPartListener {

        private FastList<Mtp3TransferPrimitive> receivedData = new FastList<Mtp3TransferPrimitive>();

        public FastList<Mtp3TransferPrimitive> getReceivedData() {
            return receivedData;
        }

        @Override
        public void onMtp3PauseMessage(Mtp3PausePrimitive arg0) {
        }

        @Override
        public void onMtp3ResumeMessage(Mtp3ResumePrimitive arg0) {
        }

        @Override
        public void onMtp3StatusMessage(Mtp3StatusPrimitive arg0) {
        }

        @Override
        public void onMtp3TransferMessage(Mtp3TransferPrimitive value) {
            receivedData.add(value);
        }
    }
}
