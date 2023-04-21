package org.apache.commons.net.pop3;

import junit.framework.TestCase;
import java.net.InetAddress;
import java.io.IOException;
import java.io.Reader;

/**
 * @author <a href="mailto:commons-dev@apache.org">[Net]</a>
 * @version $Id: POP3ClientCommandsTest.java 936824 2010-04-22 13:10:45Z sebb $
 *
 * The POP3* tests all presume the existence of the following parameters:
 *   mailserver: localhost (running on the default port 110)
 *   account: username=test; password=password
 *   account: username=alwaysempty; password=password.
 *   mail: At least four emails in the test account and zero emails
 *         in the alwaysempty account
 *
 * If this won't work for you, you can change these parameters in the
 * TestSetupParameters class.
 *
 * The tests were originally run on a default installation of James.
 * Your mileage may vary based on the POP3 server you run the tests against.
 * Some servers are more standards-compliant than others.
 */
public class POP3ClientCommandsTest extends TestCase {

    POP3Client p = null;

    String user = TestSetupParameters.user;

    String emptyUser = TestSetupParameters.emptyuser;

    String password = TestSetupParameters.password;

    String mailhost = TestSetupParameters.mailhost;

    /**
     *
     */
    public POP3ClientCommandsTest(String name) {
        super(name);
    }

    private void reset() throws IOException {
        if (p == null) {
        } else if (p.isConnected()) {
            p.disconnect();
        }
        p = null;
        p = new POP3Client();
    }

    private void connect() throws Exception {
        p.connect(InetAddress.getByName(mailhost));
        assertTrue(p.isConnected());
        assertEquals(POP3.AUTHORIZATION_STATE, p.getState());
    }

    private void login() throws Exception {
        assertTrue(p.login(user, password));
        assertEquals(POP3.TRANSACTION_STATE, p.getState());
    }

    /**
     *
     *
     */
    public void testNoopCommand() throws Exception {
        reset();
        connect();
        assertFalse(p.noop());
        login();
        assertTrue(p.noop());
        p.setState(POP3.UPDATE_STATE);
        assertFalse(p.noop());
    }

    /**
     *
     *
     */
    public void testStatus() throws Exception {
        reset();
        connect();
        assertNull(p.status());
        login();
        POP3MessageInfo msg = p.status();
        assertTrue(msg.number > 0);
        assertTrue(msg.size > 0);
        assertNull(msg.identifier);
        p.logout();
        reset();
        connect();
        assertTrue(p.login(emptyUser, password));
        POP3MessageInfo msg2 = p.status();
        assertEquals(0, msg2.number);
        assertEquals(0, msg2.size);
        assertNull(msg2.identifier);
        p.logout();
        reset();
        connect();
        login();
        p.setState(POP3.UPDATE_STATE);
        assertNull(p.status());
    }

