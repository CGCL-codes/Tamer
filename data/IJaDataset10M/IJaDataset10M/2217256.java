package org.opennms.protocols.nsclient.detector;

import java.io.IOException;
import java.io.OutputStream;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.provision.server.SimpleServer;
import org.opennms.netmgt.provision.server.exchange.RequestHandler;
import org.opennms.netmgt.provision.support.NullDetectorMonitor;
import org.opennms.protocols.nsclient.detector.NsclientDetector;
import org.opennms.test.mock.MockLogAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>JUnit Test Class for NsclientDetector.</p>
 *
 * @author Alejandro Galue <agalue@opennms.org>
 * @version $Id: $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/opennms/detectors.xml" })
public class NsclientDetectorTest {

    @Autowired
    private NsclientDetector m_detector;

    private SimpleServer m_server = null;

    @After
    public void tearDown() throws Exception {
        if (m_server != null) {
            m_server.stopServer();
            m_server = null;
        }
    }

    @Before
    public void setUp() throws Exception {
        MockLogAppender.setupLogging();
        m_server = new SimpleServer() {

            public void onInit() {
                addResponseHandler(startsWith("None&1"), new RequestHandler() {

                    @Override
                    public void doRequest(OutputStream out) throws IOException {
                        out.write(String.format("%s\r\n", "NSClient++ 0.3.8.75 2010-05-27").getBytes());
                    }
                });
            }
        };
        m_server.init();
        m_server.startServer();
        Thread.sleep(100);
        m_detector.setServiceName("NSclient++");
        m_detector.setPort(m_server.getLocalPort());
        m_detector.setTimeout(2000);
        m_detector.setRetries(3);
    }

    @Test
    public void testServerSuccess() throws Exception {
        m_detector.setCommand("CLIENTVERSION");
        m_detector.init();
        Assert.assertTrue(m_detector.isServiceDetected(m_server.getInetAddress(), new NullDetectorMonitor()));
    }

    @Test
    public void testBadCommand() throws Exception {
        m_detector.setCommand("UNKNOWN");
        m_detector.init();
        Assert.assertFalse(m_detector.isServiceDetected(m_server.getInetAddress(), new NullDetectorMonitor()));
    }

    @Test
    public void testNoCommand() throws Exception {
        m_detector.init();
        Assert.assertTrue(m_detector.isServiceDetected(m_server.getInetAddress(), new NullDetectorMonitor()));
    }
}
