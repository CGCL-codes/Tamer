package ca.on.gov.mgs.gateway;

import junit.framework.*;
import java.util.Properties;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.DeclarativeQueryManager;
import javax.xml.registry.JAXRException;
import javax.xml.registry.Query;
import javax.xml.registry.RegistryService;
import org.freebxml.omar.client.xml.registry.util.JAXRUtility;

/**
 *
 * @author najmi
 */
public class MessageReceiverTest extends TestCase {

    MessageReceiver rcvr = new MessageReceiver();

    public MessageReceiverTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MessageReceiverTest.class);
        return suite;
    }

    /**
     * Test of handleMessage method, of class ca.on.gov.mgs.gateway.GOCMSReceiever.
     */
    public void testHandleMessage() throws Exception {
        System.out.println("testHandleMessage");
    }
}
