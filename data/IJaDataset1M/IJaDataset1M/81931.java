package jgb.handlers.swing;

import jgb.builder.TagHandler;
import jgb.builder.utils.Constraints;
import jgb.handlers.TagHandlerAbstractTest;
import jgb.mocks.MockContainer;
import org.xml.sax.SAXException;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Francois Beausoleil, <a href="mailto:fbos@users.sourceforge.net">fbos@users.sourceforge.net</a>
 */
public class TestConstraintsTagHandler extends TagHandlerAbstractTest {

    private Map constraintsMap;

    public TestConstraintsTagHandler(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        stackManager.pushCurrentObject(new MockContainer());
        constraintsMap = new HashMap();
        context.put(TagHandler.CURRENT_CONSTRAINTS_KEY, constraintsMap);
    }

    public void testStartElement_NoOp() throws Exception {
        final Map oldContext = Collections.unmodifiableMap(new HashMap(context));
        handler.startElement("constraints", context, atts);
        assertEquals(oldContext, context);
    }

    public void testReset() throws Exception {
        atts.put("reset", "true");
        constraintsMap.put(stackManager.getCurrentObject(), new Constraints("CONSTRAINTS_01", String.class));
        assertTrue(context.containsKey(TagHandler.CURRENT_CONSTRAINTS_KEY));
        assertNotNull(context.get(TagHandler.CURRENT_CONSTRAINTS_KEY));
        handler.startElement("constraints", context, atts);
        handler.endElement("constraints", context);
        assertFalse("constraints were removed from the map", constraintsMap.containsKey(stackManager.getCurrentObject()));
    }

    public void testEndElement() throws Exception {
        context.put(TagHandler.PARAMETER_CLASS_KEY, java.awt.BorderLayout.CENTER.getClass());
        context.put(TagHandler.PARAMETER_VALUE_KEY, java.awt.BorderLayout.CENTER);
        handler.endElement("constraints", context);
        assertFalse("context was cleaned up - parameter class", context.containsKey(TagHandler.PARAMETER_CLASS_KEY));
        assertFalse("context was cleaned up - parameter value", context.containsKey(TagHandler.PARAMETER_VALUE_KEY));
        final Constraints currentConstraints = (Constraints) constraintsMap.get(stackManager.getCurrentObject());
        assertNotNull("constraints were saved in map, with current object as key", currentConstraints);
        assertEquals("constraint's value was saved in current constraints", java.awt.BorderLayout.CENTER, currentConstraints.getValue());
        assertEquals("constraint's class was saved in current constraints", java.awt.BorderLayout.CENTER.getClass(), currentConstraints.getValueClass());
    }

    protected TagHandler createHandler() {
        return new ConstraintsTagHandler();
    }
}
