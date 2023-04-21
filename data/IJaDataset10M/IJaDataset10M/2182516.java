package org.apache.http.impl.entity;

import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.ProtocolException;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.mockup.SessionInputBufferMockup;
import org.apache.http.mockup.HttpMessageMockup;
import org.apache.http.params.CoreProtocolPNames;

public class TestEntityDeserializer extends TestCase {

    public TestEntityDeserializer(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestEntityDeserializer.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestEntityDeserializer.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public void testIllegalGenerateArg() throws Exception {
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        try {
            entitygen.deserialize(null, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException ex) {
        }
        try {
            entitygen.deserialize(new SessionInputBufferMockup(new byte[] {}), null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testEntityWithTransferEncoding() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup("0\r\n", "US-ASCII");
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "identity, chunked");
        message.addHeader("Content-Length", "plain wrong");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertTrue(entity.isChunked());
        assertTrue(entity.getContent() instanceof ChunkedInputStream);
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertTrue(entity.isChunked());
        assertTrue(entity.getContent() instanceof ChunkedInputStream);
    }

    public void testEntityWithIdentityTransferEncoding() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] {});
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "identity");
        message.addHeader("Content-Length", "plain wrong");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertFalse(entity.isChunked());
    }

    public void testEntityWithUnsupportedTransferEncoding() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup("0\r\n", "US-ASCII");
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "whatever; param=value, chunked");
        message.addHeader("Content-Length", "plain wrong");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertTrue(entity.isChunked());
        assertTrue(entity.getContent() instanceof ChunkedInputStream);
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            entitygen.deserialize(datareceiver, message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testChunkedTransferEncodingMustBeLast() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup("0\r\n", "US-ASCII");
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Transfer-Encoding", "chunked, identity");
        message.addHeader("Content-Length", "plain wrong");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertFalse(entity.isChunked());
        assertFalse(entity.getContent() instanceof ChunkedInputStream);
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            entitygen.deserialize(datareceiver, message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithContentLength() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] {});
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "0");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(0, entity.getContentLength());
        assertFalse(entity.isChunked());
        assertTrue(entity.getContent() instanceof ContentLengthInputStream);
    }

    public void testEntityWithMultipleContentLength() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] { '0' });
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "0");
        message.addHeader("Content-Length", "0");
        message.addHeader("Content-Length", "1");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(1, entity.getContentLength());
        assertFalse(entity.isChunked());
        InputStream instream = entity.getContent();
        assertNotNull(instream);
        assertTrue(instream instanceof ContentLengthInputStream);
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            entitygen.deserialize(datareceiver, message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithMultipleContentLengthSomeWrong() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] { '0' });
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "1");
        message.addHeader("Content-Length", "yyy");
        message.addHeader("Content-Length", "xxx");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(1, entity.getContentLength());
        assertFalse(entity.isChunked());
        InputStream instream = entity.getContent();
        assertNotNull(instream);
        assertTrue(instream instanceof ContentLengthInputStream);
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            entitygen.deserialize(datareceiver, message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithMultipleContentLengthAllWrong() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] { '0' });
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "yyy");
        message.addHeader("Content-Length", "xxx");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertFalse(entity.isChunked());
        InputStream instream = entity.getContent();
        assertNotNull(instream);
        assertFalse(instream instanceof ContentLengthInputStream);
        assertTrue(instream instanceof IdentityInputStream);
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            entitygen.deserialize(datareceiver, message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityWithInvalidContentLength() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] { '0' });
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        message.addHeader("Content-Type", "unknown");
        message.addHeader("Content-Length", "xxx");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertFalse(entity.isChunked());
        InputStream instream = entity.getContent();
        assertNotNull(instream);
        assertFalse(instream instanceof ContentLengthInputStream);
        assertTrue(instream instanceof IdentityInputStream);
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, true);
        try {
            entitygen.deserialize(datareceiver, message);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
        }
    }

    public void testEntityNeitherContentLengthNorTransferEncoding() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] { '0' });
        HttpMessage message = new HttpMessageMockup();
        message.getParams().setBooleanParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertEquals(-1, entity.getContentLength());
        assertFalse(entity.isChunked());
        InputStream instream = entity.getContent();
        assertNotNull(instream);
        assertFalse(instream instanceof ContentLengthInputStream);
        assertFalse(instream instanceof ChunkedInputStream);
        assertTrue(instream instanceof IdentityInputStream);
    }

    public void testEntityContentType() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] { '0' });
        HttpMessage message = new HttpMessageMockup();
        message.addHeader("Content-Type", "stuff");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertNotNull(entity.getContentType());
        assertEquals("stuff", entity.getContentType().getValue());
    }

    public void testEntityContentEncoding() throws Exception {
        SessionInputBuffer datareceiver = new SessionInputBufferMockup(new byte[] { '0' });
        HttpMessage message = new HttpMessageMockup();
        message.addHeader("Content-Encoding", "what not");
        EntityDeserializer entitygen = new EntityDeserializer(new LaxContentLengthStrategy());
        HttpEntity entity = entitygen.deserialize(datareceiver, message);
        assertNotNull(entity);
        assertNotNull(entity.getContentEncoding());
        assertEquals("what not", entity.getContentEncoding().getValue());
    }
}
