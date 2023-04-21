package jgb.handlers.swing;

import jgb.builder.TagHandler;
import jgb.handlers.TagHandlerAbstractTest;
import javax.swing.*;

/**
 * @author Francois Beausoleil, <a href="mailto:fbos@users.sourceforge.net">fbos@users.sourceforge.net</a>
 */
public class TestMenuBarTagHandler extends TagHandlerAbstractTest {

    private JFrame frame;

    public TestMenuBarTagHandler(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        frame = new JFrame();
        stackManager.pushCurrentObject(frame);
    }

    public void testStartElement_SetsFramesBar() throws Exception {
        handler.startElement("menubar", context, atts);
        assertNotNull(frame.getJMenuBar());
        assertEquals(JMenuBar.class, frame.getJMenuBar().getClass());
    }

    public void testStartElement_WithId() throws Exception {
        atts.put("id", "menu0");
        handler.startElement("menubar", context, atts);
        JMenuBar bar = (JMenuBar) objectsMap.get("menu0");
        assertNotNull(bar);
    }

    public void testStartElement_MenuBarBecomesCurrentObject() throws Exception {
        atts.put("id", "menu1");
        handler.startElement("menubar", context, atts);
        assertEquals("menu1", stackManager.getCurrentObjectId());
        assertEquals(JMenuBar.class, stackManager.getCurrentObject().getClass());
    }

    public void testEndElement() throws Exception {
        stackManager.pushCurrentObject(new Object());
        int oldStackSize = stackManager.size();
        handler.endElement("menubar", context);
        assertEquals(oldStackSize - 1, stackManager.size());
    }

    public void testDefaultPackageThrowsException() {
        try {
            ((MenuBarTagHandler) handler).getDefaultPackagePrefix();
            fail("Failed to throw IllegalStateException");
        } catch (IllegalStateException success) {
        }
    }

    protected TagHandler createHandler() {
        return new MenuBarTagHandler();
    }
}
