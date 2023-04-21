package it.inrich.enterprise;

import junit.framework.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author riccardo
 */
public class ConnectionFactoryTest extends TestCase {

    public ConnectionFactoryTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ConnectionFactoryTest.class);
        return suite;
    }

    /**
     * Test of connectionFactory method, of class it.inrich.enterprise.ConnectionFactory.
     */
    public void testConnectionFactory() throws Exception {
        System.out.println("connectionFactory");
        Connection expResult = null;
        Connection result = ConnectionFactory.connectionFactory();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
