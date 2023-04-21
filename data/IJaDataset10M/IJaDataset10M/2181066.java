package com.neurogrid.simulation.email;

import java.util.Random;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import com.neurogrid.simulation.root.ContentMessage;

public class EmailMessageHandlerTest extends TestCase {

    EmailStorageNode o_node = null;

    EmailNetwork o_network = null;

    Random o_random = null;

    EmailSearchMessage o_search_msg = null;

    EmailSearchResponse o_response_msg = null;

    EmailNotificationMessage o_notif = null;

    EmailMessageHandler o_msg_handler = null;

    String[] o_stor_nodes = null;

    EmailFragment o_frag = null;

    Email o_email = null;

    private static Category o_cat = Category.getInstance(EmailMessageHandlerTest.class.getName());

    /**
	* Initialize the logging system
	*
	* @param p_conf The configuration filename
	*/
    public static void init(String p_conf) {
        BasicConfigurator.configure();
        PropertyConfigurator.configure(p_conf);
        o_cat.info("EmailMessageHandlerTest logging Initialized");
    }

    /**
	* Constructor for EmailMessageHandlerTest.
	* @param arg0 test name
	*/
    public EmailMessageHandlerTest(String arg0) {
        super(arg0);
        EmailMessageHandler.init(System.getProperty("Log4jConfig"));
        EmailMessageHandlerTest.init(System.getProperty("Log4jConfig"));
    }

    public static final int NO_FRAGMENTS = 6;

    public static final int NO_NOTIFICATION_FRAGMENTS = 4;

    /**
	*
	*/
    protected void setUp() throws Exception {
        super.setUp();
        o_network = new EmailNetwork();
        o_random = new Random();
        o_node = new EmailStorageNode(o_network, o_random);
        o_msg_handler = new EmailMessageHandler(o_random);
        o_email = new Email(NO_FRAGMENTS);
        o_frag = new EmailFragment(o_email);
        o_stor_nodes = new String[2];
        o_stor_nodes[0] = "EMAIL_NOD_000100";
        o_stor_nodes[1] = "EMAIL_NOD_000200";
        o_search_msg = new EmailSearchMessage("EMAIL_444444", o_node, "EMAIL_NOD_123456");
        o_response_msg = new EmailSearchResponse("EMAIL_NOD_555555", o_frag, o_node);
        o_notif = new EmailNotificationMessage(o_email, NO_NOTIFICATION_FRAGMENTS, o_stor_nodes, "EMAIL_NOD_999999", o_node);
    }

    /**
	*
	*/
    protected void tearDown() throws Exception {
        super.tearDown();
        o_network = null;
        o_random = null;
        o_node = null;
        o_msg_handler = null;
        o_email = null;
        o_frag = null;
        o_stor_nodes = null;
        o_search_msg = null;
        o_response_msg = null;
        o_notif = null;
    }

    /**
	 * 
	 */
    public void testEmailMessageHandler() {
    }

    /**
	 * 
	 */
    public void testHandleMessage() {
    }

    /**
	 * 
	 */
    public void testInjectMessage() {
    }

    /**
	 * 
	 */
    public void testInjectMessageEmailSearchMessage() {
    }

    /**
	 * 
	 */
    public void testHandleMessageEmailSearchResponse() {
    }

    /**
	 * 
	 */
    public void testInjectMessageEmailSearchResponse() {
    }

    /**
	 * 
	 */
    public void testInjectMessageEmailNotificationMessage() {
    }

    /**
	 * 
	 */
    public void testHandleMessageEmailErrorMessage() {
    }

    /**
	 * 
	 */
    public void testInjectMessageEmailErrorMessage() {
    }

    /**
	 * 
	 */
    public void testInjectMessageCreateUsernameMessage() {
    }

    /**
	 * 
	 */
    public void testHandleMessageCreateUsernameMessage() {
    }

    /**
	 * 
	 */
    public void testInjectMessageUsernameResponseMessage() {
    }

    /**
	 * 
	 */
    public void testHandleMessageUsernameResponseMessage() {
    }

    /**
	* @throws Exception - general exception
	*/
    public void testSeenMessage() throws Exception {
        String[] x_stor_nodes = new String[2];
        x_stor_nodes[0] = "EMAIL_NOD_000333333";
        x_stor_nodes[1] = "EMAIL_NOD_000444444";
        EmailNotificationMessage x_notif = new EmailNotificationMessage(o_email, 6, x_stor_nodes, "EMAIL_000222222", o_node);
        o_node.addToSeen(x_notif);
        assertTrue("Should have already seen this message", o_msg_handler.seenMessageAndAddToSeen(x_notif, o_node) == true);
        assertTrue("Should not have seen this message yet: notif msg", o_msg_handler.seenMessageAndAddToSeen(o_notif, o_node) == false);
        assertTrue("Should have now seen this message: notif msg", o_msg_handler.seenMessageAndAddToSeen(o_notif, o_node) == true);
        assertTrue("Should not yet have seen this message: search msg", o_msg_handler.seenMessageAndAddToSeen(o_search_msg, o_node) == false);
        assertTrue("Should have now seen this message: search msg", o_msg_handler.seenMessageAndAddToSeen(o_search_msg, o_node) == true);
        assertTrue("Should not yet have seen this message: response msg", o_msg_handler.seenMessageAndAddToSeen(o_response_msg, o_node) == false);
        assertTrue("Should have now seen this message: response msg", o_msg_handler.seenMessageAndAddToSeen(o_response_msg, o_node) == true);
    }

