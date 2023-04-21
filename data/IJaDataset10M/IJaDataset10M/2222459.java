package jgb.handlers.swing;

import jgb.builder.TagHandler;
import jgb.handlers.TagHandlerAbstractTest;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Francois Beausoleil, <a href="mailto:fbos@users.sourceforge.net">fbos@users.sourceforge.net</a>
 */
public class TestAbstractTagHandler extends TagHandlerAbstractTest {

    private Map objectsMap;

    private AbstractTagHandler abstractHandler;

    public TestAbstractTagHandler(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        objectsMap = new HashMap();
        objectsMap.put("object0", new Integer(12));
        context.put(TagHandler.OBJECTS_MAP_KEY, objectsMap);
        abstractHandler = (AbstractTagHandler) handler;
        abstractHandler.tagContext = context;
    }

    public void testGetObject() {
        assertSame(objectsMap.get("object0"), abstractHandler.getObject("object0"));
        assertNull(abstractHandler.getObject("object1"));
    }

    public void testPutObject() {
        abstractHandler.putObject("object0", "abs");
        assertEquals("abs", objectsMap.get("object0"));
        abstractHandler.putObject("object1", new Integer(14));
        assertEquals(new Integer(14), abstractHandler.getObject("object1"));
        assertEquals(new Integer(14), objectsMap.get("object1"));
    }

    public void testThrowsSAXExceptionWhenNoLocator() throws Exception {
        try {
            abstractHandler.throwParsingException("this is a test");
            fail("Failed to throw requested exception");
        } catch (SAXException possibleSuccess) {
            pass();
        }
    }

    public void testThrowsSAXParseExceptionWhenLocator() throws Exception {
        context.put(TagHandler.DOCUMENT_LOCATOR_KEY, new Locator() {

            public int getColumnNumber() {
                return 0;
            }

            public int getLineNumber() {
                return 0;
            }

            public String getPublicId() {
                return null;
            }

            public String getSystemId() {
                return null;
            }
        });
        try {
            abstractHandler.throwParsingException("this is a test");
            fail("Failed to throw requested exception");
        } catch (SAXParseException possibleSuccess) {
        }
    }

    protected TagHandler createHandler() {
        return new AbstractTagHandler() {

            protected void enterElement(Map atts) throws SAXException {
            }

            protected void exitElement() throws SAXException {
            }
        };
    }
}
