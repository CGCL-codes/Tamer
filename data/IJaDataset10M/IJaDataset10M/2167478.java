package org.freelords.network.server;

import java.util.UUID;
import com.esotericsoftware.kryonet.Connection;
import org.freelords.network.fake.FakeFLNetworkConnection;
import org.freelords.network.fake.FakeUpdate;
import org.freelords.network.server.fake.FakeKryonetServer;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UpdaterTest {

    private static int NUM_CONNECTION = 2;

    private Updater updater;

    private FakeKryonetServer server;

    private FakeFLNetworkConnection connection;

    @Before
    public void setup() {
        server = new FakeKryonetServer();
        updater = new Updater(server);
        for (int i = 0; i < NUM_CONNECTION; i++) {
            connection = new FakeFLNetworkConnection();
            connection.setClientId(UUID.randomUUID());
            server.addConnection(connection);
        }
        server.addConnection(new FakeFLNetworkConnection());
    }

    /**
	 * Test count of connections which should be returned by Updater.
	 * There are 3 connections in FakeKryonetServer, but only 2 with a client id.
	 */
    @Test
    public void testListConnections() {
        int size = updater.listRegisteredConnections().size();
        assertEquals("Invalid connections count", NUM_CONNECTION, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionwithNullServer() {
        updater = new Updater(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBroadcastWithNullUpdate() {
        updater.broadcastUpdate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendUpdateWithNullUpdate() {
        UUID clientId = connection.getClientId();
        updater.sendUpdate(clientId, null);
    }

    /**
	 * Test that update does not send to client with invalid id
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testSendUpdateFailed() {
        updater.sendUpdate(UUID.randomUUID(), new FakeUpdate());
    }

    /**
	 * Test that connection that have clientId received broadcast message.
	 */
    @Test
    public void testBroadcast() {
        updater.broadcastUpdate(new FakeUpdate());
        Connection[] connections = server.getConnections();
        for (int i = 0; i < connections.length; i++) {
            FakeFLNetworkConnection con = (FakeFLNetworkConnection) connections[i];
            if (con.getClientId() == null) {
                assertFalse("Broadcast to uninitialized connection.", con.isCalled());
            } else {
                assertTrue("No broadcast to valid connection.", con.isCalled());
            }
        }
    }

    /**
	 * Test that a connection with a specified id receives an Update
	 */
    @Test
    public void testSendUpdate() {
        UUID clientId = connection.getClientId();
        updater.sendUpdate(clientId, new FakeUpdate());
        Connection[] connections = server.getConnections();
        for (int i = 0; i < connections.length; i++) {
            FakeFLNetworkConnection con = (FakeFLNetworkConnection) connections[i];
            if (clientId.equals(con.getClientId())) {
                assertTrue("Connection with specified id did not receive Update.", con.isCalled());
            } else {
                assertFalse("Wrong connection was called.", con.isCalled());
            }
        }
    }
}
