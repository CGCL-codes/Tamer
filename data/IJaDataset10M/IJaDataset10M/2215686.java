package org.apache.http.impl.entity;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.http.HttpMessage;
import org.apache.http.ProtocolException;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.mockup.HttpMessageMockup;
import org.apache.http.params.CoreProtocolPNames;

public class TestLaxContentLengthStrategy extends TestCase {

    public TestLaxContentLengthStrategy(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestLaxContentLengthStrategy.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestLaxContentLengthStrategy.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public void testEntityWithTransferEncoding() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "identity, chunked");
        message.addHeader("Content-Length", "plain wrong");
        assertEquals(ContentLengthStrategy.CHUNKED, lenStrategy.determineLength(message));
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        assertEquals(ContentLengthStrategy.CHUNKED, lenStrategy.determineLength(message));
    }

    public void testEntityWithIdentityTransferEncoding() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "identity");
        message.addHeader("Content-Length", "plain wrong");
        assertEquals(ContentLengthStrategy.IDENTITY, lenStrategy.determineLength(message));
    }

    public void testEntityWithUnsupportedTransferEncoding() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "whatever; param=value, chunked");
        message.addHeader("Content-Length", "plain wrong");
        assertEquals(ContentLengthStrategy.CHUNKED, lenStrategy.determineLength(message));
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            lenStrategy.determineLength(message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testChunkedTransferEncodingMustBeLast() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "chunked, identity");
        message.addHeader("Content-Length", "plain wrong");
        assertEquals(ContentLengthStrategy.IDENTITY, lenStrategy.determineLength(message));
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            lenStrategy.determineLength(message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithContentLength() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "0");
        assertEquals(0, lenStrategy.determineLength(message));
    }

    public void testEntityWithMultipleContentLength() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "0");
        message.addHeader("Content-Length", "0");
        message.addHeader("Content-Length", "1");
        assertEquals(1, lenStrategy.determineLength(message));
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            lenStrategy.determineLength(message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithMultipleContentLengthSomeWrong() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "1");
        message.addHeader("Content-Length", "yyy");
        message.addHeader("Content-Length", "xxx");
        assertEquals(1, lenStrategy.determineLength(message));
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            lenStrategy.determineLength(message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithMultipleContentLengthAllWrong() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "yyy");
        message.addHeader("Content-Length", "xxx");
        assertEquals(ContentLengthStrategy.IDENTITY, lenStrategy.determineLength(message));
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            lenStrategy.determineLength(message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithInvalidContentLength() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "xxx");
        assertEquals(ContentLengthStrategy.IDENTITY, lenStrategy.determineLength(message));
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            lenStrategy.determineLength(message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityNeitherContentLengthNorTransferEncoding() throws Exception {
        ContentLengthStrategy lenStrategy = new LaxContentLengthStrategy();
        HttpMessage message = new HttpMessageMockup();
        assertEquals(ContentLengthStrategy.IDENTITY, lenStrategy.determineLength(message));
    }
}
