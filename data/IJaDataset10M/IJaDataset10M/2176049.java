package org.mockftpserver.stub.example;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.mockftpserver.stub.StubFtpServer;
import org.mockftpserver.test.AbstractTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.io.ByteArrayOutputStream;

/**
 * Example test for StubFtpServer, using the Spring Framework ({@link http://www.springframework.org/}) 
 * for configuration.
 */
public class SpringConfigurationTest extends AbstractTest {

    private static final Logger LOG = Logger.getLogger(SpringConfigurationTest.class);

    private static final String SERVER = "localhost";

    private static final int PORT = 9981;

    private StubFtpServer stubFtpServer;

    private FTPClient ftpClient;

    /**
     * Test starting the StubFtpServer configured within the example Spring configuration file 
     */
    public void testStubFtpServer() throws Exception {
        stubFtpServer.start();
        ftpClient.connect(SERVER, PORT);
        String dir = ftpClient.printWorkingDirectory();
        assertEquals("PWD", "foo/bar", dir);
        FTPFile[] files = ftpClient.listFiles();
        LOG.info("FTPFile[0]=" + files[0]);
        LOG.info("FTPFile[1]=" + files[1]);
        assertEquals("number of files from LIST", 2, files.length);
        assertFalse("DELE", ftpClient.deleteFile("AnyFile.txt"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertTrue(ftpClient.retrieveFile("SomeFile.txt", outputStream));
        LOG.info("File contents=[" + outputStream.toString() + "]");
    }

    /**
     * @see org.mockftpserver.test.AbstractTest#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("stubftpserver-beans.xml");
        stubFtpServer = (StubFtpServer) context.getBean("stubFtpServer");
        ftpClient = new FTPClient();
    }

    /**
     * @see org.mockftpserver.test.AbstractTest#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        stubFtpServer.stop();
    }
}
