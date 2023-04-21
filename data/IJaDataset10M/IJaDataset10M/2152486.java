package fedora.test.api;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import junit.framework.Test;
import junit.framework.TestSuite;
import fedora.common.PID;
import fedora.server.management.FedoraAPIM;
import fedora.test.DemoObjectTestSetup;
import fedora.test.FedoraServerTestCase;

/**
 * Performs tests to check notifications provided when management services
 * are exercised. Notifications are assumed to be via JMS.
 *
 * @author Bill Branan
 */
public class TestManagementNotifications extends FedoraServerTestCase implements MessageListener {

    private FedoraAPIM apim;

    private TextMessage currentMessage;

    private int messageCount = 0;

    private int messageNumber = 0;

    private final int messageTimeout = 5000;

    private Connection jmsConnection;

    private Session jmsSession;

    private Destination destination;

    private MessageConsumer messageConsumer;

    public static byte[] dsXML;

    public static byte[] demo998FOXMLObjectXML;

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("<oai_dc:dc xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\">");
        sb.append("<dc:title>Dublin Core Record</dc:title>");
        sb.append("<dc:creator>Author</dc:creator>");
        sb.append("<dc:subject>Subject</dc:subject>");
        sb.append("<dc:description>Description</dc:description>");
        sb.append("<dc:publisher>Publisher</dc:publisher>");
        sb.append("<dc:format>MIME type</dc:format>");
        sb.append("<dc:identifier>Identifier</dc:identifier>");
        sb.append("</oai_dc:dc>");
        try {
            dsXML = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
        }
        sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<foxml:digitalObject VERSION=\"1.1\" PID=\"demo:998\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"info:fedora/fedora-system:def/foxml# http://www.fedora.info/definitions/1/0/foxml1-1.xsd\">");
        sb.append("  <foxml:objectProperties>");
        sb.append("    <foxml:property NAME=\"info:fedora/fedora-system:def/model#state\" VALUE=\"A\"/>");
        sb.append("    <foxml:property NAME=\"info:fedora/fedora-system:def/model#label\" VALUE=\"Image of Coliseum in Rome\"/>");
        sb.append("    <foxml:property NAME=\"info:fedora/fedora-system:def/model#createdDate\" VALUE=\"2004-12-10T00:21:57Z\"/>");
        sb.append("    <foxml:property NAME=\"info:fedora/fedora-system:def/view#lastModifiedDate\" VALUE=\"2004-12-10T00:21:57Z\"/>");
        sb.append("  </foxml:objectProperties>");
        sb.append("</foxml:digitalObject>");
        try {
            demo998FOXMLObjectXML = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Management Notifications TestSuite");
        suite.addTestSuite(TestManagementNotifications.class);
        return new DemoObjectTestSetup(suite);
    }

    @Override
    public void setUp() throws Exception {
        apim = getFedoraClient().getAPIM();
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        props.setProperty("topic.notificationTopic", "fedora.apim.update");
        Context jndi = new InitialContext(props);
        ConnectionFactory jmsConnectionFactory = (ConnectionFactory) jndi.lookup("ConnectionFactory");
        jmsConnection = jmsConnectionFactory.createConnection();
        jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = (Topic) jndi.lookup("notificationTopic");
        messageConsumer = jmsSession.createConsumer(destination);
        messageConsumer.setMessageListener(this);
        jmsConnection.start();
    }

    @Override
    public void tearDown() throws Exception {
        jmsConnection.stop();
        jmsSession.close();
        jmsConnection.close();
    }

