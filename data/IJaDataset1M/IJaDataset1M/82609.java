package tests.api.org.xml.sax;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import junit.framework.TestCase;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributeListImpl;
import org.xml.sax.helpers.LocatorImpl;

@SuppressWarnings("deprecation")
@TestTargetClass(HandlerBase.class)
public class HandlerBaseTest extends TestCase {

    private HandlerBase h = new HandlerBase();

    @TestTargetNew(level = TestLevel.COMPLETE, method = "resolveEntity", args = { String.class, String.class })
    public void testResolveEntity() {
        try {
            h.resolveEntity("publicID", "systemID");
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "notationDecl", args = { String.class, String.class, String.class })
    public void testNotationDecl() {
        h.notationDecl("name", "publicID", "systemID");
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "unparsedEntityDecl", args = { String.class, String.class, String.class, String.class })
    public void testUnparsedEntityDecl() {
        h.unparsedEntityDecl("name", "publicID", "systemID", "notationName");
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "setDocumentLocator", args = { org.xml.sax.Locator.class })
    public void testSetDocumentLocator() {
        h.setDocumentLocator(new LocatorImpl());
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "startDocument", args = {  })
    public void testStartDocument() {
        try {
            h.startDocument();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "endDocument", args = {  })
    public void testEndDocument() {
        try {
            h.endDocument();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "startElement", args = { String.class, AttributeList.class })
    public void testStartElement() {
        try {
            h.startElement("name", new AttributeListImpl());
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "endElement", args = { String.class })
    public void testEndElement() {
        try {
            h.endElement("name");
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "characters", args = { char[].class, int.class, int.class })
    public void testCharacters() {
        try {
            h.characters("The quick brown fox".toCharArray(), 4, 11);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "ignorableWhitespace", args = { char[].class, int.class, int.class })
    public void testIgnorableWhitespace() {
        try {
            h.ignorableWhitespace("                   ".toCharArray(), 4, 11);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "processingInstruction", args = { String.class, String.class })
    public void testProcessingInstruction() {
        try {
            h.processingInstruction("target", "data");
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "warning", args = { org.xml.sax.SAXParseException.class })
    public void testWarning() {
        try {
            h.warning(new SAXParseException("Foo", new LocatorImpl()));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "error", args = { org.xml.sax.SAXParseException.class })
    public void testError() {
        try {
            h.error(new SAXParseException("Foo", new LocatorImpl()));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "fatalError", args = { org.xml.sax.SAXParseException.class })
    public void testFatalError() {
        try {
            h.fatalError(new SAXParseException("Foo", new LocatorImpl()));
            fail("SAXException expected");
        } catch (SAXException e) {
        }
        try {
            h.fatalError(null);
            fail("NullPointerException expected");
        } catch (SAXException e) {
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
    }
}
