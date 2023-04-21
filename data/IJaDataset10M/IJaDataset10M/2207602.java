package net.sf.rcer.conn.connections;

import net.sf.rcer.test.provider.ConnectionProvider;
import net.sf.rcer.test.provider.CredentialsProvider;
import org.junit.After;
import org.junit.Before;

/**
 * Abstract super-class to test the {@link ConnectionManager}. Defines minimal common behaviour.
 * @author vwegert
 *
 */
public abstract class ConnectionManagerTest {

    /**
	 * The connection manager for easier access.
	 */
    protected ConnectionManager manager;

    /**
	 * The set-up method. 
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        ConnectionProvider.setAvailableConnections(1);
        CredentialsProvider.reset();
        manager = ConnectionManager.getInstance();
        manager.closeConnections();
    }

    /**
	 * The tear-down method. 
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
        manager.closeConnections();
    }
}
