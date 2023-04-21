package jgb.handlers.swing;

import jgb.builder.TagHandler;
import jgb.handlers.TagHandlerAbstractTest;
import javax.swing.*;
import java.awt.*;
import org.xml.sax.SAXException;

/**
 * @author Francois Beausoleil, <a href="mailto:fbos@users.sourceforge.net">fbos@users.sourceforge.net</a>
 */
public class TestLayoutTagHandler extends TagHandlerAbstractTest {

    private Container contentPane;

    public TestLayoutTagHandler(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        JDialog dialog = new JDialog();
        contentPane = dialog.getContentPane();
        contentPane.setLayout(null);
        stackManager.pushCurrentObject(contentPane);
    }

    public void testStartElement() throws Exception {
        atts.put("class", "java.awt.FlowLayout");
        int oldStackSize = stackManager.size();
        int oldContextSize = context.size();
        handler.startElement("layout", context, atts);
        assertEquals(context.toString(), oldContextSize + 1, context.size());
        assertEquals(stackManager.toString(), oldStackSize + 1, stackManager.size());
        assertTrue(context.containsKey(TagHandler.CURRENT_OBJECT_KEY));
        assertTrue(false == context.containsKey(TagHandler.CURRENT_OBJECT_ID_KEY));
        assertEquals(0, contentPane.getComponentCount());
        assertEquals(FlowLayout.class, contentPane.getLayout().getClass());
        assertEquals(FlowLayout.class, stackManager.getCurrentObject().getClass());
        assertSame(context.get(TagHandler.CURRENT_OBJECT_KEY), stackManager.getCurrentObject());
    }

    public void testEndElement() throws Exception {
        stackManager.pushCurrentObject(new Object());
        context.put(TagHandler.CURRENT_OBJECT_KEY, stackManager.getCurrentObject());
        int oldStackSize = stackManager.size();
        int oldContextSize = context.size();
        handler.endElement("layout", context);
        assertEquals(oldContextSize, context.size());
        assertEquals(oldStackSize - 1, stackManager.size());
    }

    public void testStartElement_ThrowsSaxExceptionWhenClassNotFound() throws SAXException {
        final String className = "jgb.noclass.possible.with.this.Name";
        atts.put("class", className);
        try {
            handler.startElement("layout", context, atts);
            fail("Failed to throw expected Exception");
        } catch (SAXException possibleSuccess) {
            assertEquals(ClassNotFoundException.class, possibleSuccess.getException().getClass());
            assertTrue(-1 != possibleSuccess.getException().getMessage().indexOf(className));
            assertTrue(-1 != possibleSuccess.getMessage().indexOf("Layout manager class not found"));
        }
    }

    public void testStartElement_ThrowsSaxExceptionWhenClassNotInstantiable() throws SAXException {
        final String className = ExceptionLayoutClass.class.getName();
        atts.put("class", className);
        try {
            handler.startElement("layout", context, atts);
            fail("Failed to throw expected Exception");
        } catch (SAXException possibleSuccess) {
            assertEquals(IllegalStateException.class, possibleSuccess.getException().getClass());
            assertTrue(-1 != possibleSuccess.getMessage().indexOf(className));
            assertTrue(-1 != possibleSuccess.getMessage().indexOf("Could not instantiate layout manager"));
        }
    }

    protected TagHandler createHandler() {
        return new LayoutTagHandler();
    }

    public static final class ExceptionLayoutClass {

        public ExceptionLayoutClass() {
            throw new IllegalStateException("invalid layout");
        }
    }
}