    /**
     * Tests notifications on
     * 1) ingest
     * 2) modifyObject
     * 3) addRelationship
     * 4) purgeRelationship
     * 5) purgeObject
     *
     * @throws Exception
     */
    public void testObjectMethodNotifications() throws Exception {
        System.out.println("Running TestManagementNotifications.testIngest...");
        String pid = apim.ingest(demo998FOXMLObjectXML, FOXML1_1.uri, "ingesting new foxml object");
        assertNotNull(pid);
        checkNotification(pid, "ingest");
        System.out.println("Running TestManagementNotifications.testModifyObject...");
        String modifyResult = apim.modifyObject(pid, "I", "Updated Object Label", null, "Changed state to inactive and updated label");
        assertNotNull(modifyResult);
        checkNotification(pid, "modifyObject");
        System.out.println("Running TestManagementNotifications.testAddRelationship...");
        boolean addRelResult = apim.addRelationship(pid, "rel:isRelatedTo", "demo:5", false, null);
        assertTrue(addRelResult);
        checkNotification(pid, "addRelationship");
        System.out.println("Running TestManagementNotifications.testAddRelationship...");
        addRelResult = apim.addRelationship(PID.toURI(pid), "rel:isRelatedTo", "demo:6", false, null);
        assertTrue(addRelResult);
        checkNotification(pid, "addRelationship");
        System.out.println("Running TestManagementNotifications.testAddRelationship...");
        addRelResult = apim.addRelationship(PID.toURI(pid) + "/DS1", "rel:isRelatedTo", "demo:7", false, null);
        assertTrue(addRelResult);
        checkNotification(pid, "addRelationship");
        System.out.println("Running TestManagementNotifications.testPurgeRelationship...");
        boolean purgeRelResult = apim.purgeRelationship(pid, "rel:isRelatedTo", "demo:5", false, null);
        assertTrue(purgeRelResult);
        checkNotification(pid, "purgeRelationship");
        System.out.println("Running TestManagementNotifications.testPurgeRelationship...");
        purgeRelResult = apim.purgeRelationship(PID.toURI(pid), "rel:isRelatedTo", "demo:6", false, null);
        assertTrue(purgeRelResult);
        checkNotification(pid, "purgeRelationship");
        System.out.println("Running TestManagementNotifications.testPurgeRelationship...");
        purgeRelResult = apim.purgeRelationship(PID.toURI(pid) + "/DS1", "rel:isRelatedTo", "demo:7", false, null);
        assertTrue(purgeRelResult);
        checkNotification(pid, "purgeRelationship");
        System.out.println("Running TestManagementNotifications.testPurgeObject...");
        String purgeResult = apim.purgeObject(pid, "Purging object " + pid, false);
        assertNotNull(purgeResult);
        checkNotification(pid, "purgeObject");
    }

    /**
     *  Test notifications on
     *  1) addDatastream
     *  2) modifyDatastreamByReference
     *  3) modifyDatastreamByValue
     *  4) setDatastreamState
     *  5) setDatastreamVersionable
     *  6) purgeDatastream
     *
     * @throws Exception
     */
    public void testDatastreamMethodNotifications() throws Exception {
        System.out.println("Running TestManagementNotifications.testAddDatastream...");
        String[] altIds = new String[1];
        altIds[0] = "Datastream Alternate ID";
        String pid = "demo:14";
        String datastreamId = apim.addDatastream(pid, "NEWDS1", altIds, "A New M-type Datastream", true, "text/xml", "info:myFormatURI/Mtype/stuff#junk", getBaseURL() + "/get/fedora-system:ContentModel-3.0/DC", "M", "A", null, null, "adding new datastream");
        assertEquals(datastreamId, "NEWDS1");
        checkNotification(pid, "addDatastream");
        datastreamId = apim.addDatastream(pid, "NEWDS2", altIds, "A New X-type Datastream", true, "text/xml", "info:myFormatURI/Mtype/stuff#junk", getBaseURL() + "/get/fedora-system:ContentModel-3.0/DC", "X", "A", null, null, "adding new datastream");
        assertEquals(datastreamId, "NEWDS2");
        checkNotification(pid, "addDatastream");
        System.out.println("Running TestManagementNotifications.testModifyDatastreamByReference...");
        String updateTimestamp = apim.modifyDatastreamByReference(pid, "NEWDS1", altIds, "Modified Datastream by Reference", "text/xml", "info:newMyFormatURI/Mtype/stuff#junk", getBaseURL() + "/get/fedora-system:ContentModel-3.0/DC", null, null, "modified datastream", false);
        assertNotNull(updateTimestamp);
        checkNotification(pid, "modifyDatastreamByReference");
        System.out.println("Running TestManagementNotifications.testModifyDatastreamByValue...");
        updateTimestamp = apim.modifyDatastreamByValue(pid, "NEWDS2", altIds, "Modified Datastream by Value", "text/xml", "info:newMyFormatURI/Xtype/stuff#junk", dsXML, null, null, "modified datastream", false);
        assertNotNull(updateTimestamp);
        checkNotification(pid, "modifyDatastreamByValue");
        System.out.println("Running TestManagementNotifications.testSetDatastreamState...");
        String setStateresult = apim.setDatastreamState(pid, "NEWDS1", "I", "Changed state of datstream DC to Inactive");
        assertNotNull(setStateresult);
        checkNotification(pid, "setDatastreamState");
        System.out.println("Running TestManagementNotifications.testSetDatastreamVersionable...");
        String setVersionableResult = apim.setDatastreamVersionable(pid, "NEWDS2", false, "Changed versionable on datastream NEWDS1 to false");
        assertNotNull(setVersionableResult);
        checkNotification(pid, "setDatastreamVersionable");
        System.out.println("Running TestManagementNotifications.testPurgeDatastream...");
        String[] results = apim.purgeDatastream(pid, "NEWDS1", null, null, "purging datastream NEWDS1", false);
        assertTrue(results.length > 0);
        checkNotification(pid, "purgeDatastream");
        results = apim.purgeDatastream(pid, "NEWDS2", null, null, "purging datastream NEWDS2", false);
        assertTrue(results.length > 0);
        checkNotification(pid, "purgeDatastream");
    }

