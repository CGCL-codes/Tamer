package org.apache.axis2.handlers.addressing;

import org.apache.axiom.soap.RolePlayer;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;

public class AddressingSubmissionInHandlerTest extends AddressingInHandlerTestBase {

    private static Log log = LogFactory.getLog(AddressingSubmissionInHandlerTest.class);

    /** @param testName  */
    public AddressingSubmissionInHandlerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        inHandler = new AddressingInHandler();
        addressingNamespace = AddressingConstants.Submission.WSA_NAMESPACE;
        versionDirectory = "submission";
        fromAddress = "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";
        secondRelationshipType = "axis2:some.custom.relationship";
    }

    public void testExtractAddressingInformationFromHeaders() throws Exception {
        Options options = extractAddressingInformationFromHeaders(null);
        assertNotNull(options);
        assertNotNull(options.getTo());
    }

    public void testExtractAddressingInformationFromHeadersCustomRole() throws Exception {
        testFileName = "soapmessage.customrole.xml";
        extractAddressingInformationFromHeaders(new RolePlayer() {

            public List getRoles() {
                ArrayList al = new ArrayList();
                al.add("http://my/custom/role");
                return al;
            }

            public boolean isUltimateDestination() {
                return false;
            }
        });
    }

    public void testMessageWithOmittedAction() {
        try {
            testMessageWithOmittedHeaders("noAction");
            fail("An AxisFault should have been thrown due to a missing Action header.");
        } catch (AxisFault af) {
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            fail(" An Exception has occured " + e.getMessage());
        }
    }

    public void testMessageWithOmittedFaultTo() {
        try {
            Options options = testMessageWithOmittedHeaders("noFaultTo");
            EndpointReference epr = options.getFaultTo();
            assertNull("The FaultTo endpoint reference is not null.", epr);
        } catch (AxisFault af) {
            af.printStackTrace();
            log.error(af.getMessage());
            fail("An unexpected AxisFault was thrown due to a missing FaultTo header.");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            fail(" An Exception has occured " + e.getMessage());
        }
    }

    public void testMessageWithOmittedFrom() {
        try {
            Options options = testMessageWithOmittedHeaders("noFrom");
            EndpointReference epr = options.getFrom();
            assertNull("The From endpoint reference is not null.", epr);
        } catch (AxisFault af) {
            af.printStackTrace();
            log.error(af.getMessage());
            fail("An unexpected AxisFault was thrown due to a missing From header.");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            fail(" An Exception has occured " + e.getMessage());
        }
    }

    public void testMessageWithOmittedMessageID() {
        try {
            testMessageWithOmittedHeaders("noMessageID");
            fail("An AxisFault should have been thrown due to a missing MessageID header.");
        } catch (AxisFault af) {
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            fail(" An Exception has occured " + e.getMessage());
        }
    }

    public void testMessageWithOmittedMessageIDReplyToAndFaultTo() {
        try {
            Options options = testMessageWithOmittedHeaders("noMessageIDNoReplyToNoFaultTo");
            String messageID = options.getMessageId();
            assertNull("The message id is not null.", messageID);
        } catch (AxisFault af) {
            af.printStackTrace();
            log.error(af.getMessage());
            fail("An unexpected AxisFault was thrown due to missing MessageID, ReplyTo, and FaultTo headers.");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            fail(" An Exception has occured " + e.getMessage());
        }
    }

    public void testMessageWithOmittedReplyTo() {
        try {
            Options options = testMessageWithOmittedHeaders("noReplyTo");
            EndpointReference epr = options.getReplyTo();
            String address = epr.getAddress();
            assertEquals("The address of the ReplyTo endpoint reference is not the none URI.", AddressingConstants.Final.WSA_NONE_URI, address);
        } catch (AxisFault af) {
            af.printStackTrace();
            log.error(af.getMessage());
            fail("An unexpected AxisFault was thrown due to a missing ReplyTo header.");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            fail(" An Exception has occured " + e.getMessage());
        }
    }

    public void testMessageWithOmittedTo() {
        try {
            testMessageWithOmittedHeaders("noTo");
            fail("An AxisFault should have been thrown due to a missing To header.");
        } catch (AxisFault af) {
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            fail(" An Exception has occured " + e.getMessage());
        }
    }
}
