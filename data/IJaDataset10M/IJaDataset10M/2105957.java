package org.mockftpserver.stub.example;

import org.mockftpserver.core.command.CommandNames;
import org.mockftpserver.stub.StubFtpServer;
import org.mockftpserver.stub.command.PwdCommandHandler;
import org.mockftpserver.test.AbstractTest;
import org.mockftpserver.test.IntegrationTest;

/**
 * Example test using StubFtpServer, with programmatic configuration.
 */
public class FtpWorkingDirectoryTest extends AbstractTest implements IntegrationTest {

    private static final int PORT = 9981;

    private FtpWorkingDirectory ftpWorkingDirectory;

    private StubFtpServer stubFtpServer;

    /**
     * Test FtpWorkingDirectory getWorkingDirectory() method 
     */
    public void testGetWorkingDirectory() throws Exception {
        final String DIR = "some/dir";
        PwdCommandHandler pwdCommandHandler = new PwdCommandHandler();
        pwdCommandHandler.setDirectory(DIR);
        stubFtpServer.setCommandHandler(CommandNames.PWD, pwdCommandHandler);
        stubFtpServer.start();
        String workingDir = ftpWorkingDirectory.getWorkingDirectory();
        assertEquals("workingDirectory", DIR, workingDir);
    }

    /**
     * @see org.mockftpserver.test.AbstractTest#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ftpWorkingDirectory = new FtpWorkingDirectory();
        ftpWorkingDirectory.setPort(PORT);
        stubFtpServer = new StubFtpServer();
        stubFtpServer.setServerControlPort(PORT);
    }

    /**
     * @see org.mockftpserver.test.AbstractTest#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        stubFtpServer.stop();
    }
}