    /**
     *
     *
     */
    public void testListMessagesOnFullMailbox() throws Exception {
        reset();
        connect();
        login();
        POP3MessageInfo[] msg = p.listMessages();
        assertTrue(msg.length > 0);
        for (int i = 0; i < msg.length; i++) {
            assertNotNull(msg[i]);
            assertEquals(i + 1, msg[i].number);
            assertTrue(msg[i].size > 0);
            assertNull(msg[i].identifier);
        }
        p.setState(POP3.UPDATE_STATE);
        msg = p.listMessages();
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testListMessageOnFullMailbox() throws Exception {
        reset();
        connect();
        login();
        POP3MessageInfo msg = p.listMessage(1);
        assertNotNull(msg);
        assertEquals(1, msg.number);
        assertTrue(msg.size > 0);
        assertNull(msg.identifier);
        msg = p.listMessage(0);
        assertNull(msg);
        msg = p.listMessage(100000);
        assertNull(msg);
        msg = p.listMessage(-2);
        assertNull(msg);
        p.setState(POP3.UPDATE_STATE);
        msg = p.listMessage(1);
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testListMessagesOnEmptyMailbox() throws Exception {
        reset();
        connect();
        assertTrue(p.login(emptyUser, password));
        POP3MessageInfo[] msg = p.listMessages();
        assertEquals(0, msg.length);
        p.setState(POP3.UPDATE_STATE);
        msg = p.listMessages();
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testListMessageOnEmptyMailbox() throws Exception {
        reset();
        connect();
        assertTrue(p.login(emptyUser, password));
        POP3MessageInfo msg = p.listMessage(1);
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testListUniqueIDsOnFullMailbox() throws Exception {
        reset();
        connect();
        login();
        POP3MessageInfo[] msg = p.listUniqueIdentifiers();
        assertTrue(msg.length > 0);
        for (int i = 0; i < msg.length; i++) {
            assertNotNull(msg[i]);
            assertEquals(i + 1, msg[i].number);
            assertNotNull(msg[i].identifier);
        }
        p.setState(POP3.UPDATE_STATE);
        msg = p.listUniqueIdentifiers();
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testListUniqueIDOnFullMailbox() throws Exception {
        reset();
        connect();
        login();
        POP3MessageInfo msg = p.listUniqueIdentifier(1);
        assertNotNull(msg);
        assertEquals(1, msg.number);
        assertNotNull(msg.identifier);
        msg = p.listUniqueIdentifier(0);
        assertNull(msg);
        msg = p.listUniqueIdentifier(100000);
        assertNull(msg);
        msg = p.listUniqueIdentifier(-2);
        assertNull(msg);
        p.setState(POP3.UPDATE_STATE);
        msg = p.listUniqueIdentifier(1);
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testListUniqueIDsOnEmptyMailbox() throws Exception {
        reset();
        connect();
        assertTrue(p.login(emptyUser, password));
        POP3MessageInfo[] msg = p.listUniqueIdentifiers();
        assertEquals(0, msg.length);
        p.setState(POP3.UPDATE_STATE);
        msg = p.listUniqueIdentifiers();
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testListUniqueIdentifierOnEmptyMailbox() throws Exception {
        reset();
        connect();
        assertTrue(p.login(emptyUser, password));
        POP3MessageInfo msg = p.listUniqueIdentifier(1);
        assertNull(msg);
    }

    /**
     *
     *
     */
    public void testRetrieveMessageOnFullMailbox() throws Exception {
        reset();
        connect();
        login();
        int reportedSize = 0;
        int actualSize = 0;
        POP3MessageInfo[] msg = p.listMessages();
        assertTrue(msg.length > 0);
        for (int i = msg.length; i > 0; i--) {
            reportedSize = msg[i - 1].size;
            Reader r = p.retrieveMessage(i);
            assertNotNull(r);
            int delaycount = 0;
            if (!r.ready()) {
                Thread.sleep(500);
                delaycount++;
                if (delaycount == 4) {
                    break;
                }
            }
            while (r.ready()) {
                r.read();
                actualSize++;
            }
            assertTrue(actualSize >= reportedSize);
        }
    }

    /**
     *
     *
     */
    public void testRetrieveMessageOnEmptyMailbox() throws Exception {
        reset();
        connect();
        assertTrue(p.login(emptyUser, password));
        assertNull(p.retrieveMessage(1));
    }

    /**
     *
     *
     */
    public void testRetrieveMessageShouldFails() throws Exception {
        reset();
        connect();
        login();
        assertNull(p.retrieveMessage(0));
        assertNull(p.retrieveMessage(-2));
        assertNull(p.retrieveMessage(100000));
        p.setState(POP3.UPDATE_STATE);
        assertNull(p.retrieveMessage(1));
    }

    /**
     *
     *
     */
    public void testRetrieveMessageTopOnFullMailbox() throws Exception {
        reset();
        connect();
        login();
        int numLines = 10;
        POP3MessageInfo[] msg = p.listMessages();
        assertTrue(msg.length > 0);
        for (int i = 0; i < msg.length; i++) {
            Reader r = p.retrieveMessageTop(i + 1, numLines);
            assertNotNull(r);
            r.close();
            r = null;
        }
    }

    /**
     *
     *
     */
    public void testRetrieveOverSizedMessageTopOnFullMailbox() throws Exception {
        reset();
        connect();
        login();
        int reportedSize = 0;
        int actualSize = 0;
        POP3MessageInfo msg = p.listMessage(1);
        reportedSize = msg.size;
        Reader r = p.retrieveMessageTop(1, 100000);
        assertNotNull(r);
        int delaycount = 0;
        while (!r.ready()) {
            Thread.sleep(500);
            delaycount++;
            if (delaycount == 4) {
                break;
            }
        }
        while (r.ready()) {
            r.read();
            actualSize++;
        }
        assertTrue(actualSize >= reportedSize);
    }

    /**
     *
     *
     */
    public void testRetrieveMessageTopOnEmptyMailbox() throws Exception {
        reset();
        connect();
        assertTrue(p.login(emptyUser, password));
        assertNull(p.retrieveMessageTop(1, 10));
    }

    /**
     *
     *
     */
    public void testRetrieveMessageTopShouldFails() throws Exception {
        reset();
        connect();
        login();
        assertNull(p.retrieveMessageTop(0, 10));
        assertNull(p.retrieveMessageTop(-2, 10));
        assertNull(p.retrieveMessageTop(100000, 10));
        p.setState(POP3.UPDATE_STATE);
        assertNull(p.retrieveMessageTop(1, 10));
    }

    public void testDeleteWithReset() throws Exception {
        reset();
        connect();
        login();
        POP3MessageInfo[] msg = p.listMessages();
        int numMessages = msg.length;
        int numDeleted = 0;
        for (int i = 0; i < numMessages - 1; i++) {
            p.deleteMessage(i + 1);
            numDeleted++;
        }
        assertEquals(numMessages, (numDeleted + 1));
        p.reset();
        p.logout();
        reset();
        connect();
        login();
        msg = p.listMessages();
        assertEquals(numMessages, msg.length);
    }

    public void testDelete() throws Exception {
        reset();
        connect();
        login();
        POP3MessageInfo[] msg = p.listMessages();
        int numMessages = msg.length;
        int numDeleted = 0;
        for (int i = 0; i < numMessages - 3; i++) {
            p.deleteMessage(i + 1);
            numDeleted++;
        }
        assertEquals(numMessages, (numDeleted + 3));
        p.logout();
        reset();
        connect();
        login();
        msg = p.listMessages();
        assertEquals(numMessages - numDeleted, msg.length);
    }

    public void testResetAndDeleteShouldFails() throws Exception {
        reset();
        connect();
        login();
        p.setState(POP3.UPDATE_STATE);
        assertFalse(p.reset());
        assertFalse(p.deleteMessage(1));
    }
}