    public void testSelectors() throws Exception {
        System.out.println("Running TestManagementNotifications.testSelectors...");
        messageConsumer.close();
        String messageSelector = "methodName LIKE 'ingest%'";
        messageConsumer = jmsSession.createConsumer(destination, messageSelector);
        messageConsumer.setMessageListener(this);
        String pid = apim.ingest(demo998FOXMLObjectXML, FOXML1_1.uri, "ingesting new foxml object");
        assertNotNull(pid);
        checkNotification(pid, "ingest");
        String purgeResult = apim.purgeObject(pid, "Purging object " + pid, false);
        assertNotNull(purgeResult);
        checkNoNotifications();
    }

    /**
     * Waits for a notification message and checks to see if the message
     * body includes the includedText.
     *
     * @param methodName - the text that should be found in the message body
     */
    private void checkNotification(String pid, String methodName) throws Exception {
        long startTime = System.currentTimeMillis();
        messageNumber++;
        while (true) {
            if (messageCount >= messageNumber) {
                String failureText = "Notification did not include text: " + methodName;
                assertTrue(failureText, currentMessage.getText().contains(methodName));
                failureText = "Notification did not include methodName property with " + "value: " + methodName;
                assertTrue(failureText, methodName.equals(currentMessage.getStringProperty("methodName")));
                failureText = "Notification did not include pid property with " + "value: " + pid;
                assertTrue(failureText, pid.equals(currentMessage.getStringProperty("pid")));
                break;
            } else {
                long currentTime = System.currentTimeMillis();
                if (currentTime > (startTime + messageTimeout)) {
                    fail("Timeout reached waiting for notification " + "on message regarding: " + methodName);
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        currentMessage = null;
    }

    /**
     * Waits for a notification to make sure none come through.
     */
    private void checkNoNotifications() {
        long startTime = System.currentTimeMillis();
        while (true) {
            if (messageCount > messageNumber) {
                fail("No messages should be received during this test.");
                break;
            } else {
                long currentTime = System.currentTimeMillis();
                if (currentTime > (startTime + messageTimeout)) {
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        currentMessage = null;
    }

    /**
     * Handles messages sent as notifications.
     *
     * {@inheritDoc}
     */
    public void onMessage(Message msg) {
        if (msg instanceof TextMessage) {
            currentMessage = (TextMessage) msg;
            messageCount++;
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestManagementNotifications.class);
    }
}
