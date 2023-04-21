package org.mime4j.message;

import org.mime4j.field.Field;
import org.mime4j.message.Header;
import org.mime4j.message.Message;
import junit.framework.TestCase;

/**
 * 
 *
 * @author Niklas Therning
 * @version $Id: MessageTest.java,v 1.4 2004/10/02 12:41:11 ntherning Exp $
 */
public class MessageTest extends TestCase {

    private Header headerTextPlain = null;

    private Header headerMessageRFC822 = null;

    private Header headerEmpty = null;

    private Header headerMultipartMixed = null;

    private Header headerMultipartDigest = null;

    public void setUp() {
        headerTextPlain = new Header();
        headerMessageRFC822 = new Header();
        headerEmpty = new Header();
        headerMultipartMixed = new Header();
        headerMultipartDigest = new Header();
        headerTextPlain.addField(Field.parse("Content-Type: text/plain"));
        headerMessageRFC822.addField(Field.parse("Content-Type: message/RFC822"));
        headerMultipartMixed.addField(Field.parse("Content-Type: multipart/mixed; boundary=foo"));
        headerMultipartDigest.addField(Field.parse("Content-Type: multipart/digest; boundary=foo"));
    }

    public void testGetParts() {
    }

    public void testGetMimeType() {
        Message parent = null;
        Message child = null;
        parent = new Message();
        child = new Message();
        child.setParent(parent);
        parent.setHeader(headerMultipartDigest);
        child.setHeader(headerEmpty);
        assertEquals("multipart/digest, empty", "message/rfc822", child.getMimeType());
        child.setHeader(headerTextPlain);
        assertEquals("multipart/digest, text/plain", "text/plain", child.getMimeType());
        child.setHeader(headerMessageRFC822);
        assertEquals("multipart/digest, message/rfc822", "message/rfc822", child.getMimeType());
        parent = new Message();
        child = new Message();
        child.setParent(parent);
        parent.setHeader(headerMultipartMixed);
        child.setHeader(headerEmpty);
        assertEquals("multipart/mixed, empty", "text/plain", child.getMimeType());
        child.setHeader(headerTextPlain);
        assertEquals("multipart/mixed, text/plain", "text/plain", child.getMimeType());
        child.setHeader(headerMessageRFC822);
        assertEquals("multipart/mixed, message/rfc822", "message/rfc822", child.getMimeType());
        child = new Message();
        child.setHeader(headerEmpty);
        assertEquals("null, empty", "text/plain", child.getMimeType());
        child.setHeader(headerTextPlain);
        assertEquals("null, text/plain", "text/plain", child.getMimeType());
        child.setHeader(headerMessageRFC822);
        assertEquals("null, message/rfc822", "message/rfc822", child.getMimeType());
    }

    public void testIsMultipart() {
        Message m = new Message();
        m.setHeader(headerEmpty);
        assertTrue("empty", !m.isMultipart());
        m.setHeader(headerTextPlain);
        assertTrue("text/plain", !m.isMultipart());
        m.setHeader(headerMultipartDigest);
        assertTrue("multipart/digest", m.isMultipart());
        m.setHeader(headerMultipartMixed);
        assertTrue("multipart/mixed", m.isMultipart());
    }
}
