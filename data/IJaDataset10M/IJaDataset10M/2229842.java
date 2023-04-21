package org.mockftpserver.stub;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandNames;
import org.mockftpserver.core.command.InvocationRecord;
import org.mockftpserver.core.session.Session;
import org.mockftpserver.stub.command.AbstractStubCommandHandler;
import org.mockftpserver.test.AbstractTest;
import org.mockftpserver.test.IntegrationTest;
import org.mockftpserver.test.PortTestUtil;

/**
 * StubFtpServer tests for multiple FTP clients using the Apache Jakarta Commons Net FTP client.
 * 
 * @version $Revision: 184 $ - $Date: 2008-12-03 17:52:39 -0500 (Wed, 03 Dec 2008) $
 * 
 * @author Chris Mair
 */
public final class StubFtpServer_MultipleClientsIntegrationTest extends AbstractTest implements IntegrationTest {

    private static final Logger LOG = Logger.getLogger(StubFtpServer_MultipleClientsIntegrationTest.class);

    private static final String SERVER = "localhost";

    private static class CustomPwdCommandHandler extends AbstractStubCommandHandler {

        protected void handleCommand(Command command, Session session, InvocationRecord invocationRecord) throws Exception {
            String replyText = quotes(Integer.toString(session.hashCode()));
            sendReply(session, 257, null, replyText, null);
        }
    }

    private StubFtpServer stubFtpServer;

    private FTPClient ftpClient1;

    private FTPClient ftpClient2;

    private FTPClient ftpClient3;

    /**
     * Test that multiple simultaneous clients can connect and establish sessions. 
     */
    public void testMultipleClients() throws Exception {
        LOG.info("connect() to ftpClient1");
        ftpClient1.connect(SERVER, PortTestUtil.getFtpServerControlPort());
        String sessionId1 = ftpClient1.printWorkingDirectory();
        LOG.info("PWD(1) reply =[" + sessionId1 + "]");
        LOG.info("connect() to ftpClient2");
        ftpClient2.connect(SERVER, PortTestUtil.getFtpServerControlPort());
        String sessionId2 = ftpClient2.printWorkingDirectory();
        LOG.info("PWD(2) reply =[" + sessionId2 + "]");
        LOG.info("connect() to ftpClient3");
        ftpClient3.connect(SERVER, PortTestUtil.getFtpServerControlPort());
        String sessionId3 = ftpClient3.printWorkingDirectory();
        LOG.info("PWD(3) reply =[" + sessionId3 + "]");
        assertNotSame("sessionId1 vs sessionId2", sessionId1, sessionId2);
        assertNotSame("sessionId2 vs sessionId3", sessionId2, sessionId3);
        assertNotSame("sessionId1 vs sessionId3", sessionId1, sessionId3);
        assertEquals("reply from session1", sessionId1, ftpClient1.printWorkingDirectory());
        assertEquals("reply from session2", sessionId2, ftpClient2.printWorkingDirectory());
        assertEquals("reply from session3", sessionId3, ftpClient3.printWorkingDirectory());
    }

    /**
     * Perform initialization before each test
     * @see org.mockftpserver.test.AbstractTest#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        stubFtpServer = new StubFtpServer();
        stubFtpServer.setServerControlPort(PortTestUtil.getFtpServerControlPort());
        stubFtpServer.setCommandHandler(CommandNames.PWD, new CustomPwdCommandHandler());
        stubFtpServer.start();
        ftpClient1 = new FTPClient();
        ftpClient2 = new FTPClient();
        ftpClient3 = new FTPClient();
        ftpClient1.setDefaultTimeout(1000);
        ftpClient2.setDefaultTimeout(1000);
        ftpClient3.setDefaultTimeout(1000);
    }

    /**
     * Perform cleanup after each test
     * @see org.mockftpserver.test.AbstractTest#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        LOG.info("Cleaning up...");
        if (ftpClient1.isConnected()) {
            ftpClient1.disconnect();
        }
        if (ftpClient2.isConnected()) {
            ftpClient2.disconnect();
        }
        if (ftpClient3.isConnected()) {
            ftpClient3.disconnect();
        }
        stubFtpServer.stop();
    }
}