    /**
	* @throws Exception - general exception
	*/
    public void testHandleMessageEmailNotificationMessage() throws Exception {
        int x_no_notifs = o_node.getNoNotificationsStoring();
        EmailNotificationMessage x_notif = new EmailNotificationMessage(o_email, 5, o_stor_nodes, o_node.getNodeID(), o_node);
        assertTrue("message not handled correctly", o_msg_handler.handleMessage(x_notif, o_node) == true);
        assertTrue("message not being added to the notif store", o_node.getNoNotificationsStoring() == (x_no_notifs + 1));
    }

    /**
	* @throws Exception - general exception
	*/
    public void testHandleMessageEmailResponseMessage() throws Exception {
        int x_no_frags = o_node.o_frags_to_build_email_store.totalSize();
        o_response_msg = new EmailSearchResponse(o_node.getNodeID(), o_frag, o_node);
        assertTrue("message was not handled correctly: response msg", o_msg_handler.handleMessage(o_response_msg, o_node) == true);
        assertTrue("the fragment was not deposited on the node", o_node.o_frags_to_build_email_store.totalSize() == (x_no_frags + 1));
    }

    /**
	* @throws Exception - general exception
	*/
    public void testHandleMessageEmailSearchMessage() throws Exception {
        Email x_email = new Email(4);
        EmailFragment x_frag = new EmailFragment(x_email);
        o_search_msg = new EmailSearchMessage(x_email.getDocumentID(), o_node, o_node.getNodeID());
        o_node.storeEmailFragment(x_frag);
        assertTrue("Message not handled as expected: EmailSearchMessage: ", o_msg_handler.handleMessage(o_search_msg, o_node) == true);
    }

    /**
	*
	* @throws Exception a general exception
	*/
    public void testForwardMessage() throws Exception {
        final int x_no_node_arrays = 5;
        final int x_no_nodes0 = 79;
        final int x_no_nodes1 = 270;
        final int x_no_nodes2 = 440;
        final int x_no_nodes3 = 1350;
        final int x_no_nodes4 = 9005;
        final int[] x_no_nodes = new int[x_no_node_arrays];
        x_no_nodes[0] = x_no_nodes0;
        x_no_nodes[1] = x_no_nodes1;
        x_no_nodes[2] = x_no_nodes2;
        x_no_nodes[3] = x_no_nodes3;
        x_no_nodes[4] = x_no_nodes4;
        EmailStorageNode[] x_nodes = new EmailStorageNode[x_no_node_arrays];
        int j;
        for (int i = 0; i < x_no_node_arrays; i++) {
            for (j = 0; j < x_no_nodes[i]; j++) {
                x_nodes[i] = new EmailStorageNode(o_network, o_random);
            }
            o_node.addConnection(x_nodes[i]);
            o_cat.debug("x_nodes[" + i + "] id: " + x_nodes[i].getNodeID());
        }
        o_cat.debug("created all nodes");
        System.out.println("THIS NODE ID: " + o_node.getNodeID());
        for (int i = 0; i < x_nodes.length; i++) {
            System.out.println("NODE ID: " + x_nodes[i]);
        }
        ContentMessage x_msg = (ContentMessage) o_notif;
        String[] x_node_ids = new String[x_no_node_arrays];
        x_node_ids[0] = "EMAIL_NOD_000000111";
        x_node_ids[1] = "EMAIL_NOD_000010316";
        x_node_ids[2] = "EMAIL_NOD_000000360";
        x_node_ids[3] = "EMAIL_NOD_000000089";
        x_node_ids[4] = "EMAIL_NOD_000006650";
        EmailStorageNode x_closest_node = o_msg_handler.forwardMessage(x_msg, o_node, "EMAIL_NOD_000000111");
        assertTrue("The closest node was not found: ", x_closest_node != null);
        assertTrue("The closest node was incorrect: " + x_closest_node.getNodeID(), x_closest_node.getNodeID().equals(x_nodes[0].getNodeID()));
        x_closest_node = null;
        x_closest_node = o_msg_handler.forwardMessage(x_msg, o_node, "EMAIL_NOD_000010316");
        assertTrue("The closest node was not found : ", x_closest_node != null);
        assertTrue("The closest node was incorrect1: " + x_closest_node.getNodeID(), x_closest_node.getNodeID().equals(x_nodes[4].getNodeID()));
        x_closest_node = null;
        x_closest_node = o_msg_handler.forwardMessage(x_msg, o_node, "EMAIL_NOD_000000360");
        assertTrue("The closest node was not found: ", x_closest_node != null);
        assertTrue("The closest node was incorrect2: " + x_closest_node.getNodeID(), x_closest_node.getNodeID().equals(x_nodes[2].getNodeID()));
    }

    /**
	 * main method that supports individual running
	 * of different test methods
	 *
	 * @param args   array of strings that are incoming arguments
	 */
    public static void main(String[] args) {
        try {
            String x_method = System.getProperty("test.method");
            System.out.println("test.method=" + x_method);
            if (x_method == null || x_method.equals("")) {
                System.out.println("testing all methods");
                junit.textui.TestRunner.run(EmailMessageHandlerTest.class);
            } else {
                System.out.println("testing single method");
                TestSuite suite = new TestSuite();
                suite.addTest(new EmailMessageHandlerTest(x_method));
                junit.textui.TestRunner.run(suite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
