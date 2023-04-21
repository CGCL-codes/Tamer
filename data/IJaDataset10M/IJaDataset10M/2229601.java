package com.technoetic.tornado;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import com.technoetic.tornado.config.DatabaseConfiguration;
import com.technoetic.tornado.config.TransactionListenerConfiguration;
import com.technoetic.tornado.event.TransactionEvent;
import com.technoetic.tornado.event.TransactionEventListener;

public class TestJdbcDatabaseContext {

    private static final String CONFIG_FILENAME = "CONFIG_FILENAME";

    private static final String DATABASE_ENGINE = "DATABASE_ENGINE";

    private Transaction mockTransaction;

    private DatabaseConfiguration mockDatabaseConfiguration;

    private TransactionListenerConfiguration mockTxnListenerConfiguration;

    private JdbcDatabaseContextUnderTest context;

    private JdbcConnectionFactoryUnderTest mockJdbcConnectionFactory;

    private Connection mockConnection;

    public class JdbcConnectionFactoryUnderTest extends JdbcConnectionFactory {

        public JdbcConnectionFactoryUnderTest() {
            super(null, null, null);
        }

        public boolean getConnectionCalled;

        public Connection getConnectionReturn;

        public Connection getConnection() {
            getConnectionCalled = true;
            return getConnectionReturn;
        }
    }

    private class JdbcDatabaseContextUnderTest extends JdbcDatabaseContext {

        public DatabaseConfiguration newDatabaseConfiguration() {
            return mockDatabaseConfiguration;
        }

        public Transaction getTransaction() {
            return mockTransaction;
        }
    }

    public class TestObject {
    }

    public static class TransactionEventListenerForTest implements TransactionEventListener {

        public void onEvent(TransactionEvent event) {
        }
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        mockTransaction = mock(Transaction.class);
        mockTxnListenerConfiguration = mock(TransactionListenerConfiguration.class);
        stub((Class<TransactionEventListenerForTest>) mockTxnListenerConfiguration.getListener()).toReturn(TransactionEventListenerForTest.class);
        mockDatabaseConfiguration = mock(DatabaseConfiguration.class);
        stub(mockDatabaseConfiguration.getDatabaseEngineName()).toReturn(DATABASE_ENGINE);
        ArrayList<TransactionListenerConfiguration> listeners = new ArrayList<TransactionListenerConfiguration>();
        listeners.add(mockTxnListenerConfiguration);
        stub(mockDatabaseConfiguration.getTransactionListeners()).toReturn(listeners);
        mockConnection = mock(Connection.class);
        mockJdbcConnectionFactory = new JdbcConnectionFactoryUnderTest();
        mockJdbcConnectionFactory.getConnectionReturn = mockConnection;
        context = new JdbcDatabaseContextUnderTest();
        context.setConnectionFactory(mockJdbcConnectionFactory);
    }

    @Test
    public void testConfigure() throws Exception {
        context.configure(CONFIG_FILENAME);
        verify(mockDatabaseConfiguration).load(CONFIG_FILENAME);
        verify(mockDatabaseConfiguration).getTransactionListeners();
        verify(mockDatabaseConfiguration).getConnectionFactory();
        verify(mockDatabaseConfiguration).getDatabaseEngineName();
        assertEquals("wrong db engine", DATABASE_ENGINE, context.getDatabaseEngineName());
        assertTrue("context was not configured", context.isConfigured());
    }

    @Test
    public void testGetJdbcConnection() throws Exception {
        context.getJdbcConnection();
        assertTrue("factory not invoked", mockJdbcConnectionFactory.getConnectionCalled);
        verify(mockConnection).setAutoCommit(false);
    }

    @Test
    public void testReleaseJdbcConnection() throws Exception {
        context.getJdbcConnection();
        context.releaseJdbcConnection();
        context.releaseJdbcConnection();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testAddMapping() throws Exception {
        context.configure("");
        ObjectMappingImpl mapping = new ObjectMappingImpl(Object.class, new ArrayList<Object>(), new ArrayList<Object>(), new ArrayList<Object>(), "", new String[0]);
        context.addMapping(mapping);
        verify(mockDatabaseConfiguration).addMapping(mapping);
    }
}
