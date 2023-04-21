package org.mockftpserver.core.server;

import org.apache.commons.net.ftp.FTPClient;
import org.mockftpserver.test.*;
import org.mockftpserver.test.AbstractTestCase;

/**
 * Abstract superclass for tests of AbstractFtpServer subclasses that require the server thread to be started.
 *
 * @author Chris Mair
 * @version $Revision: 242 $ - $Date: 2010-03-21 07:41:01 -0400 (Sun, 21 Mar 2010) $
 */
public abstract class AbstractFtpServer_StartTestCase extends AbstractTestCase {

    private static final String SERVER = "localhost";

    private AbstractFtpServer ftpServer;

    /**
     * Test the start() and stop() methods. Start the server and then stop it immediately.
     */
    public void testStartAndStop() throws Exception {
        ftpServer.setServerControlPort(PortTestUtil.getFtpServerControlPort());
        assertEquals("started - before", false, ftpServer.isStarted());
        ftpServer.start();
        Thread.sleep(200L);
        assertEquals("started - after start()", true, ftpServer.isStarted());
        assertEquals("shutdown - after start()", false, ftpServer.isShutdown());
        ftpServer.stop();
        assertEquals("shutdown - after stop()", true, ftpServer.isShutdown());
    }

    /**
     * Test setting a non-default port number for the StubFtpServer control connection socket.
     */
    public void testCustomServerControlPort() throws Exception {
        final int SERVER_CONTROL_PORT = 9187;
        ftpServer.setServerControlPort(SERVER_CONTROL_PORT);
        ftpServer.start();
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(SERVER, SERVER_CONTROL_PORT);
        } finally {
            ftpServer.stop();
        }
    }

    /**
     * @see org.mockftpserver.test.AbstractTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ftpServer = createFtpServer();
    }

    protected abstract AbstractFtpServer createFtpServer();
}
