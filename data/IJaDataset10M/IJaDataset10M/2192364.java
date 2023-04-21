package net.sf.rcer.conn.connections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import net.sf.rcer.test.provider.ConnectionProvider;
import org.junit.Test;
import com.sap.conn.jco.JCoDestination;

/**
 * A set of tests for the various state handling functions of the {@link ConnectionManager}
 * @author vwegert
 *
 */
public class OpenCloseActiveTest extends ConnectionManagerTest {

    private static final String CONNECTION_ID_1 = ConnectionProvider.PROVIDER_ID + "#1";

    private static final String CONNECTION_ID_2 = ConnectionProvider.PROVIDER_ID + "#2";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ConnectionProvider.setAvailableConnections(2);
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#getActiveConnectionIDs()}.
	 * @throws Exception 
	 */
    @Test
    public void testGetActiveConnectionIDsNoConnections() throws Exception {
        Set<String> ids = manager.getActiveConnectionIDs();
        assertNotNull("Result should never be null.", ids);
        assertTrue("No connections should be active.", ids.isEmpty());
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#getActiveConnectionIDs()}.
	 * @throws Exception 
	 */
    @Test
    public void testGetActiveConnectionIDsOneConnection() throws Exception {
        JCoDestination dest = manager.getDestination(CONNECTION_ID_1);
        Set<String> ids = manager.getActiveConnectionIDs();
        assertNotNull("Result should never be null.", ids);
        assertEquals("Number of active connections", 1, ids.size());
        assertTrue("ID of active connection should be returned", ids.contains(dest.getDestinationName()));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#getActiveConnectionIDs()}.
	 * @throws Exception 
	 */
    @Test
    public void testGetActiveConnectionIDsMultipleConnections() throws Exception {
        JCoDestination dest1 = manager.getDestination(CONNECTION_ID_1);
        JCoDestination dest2 = manager.getDestination(CONNECTION_ID_2);
        Set<String> ids = manager.getActiveConnectionIDs();
        assertNotNull("Result should never be null.", ids);
        assertEquals("Number of active connections", 2, ids.size());
        assertTrue("ID of active connection should be returned", ids.contains(dest1.getDestinationName()));
        assertTrue("ID of active connection should be returned", ids.contains(dest2.getDestinationName()));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#isActive(net.sf.rcer.conn.connections.IConnectionData)}.
	 * @throws Exception 
	 */
    @Test
    public void testIsActiveIConnectionDataNoConnections() throws Exception {
        IConnectionData conn1 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_1);
        IConnectionData conn2 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_2);
        assertFalse("Connection should not be active", manager.isActive(conn1));
        assertFalse("Connection should not be active", manager.isActive(conn2));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#isActive(net.sf.rcer.conn.connections.IConnectionData)}.
	 * @throws Exception 
	 */
    @Test
    public void testIsActiveIConnectionDataOneConnection() throws Exception {
        IConnectionData conn1 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_1);
        IConnectionData conn2 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_2);
        @SuppressWarnings("unused") JCoDestination dest1 = manager.getDestination(conn1);
        assertTrue("Connection should be active", manager.isActive(conn1));
        assertFalse("Connection should not be active", manager.isActive(conn2));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#isActive(net.sf.rcer.conn.connections.IConnectionData)}.
	 * @throws Exception 
	 */
    @Test
    public void testIsActiveIConnectionDataMultipleConnections() throws Exception {
        IConnectionData conn1 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_1);
        IConnectionData conn2 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_2);
        @SuppressWarnings("unused") JCoDestination dest1 = manager.getDestination(conn1);
        @SuppressWarnings("unused") JCoDestination dest2 = manager.getDestination(conn2);
        assertTrue("Connection should be active", manager.isActive(conn1));
        assertTrue("Connection should be active", manager.isActive(conn2));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#isActive(java.lang.String)}.
	 * @throws Exception 
	 */
    @Test
    public void testIsActiveStringNoConnections() throws Exception {
        assertFalse("Connection should not be active", manager.isActive(CONNECTION_ID_1));
        assertFalse("Connection should not be active", manager.isActive(CONNECTION_ID_2));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#isActive(java.lang.String)}.
	 * @throws Exception 
	 */
    @Test
    public void testIsActiveStringOneConnection() throws Exception {
        JCoDestination dest1 = manager.getDestination(CONNECTION_ID_1);
        assertTrue("Connection should be active", manager.isActive(dest1.getDestinationName()));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#isActive(java.lang.String)}.
	 * @throws Exception 
	 */
    @Test
    public void testIsActiveStringMultipleConnections() throws Exception {
        JCoDestination dest1 = manager.getDestination(CONNECTION_ID_1);
        JCoDestination dest2 = manager.getDestination(CONNECTION_ID_2);
        assertTrue("Connection should be active", manager.isActive(dest1.getDestinationName()));
        assertTrue("Connection should be active", manager.isActive(dest2.getDestinationName()));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#isConnected()}.
	 * @throws Exception 
	 */
    @Test
    public void testIsConnected() throws Exception {
        assertFalse(manager.isConnected());
        JCoDestination dest1 = manager.getDestination(CONNECTION_ID_1);
        assertTrue(manager.isConnected());
        JCoDestination dest2 = manager.getDestination(CONNECTION_ID_2);
        assertTrue(manager.isConnected());
        manager.closeConnection(dest1);
        assertTrue(manager.isConnected());
        manager.closeConnection(dest2);
        assertFalse(manager.isConnected());
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#closeConnection(com.sap.conn.jco.JCoDestination)}.
	 * @throws Exception 
	 */
    @Test
    public void testCloseConnectionJCoDestination() throws Exception {
        JCoDestination dest1 = manager.getDestination(CONNECTION_ID_1);
        JCoDestination dest2 = manager.getDestination(CONNECTION_ID_2);
        assertTrue("Connection should be active", manager.isActive(dest1.getDestinationName()));
        assertTrue("Connection should be active", manager.isActive(dest2.getDestinationName()));
        manager.closeConnection(dest1);
        assertFalse("Connection should not be active", manager.isActive(dest1.getDestinationName()));
        assertTrue("Connection should be active", manager.isActive(dest2.getDestinationName()));
        manager.closeConnection(dest2);
        assertFalse("Connection should not be active", manager.isActive(dest1.getDestinationName()));
        assertFalse("Connection should not be active", manager.isActive(dest2.getDestinationName()));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#closeConnection(net.sf.rcer.conn.connections.IConnection)}.
	 * @throws Exception 
	 */
    @Test
    public void testCloseConnectionIConnection() throws Exception {
        IConnectionData conn1 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_1);
        IConnectionData conn2 = ConnectionRegistry.getInstance().getConnectionData(CONNECTION_ID_2);
        @SuppressWarnings("unused") JCoDestination dest1 = manager.getDestination(conn1);
        @SuppressWarnings("unused") JCoDestination dest2 = manager.getDestination(conn2);
        Collection<IConnection> conns = manager.getActiveConnections();
        assertEquals(2, conns.size());
        IConnection connection1 = null;
        IConnection connection2 = null;
        for (Iterator<IConnection> it = conns.iterator(); it.hasNext(); ) {
            IConnection conn = it.next();
            if (conn.getConnectionDataID().equals(CONNECTION_ID_1)) {
                connection1 = conn;
            }
            if (conn.getConnectionDataID().equals(CONNECTION_ID_2)) {
                connection2 = conn;
            }
        }
        assertNotNull(connection1);
        assertNotNull(connection2);
        assertTrue("Connection should be active", manager.isActive(connection1));
        assertTrue("Connection should be active", manager.isActive(connection2));
        manager.closeConnection(connection1);
        assertFalse("Connection should not be active", manager.isActive(connection1));
        assertTrue("Connection should be active", manager.isActive(connection2));
        manager.closeConnection(connection2);
        assertFalse("Connection should not be active", manager.isActive(connection1));
        assertFalse("Connection should not be active", manager.isActive(connection2));
    }

    /**
	 * Test method for {@link net.sf.rcer.conn.connections.ConnectionManager#closeConnection(java.lang.String)}.
	 * @throws Exception 
	 */
    @Test
    public void testCloseConnectionString() throws Exception {
        JCoDestination dest1 = manager.getDestination(CONNECTION_ID_1);
        JCoDestination dest2 = manager.getDestination(CONNECTION_ID_2);
        assertTrue("Connection should be active", manager.isActive(dest1.getDestinationName()));
        assertTrue("Connection should be active", manager.isActive(dest2.getDestinationName()));
        manager.closeConnection(dest1.getDestinationName());
        assertFalse("Connection should not be active", manager.isActive(dest1.getDestinationName()));
        assertTrue("Connection should be active", manager.isActive(dest2.getDestinationName()));
        manager.closeConnection(dest2.getDestinationName());
        assertFalse("Connection should not be active", manager.isActive(dest1.getDestinationName()));
        assertFalse("Connection should not be active", manager.isActive(dest2.getDestinationName()));
    }
}
