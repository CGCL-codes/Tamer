package org.mobicents.diameter.stack.functional.gx.base;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jdiameter.api.DisconnectCause;
import org.jdiameter.api.Mode;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerState;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Stack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
@RunWith(Parameterized.class)
public class GxSessionBasicFlowTest {

    private Client clientNode;

    private Server serverNode1;

    private URI clientConfigURI;

    private URI serverNode1ConfigURI;

    /**
   * @param clientNode
   * @param node1
   * @param node2
   * @param serverCount
   */
    public GxSessionBasicFlowTest(String clientConfigUrl, String serverNode1ConfigURL) throws Exception {
        super();
        this.clientConfigURI = new URI(clientConfigUrl);
        this.serverNode1ConfigURI = new URI(serverNode1ConfigURL);
    }

    @Before
    public void setUp() throws Exception {
        try {
            this.clientNode = new Client();
            this.serverNode1 = new Server();
            this.serverNode1.init(new FileInputStream(new File(this.serverNode1ConfigURI)), "SERVER1");
            this.serverNode1.start();
            this.clientNode.init(new FileInputStream(new File(this.clientConfigURI)), "CLIENT");
            this.clientNode.start(Mode.ANY_PEER, 10, TimeUnit.SECONDS);
            Stack stack = this.clientNode.getStack();
            List<Peer> peers = stack.unwrap(PeerTable.class).getPeerTable();
            if (peers.size() == 1) {
            } else if (peers.size() > 1) {
                boolean foundConnected = false;
                for (Peer p : peers) {
                    if (p.getState(PeerState.class).equals(PeerState.OKAY)) {
                        if (foundConnected) {
                            throw new Exception("Wrong number of connected peers: " + peers);
                        }
                        foundConnected = true;
                    }
                }
            } else {
                throw new Exception("Wrong number of connected peers: " + peers);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        if (this.serverNode1 != null) {
            try {
                this.serverNode1.stop(DisconnectCause.REBOOTING);
            } catch (Exception e) {
            }
            this.serverNode1 = null;
        }
        if (this.clientNode != null) {
            try {
                this.clientNode.stop(DisconnectCause.REBOOTING);
            } catch (Exception e) {
            }
            this.clientNode = null;
        }
    }

    @Test
    public void testBasicFlow() throws Exception {
        try {
            clientNode.sendInitial();
            waitForMessage();
            serverNode1.sendInitial();
            waitForMessage();
            clientNode.sendInterim();
            waitForMessage();
            serverNode1.sendInterim();
            waitForMessage();
            serverNode1.sendReAuth();
            waitForMessage();
            clientNode.sendReAuth();
            waitForMessage();
            clientNode.sendTermination();
            waitForMessage();
            serverNode1.sendTermination();
            waitForMessage();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
        if (!clientNode.isReceiveINITIAL()) {
            StringBuilder sb = new StringBuilder("Did not receive INITIAL! ");
            sb.append("Client ER:\n").append(clientNode.createErrorReport(this.clientNode.getErrors()));
            fail(sb.toString());
        }
        if (!clientNode.isReceiveINTERIM()) {
            StringBuilder sb = new StringBuilder("Did not receive INTERIM! ");
            sb.append("Client ER:\n").append(clientNode.createErrorReport(this.clientNode.getErrors()));
            fail(sb.toString());
        }
        if (!clientNode.isReceiveTERMINATE()) {
            StringBuilder sb = new StringBuilder("Did not receive TERMINATE! ");
            sb.append("Client ER:\n").append(clientNode.createErrorReport(this.clientNode.getErrors()));
            fail(sb.toString());
        }
        if (!clientNode.isPassed()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Client ER:\n").append(clientNode.createErrorReport(this.clientNode.getErrors()));
            fail(sb.toString());
        }
        if (!serverNode1.isReceiveINITIAL()) {
            StringBuilder sb = new StringBuilder("Did not receive INITIAL! ");
            sb.append("Server ER:\n").append(serverNode1.createErrorReport(this.serverNode1.getErrors()));
            fail(sb.toString());
        }
        if (!serverNode1.isReceiveINTERIM()) {
            StringBuilder sb = new StringBuilder("Did not receive INTERIM! ");
            sb.append("Server ER:\n").append(serverNode1.createErrorReport(this.serverNode1.getErrors()));
            fail(sb.toString());
        }
        if (!serverNode1.isReceiveTERMINATE()) {
            StringBuilder sb = new StringBuilder("Did not receive TERMINATE! ");
            sb.append("Server ER:\n").append(serverNode1.createErrorReport(this.serverNode1.getErrors()));
            fail(sb.toString());
        }
        if (!serverNode1.isPassed()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Server ER:\n").append(serverNode1.createErrorReport(this.serverNode1.getErrors()));
            fail(sb.toString());
        }
    }

    @Parameters
    public static Collection<Object[]> data() {
        String client = "configurations/functional-gx/config-client.xml";
        String server1 = "configurations/functional-gx/config-server-node1.xml";
        String replicatedClient = "configurations/functional-gx/replicated-config-client.xml";
        String replicatedServer1 = "configurations/functional-gx/replicated-config-server-node1.xml";
        Class<GxSessionBasicFlowTest> t = GxSessionBasicFlowTest.class;
        client = t.getClassLoader().getResource(client).toString();
        server1 = t.getClassLoader().getResource(server1).toString();
        replicatedClient = t.getClassLoader().getResource(replicatedClient).toString();
        replicatedServer1 = t.getClassLoader().getResource(replicatedServer1).toString();
        return Arrays.asList(new Object[][] { { client, server1 }, { replicatedClient, replicatedServer1 } });
    }

    private void waitForMessage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
