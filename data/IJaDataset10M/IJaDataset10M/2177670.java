package net.sf.mavensynapseplugin;

import java.io.IOException;
import net.sf.mavensynapseplugin.test.HttpInvoker;
import net.sf.mavensynapseplugin.test.RemoteServerSimulator;
import net.sf.mavensynapseplugin.test.RemoteServerSimulatorEventListener;
import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.xml.sax.SAXException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SynapseConfigIntegrationTest {

    private HttpInvoker httpInvoker = new HttpInvoker();

    @Autowired
    private RemoteServerSimulator remoteServerSimulator;

    @Test
    public void canTestSimpleProxyThatReturnsCannedResult() throws SAXException, IOException {
        String result = httpInvoker.executeWebServicePost("http://localhost:8280/soap/CannedMessageProxy", fromClasspath("SCIT/simple-request.xml"));
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(result, fromClasspath("SCIT/expected-canned-response.xml"), result);
    }

    @Test
    public void canTestMediatorsWithJmsOutputFromSynapse() throws IOException, SAXException {
        String expectedRequest = fromClasspath("SCIT/expected-request-to-remote-server.xml");
        String replyWith = fromClasspath("SCIT/reply-from-remote-server.xml");
        remoteServerSimulator.setRemoteServerSimulatorEventListener(new XmlCheckingRemoteServerSimulatorEventListener(expectedRequest, replyWith));
        String result = httpInvoker.executeWebServicePost("http://localhost:8280/soap/JMSProxy", fromClasspath("SCIT/simple-request.xml"));
        remoteServerSimulator.verify();
        XMLAssert.assertXMLEqual(result, fromClasspath("SCIT/expected-mediated-response.xml"), result);
    }

    private String fromClasspath(String resource) {
        try {
            return IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class XmlCheckingRemoteServerSimulatorEventListener implements RemoteServerSimulatorEventListener {

        private final String expectedRequest;

        private final String respondWith;

        private XmlCheckingRemoteServerSimulatorEventListener(String expectedRequest, String respondWith) {
            this.expectedRequest = expectedRequest;
            this.respondWith = respondWith;
        }

        public String onMessageReceived(String requestMessage) {
            try {
                XMLAssert.assertXMLEqual(expectedRequest, requestMessage);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return respondWith;
        }
    }
}
