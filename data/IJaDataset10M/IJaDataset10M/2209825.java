package lcd_api.messages.outbound;

import junit.framework.TestCase;
import lcd_api.infrastructure.exceptions.IllegalAttributeException;
import lcd_api.infrastructure.exceptions.MessageIncompleteException;
import lcd_api.messages.outbound.OutboundMessage;
import lcd_api.messages.outbound.OutboundMessageType;
import lcd_api.messages.outbound.ScreenAdd;
import org.junit.After;
import org.junit.Before;

/**
 * A test of the ScreenAdd OutboundMessage. This message contains one single
 * attribute that is required.
 * 
 * @author Robert Derelanko
 */
public class ScreenAddTest extends TestCase {

    /**
	 * Each of these controls whether the associated test is executed,
	 * otherwise the test is skipped and marked as passed.
	 */
    private static final boolean CONSTRUCTOR = true;

    private static final boolean GET_MESSAGE_TYPE = true;

    private static final boolean GET_MESSAGE_EMPTY = true;

    private static final boolean SET_SCREEN_ID_GOOD = true;

    private static final boolean SET_SCREEN_ID_BAD = true;

    /**
	 * General Constants.
	 */
    private static final String PASSED = "[PASSED]";

    private static final String SKIPPED = "[SKIPPED]";

    /**
	 * Actions to perform prior to executing a test case.
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        System.out.println("##############################################");
    }

    /**
	 * Actions to perform after executing a test case.
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
        System.out.println("##############################################\n");
    }

    /**
	 * The default constructor.
	 */
    public ScreenAddTest() {
    }

    /**
	 * A test of the message constructor.
	 */
    public void test_Constructor() {
        System.out.println("TEST CASE: constructor");
        if (!CONSTRUCTOR) {
            System.out.println(SKIPPED);
            return;
        }
        OutboundMessage theMessage = null;
        try {
            theMessage = new ScreenAdd();
        } catch (Exception anException) {
            anException.printStackTrace();
        }
        assertNotNull(theMessage);
        System.out.println(PASSED);
    }

    /**
	 * A test of the getMessageType method.
	 * 
	 * The message type is hard-coded in the constructor of each subtype
	 * and should always be set correctly. This test verifies that assumption.
	 */
    public void test_getMessageType() {
        System.out.println("TEST CASE: getMessageType.");
        if (!GET_MESSAGE_TYPE) {
            System.out.println(SKIPPED);
            return;
        }
        OutboundMessage theMessage = new ScreenAdd();
        OutboundMessageType theType = theMessage.getMessageType();
        assertNotNull(theType);
        assertEquals(OutboundMessageType.SCREEN_ADD, theType);
        System.out.println("Type: " + theType.toString());
        System.out.println("Tag:  " + theType.getProtocolTag());
        assertEquals("screen_add", theType.getProtocolTag());
        System.out.println(PASSED);
    }

    /**
	 * A test of the getMessage method when no attributes were set.
	 * 
	 * Expected Output:
	 * An exception gets thrown when attemtping to call the getMessage  method
	 * as the message is incomplete.
	 */
    public void test_getMessage_Empty() {
        System.out.println("TEST CASE: getMessage: Emtpy Message.");
        if (!GET_MESSAGE_EMPTY) {
            System.out.println(SKIPPED);
            return;
        }
        ScreenAdd theMessage = new ScreenAdd();
        assertNotNull(theMessage);
        String theMessageString = null;
        boolean expectedExceptionCaught = false;
        try {
            theMessageString = theMessage.getMessage();
        } catch (MessageIncompleteException aGoodException) {
            expectedExceptionCaught = true;
            System.out.println("Expected Exception: \"" + aGoodException.getMessage() + "\"");
        } catch (Exception aBadException) {
            aBadException.printStackTrace();
            assertTrue(false);
        }
        assertTrue(expectedExceptionCaught);
        assertNull(theMessageString);
        System.out.println(PASSED);
    }

    /**
	 * A test of the setScreenID method with the following conditions:
	 * 1) The SCREEN_ID attribute is properly set.
	 * 
	 * Expected Output:
	 * The message generates it's string properly and passes all tests.
	 * 
	 * This test will also test the output of the getMessage method ensuring the
	 * requested attribute was properly set in the message.
	 */
    public void test_setScreenID_Good() {
        System.out.println("TEST CASE: setScreenID: Good SCREEN_ID.");
        if (!SET_SCREEN_ID_GOOD) {
            System.out.println(SKIPPED);
            return;
        }
        ScreenAdd theMessage = new ScreenAdd();
        assertNotNull(theMessage);
        try {
            theMessage.setScreenID("Screen_1");
        } catch (IllegalAttributeException aBadException) {
            aBadException.printStackTrace();
            assertTrue(false);
        }
        String messageString = null;
        try {
            messageString = theMessage.getMessage();
        } catch (MessageIncompleteException aBadException) {
            aBadException.printStackTrace();
            assertTrue(false);
        }
        assertNotNull(messageString);
        System.out.println("Generated Message: \"" + messageString + "\"");
        assertEquals("screen_add Screen_1", messageString);
        System.out.println(PASSED);
    }

    /**
	 * A test of the setScreenID method with the following conditions:
	 * 1) The SCREEN_ID attribute fails to set.
	 * 
	 * Expected Output:
	 * The message fails to generate it's string due to the missing required
	 * attribute.
	 * 
	 * This test will also test the output of the getMessage method ensuring the
	 * requested attribute was properly set in the message.
	 */
    public void test_setScreenID_Bad() {
        System.out.println("TEST CASE: setScreenID: Bad SCREEN_ID.");
        if (!SET_SCREEN_ID_BAD) {
            System.out.println(SKIPPED);
            return;
        }
        ScreenAdd theMessage = new ScreenAdd();
        assertNotNull(theMessage);
        boolean expectedExceptionCaught = false;
        try {
            theMessage.setScreenID("Bad Screen Spaces");
        } catch (IllegalAttributeException anExpectedException) {
            System.out.println("Expected Exception: \"" + anExpectedException.getMessage() + "\"");
            expectedExceptionCaught = true;
        } catch (Exception aBadException) {
            aBadException.printStackTrace();
            assertTrue(false);
        }
        assertTrue(expectedExceptionCaught);
        String messageString = null;
        try {
            messageString = theMessage.getMessage();
        } catch (MessageIncompleteException anExpectedException) {
            System.out.println("Expected Exception: \"" + anExpectedException.getMessage() + "\"");
            expectedExceptionCaught = true;
        }
        assertNull(messageString);
        assertTrue(expectedExceptionCaught);
        System.out.println(PASSED);
    }
}
