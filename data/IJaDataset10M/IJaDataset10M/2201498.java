package org.apache.james.mime4j.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.james.mime4j.ExampleMail;
import junit.framework.TestCase;

public class MessageWriteToTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSimpleMail() throws Exception {
        Message message = createMessage(ExampleMail.RFC822_SIMPLE_BYTES);
        assertFalse("Not multipart", message.isMultipart());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        message.writeTo(out);
        assertEquals(out.toByteArray(), ExampleMail.RFC822_SIMPLE_BYTES);
    }

    private void assertEquals(byte[] expected, byte[] actual) {
        StringBuilder buffer = new StringBuilder(expected.length);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            buffer.append((char) actual[i]);
            assertEquals("Mismatch@" + i, expected[i], actual[i]);
        }
    }

    public void testBinaryAttachment() throws Exception {
        Message message = createMessage(ExampleMail.MULTIPART_WITH_BINARY_ATTACHMENTS_BYTES);
        assertTrue("Is multipart", message.isMultipart());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        message.writeTo(out);
        assertEquals(ExampleMail.MULTIPART_WITH_BINARY_ATTACHMENTS_BYTES, out.toByteArray());
    }

    private Message createMessage(byte[] octets) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(octets);
        Message message = new Message(in);
        return message;
    }
}
