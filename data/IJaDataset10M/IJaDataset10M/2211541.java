package jgb.handlers.swing;

import jgb.builder.TagHandler;
import jgb.handlers.TagHandlerAbstractTest;
import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Francois Beausoleil, <a href="mailto:fbos@users.sourceforge.net">fbos@users.sourceforge.net</a>
 */
public class TestSeparatorTagHandler extends TagHandlerAbstractTest {

    private JMenu menu;

    public TestSeparatorTagHandler(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        menu = new JMenu();
        stackManager.pushCurrentObject(menu);
    }

    public void testStartElement_Id() throws Exception {
        atts.put("id", "sep0");
        handler.startElement("separator", context, atts);
        JSeparator separator = (JSeparator) objectsMap.get("sep0");
        assertSame(separator, stackManager.getCurrentObject());
        assertEquals("sep0", stackManager.getCurrentObjectId());
        assertEquals(1, menu.getMenuComponentCount());
        assertSame(separator, menu.getMenuComponent(0));
    }

    public void testStartElement_NoId() throws Exception {
        final Map initialMap = Collections.unmodifiableMap((Map) (((HashMap) objectsMap).clone()));
        handler.startElement("separator", context, atts);
        JSeparator separator = (JSeparator) stackManager.getCurrentObject();
        assertNotNull(separator);
        assertNull(stackManager.getCurrentObjectId());
        assertEquals("objectsMap should not have changed", initialMap, objectsMap);
    }

    public void testEndElement() throws Exception {
        stackManager.pushCurrentObject(new Object());
        int oldStackSize = stackManager.size();
        handler.endElement("separator", context);
        assertEquals(oldStackSize - 1, stackManager.size());
    }

    public void testDefaultPackageThrowsException() {
        try {
            ((SeparatorTagHandler) handler).getDefaultPackagePrefix();
            fail("Failed to throw IllegalStateException");
        } catch (IllegalStateException success) {
        }
    }

    protected TagHandler createHandler() {
        return new SeparatorTagHandler();
    }
}
