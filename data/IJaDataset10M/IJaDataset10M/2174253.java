package org.echarts.servlet.sip.features.sipRoutingApp.test;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;
import org.echarts.test.sip.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import static org.echarts.test.sip.CATMatchers.*;
import static org.hamcrest.Matchers.*;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

public final class SipRoutingAppTest extends org.echarts.test.sip.CATTestCase {

    /** Version control identifier strings. */
    public static final String[] RCS_ID = { "$URL$", "$Id$" };

    String appServer;

    String httpServer;

    String appName;

    int sipListenPort;

    Logger logger;

    int remoteListenPort;

    String outputDir;

    public SipRoutingAppTest() throws Exception {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("test.properties"));
        } catch (Exception e) {
            System.out.println("error loading test configuration file");
            e.printStackTrace();
            System.exit(1);
        }
        appServer = props.getProperty("SipAS");
        httpServer = props.getProperty("HttpAS");
        sipListenPort = Integer.parseInt(props.getProperty("SipStackListenPort"));
        appName = props.getProperty("AppName", "sipRoutingAppTest");
        outputDir = props.getProperty("OutputDir", "out");
        logger = Logger.getLogger(this.getClass().getPackage().getName());
    }

    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }

    @AfterClass
    public static void runOnceAfterAllTests() {
    }

    @Before
    public void runBeforeEachTest() {
        try {
            logger.info("running test setup");
        } catch (Exception e) {
            logger.error("test setup failed", e);
        }
    }

    @After
    public void runAfterEachTest() {
        try {
            logger.info("running test cleanup");
            this.release();
        } catch (Exception e) {
            logger.error("test cleanup failed", e);
        }
        logger.info("====================");
    }

    /** A sample do nothing test.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testDoNothing() {
        try {
            this.init("testDoNothing", outputDir, this.sipListenPort);
        } catch (Exception e) {
            logger.error("testing failed ", e);
            fail(e.toString());
        }
    }
}
