package net.sf.agentopia.platform.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import junit.framework.JUnit4TestAdapter;
import net.sf.agentopia.core.AgentopiaConstants;
import net.sf.agentopia.platform.AbstractAgent;
import net.sf.agentopia.platform.IAgentopiaAgent;
import net.sf.agentopia.platform.IAgentopiaServerRunnable;
import net.sf.agentopia.platform.IMarketPlace;
import net.sf.agentopia.platform.MarketPlaceTest.MockupEnvironment;
import net.sf.agentopia.util.Logger;
import net.sf.agentopia.util.OS;
import net.sf.agentopia.util.net.HostId;
import org.junit.Test;

/**
 * Tests the abstract agents (without any networks).
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 2008
 */
public class ConnectionInputHandlerTest {

    /** The first expected int flag to be sent around. */
    private static final int EXPECTED_FIRST_FLAG = AgentopiaConstants.PACKET_PING;

    /**
     * @return A suite with this class.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ConnectionInputHandlerTest.class);
    }

    /**
     * @throws Exception If something failed.
     */
    @Test
    public void testConnectionInputHandler() throws Exception {
        MockupEnvironment env = new MockupEnvironment();
        env.removeExits();
        IMarketPlace market = env.getMarket1();
        assertNotNull(market);
        final HostId serverId = new HostId("localhost:15907");
        MockupConnectionInputHandlerServer server = new MockupConnectionInputHandlerServer(serverId, market);
        server.start();
        IAgentopiaAgent agent = new SustainerTest.MockupNothingAgent();
        assertTrue(agent instanceof AbstractAgent);
        Communicator comm;
        int intFlag;
        assertEquals(0, server.successCount);
        comm = createCommunicator(serverId);
        comm.writeInt(AgentopiaConstants.HEADER_AGENT);
        intFlag = comm.readInt();
        assertEquals(AgentopiaConstants.HEADER_AGENT, intFlag);
        comm.transferMe(agent);
        OS.sleep(100);
        assertEquals(1, server.successCount);
        comm.shutDown();
        assertTrue(true);
        assertFalse(false);
        comm = createCommunicator(serverId);
        comm.writeInt(AgentopiaConstants.MESSAGE_QUIT);
        OS.sleep(100);
        assertEquals(2, server.successCount);
        assertFalse(server.isAlive());
        server.shutDown();
        assertTrue(server.serverSocket.isClosed());
        assertEquals(0, Logger.getLogger().getWarningCount());
    }

    /**
     * Creates a communicator.
     * 
     * @param serverId The server id.
     * @return The communicator.
     * @throws IOException If connection failed.
     */
    private Communicator createCommunicator(HostId serverId) throws IOException {
        Communicator comm = Communicator.createBasicCommunicator(serverId, EXPECTED_FIRST_FLAG);
        int intFlag = comm.readInt();
        assertEquals(AgentopiaConstants.PACKET_PING, intFlag);
        return comm;
    }

    /**
     * A server that answers to a communicators request.
     * 
     * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
     * @since 2008
     */
    private static class MockupConnectionInputHandlerServer extends Thread implements IAgentopiaServerRunnable {

        /** The number of successes. */
        public int successCount = 0;

        /** The servers host id. */
        public HostId hostId;

        /** The market to operate on. */
        public IMarketPlace market;

        /** The server socket. */
        public ServerSocket serverSocket;

        /** The socket to the target. */
        private Socket clientSocket;

        /** The input channel from the target. */
        private DataInputStream dataIn;

        /** The output channel to the target. */
        private DataOutputStream dataOut;

        /**
         * A new mockup server.
         * 
         * @param hostId The host id for the server. Must be localhost.
         * @param market The market place to operate on.
         */
        public MockupConnectionInputHandlerServer(HostId hostId, IMarketPlace market) {
            this.hostId = hostId;
            this.market = market;
            super.setName("Mockup Server (Connection Input Handler)");
        }

        /**
         * @see java.lang.Thread#run()
         */
        public void run() {
            ConnectionInputHandler handler;
            try {
                serverSocket = new ServerSocket(hostId.getPort());
                boolean isRunning = true;
                while (isRunning) {
                    clientSocket = serverSocket.accept();
                    dataIn = new DataInputStream(clientSocket.getInputStream());
                    dataOut = new DataOutputStream(clientSocket.getOutputStream());
                    int intFlag = dataIn.readInt();
                    if (intFlag != EXPECTED_FIRST_FLAG) {
                        throw new IOException("First flag  \"" + EXPECTED_FIRST_FLAG + "\" expected, but was: " + intFlag);
                    }
                    dataOut.writeInt(EXPECTED_FIRST_FLAG);
                    handler = new ConnectionInputHandler(clientSocket, market);
                    handler.run();
                    if (handler.isSuccessful()) {
                        successCount++;
                    } else {
                        Logger.getLogger().warn("Unsuccessful in handling int flag: " + handler.getInitialIntFlag());
                    }
                    if (AgentopiaConstants.MESSAGE_QUIT == handler.getInitialIntFlag()) {
                        isRunning = false;
                    }
                }
                serverSocket.close();
            } catch (IOException exc) {
                successCount = -1;
                exc.printStackTrace();
            }
        }

        /**
         * @see net.sf.agentopia.platform.IAgentopiaServerRunnable#shutDown()
         */
        public void shutDown() {
        }
    }
}
