package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.ab.editors.ElementSelectionChangeEvent;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;
import org.jdom.Element;

/**
 * Test the ElementSelectionChangeEvent class
 */
public class ElementSelectionChangeEventTestCase extends TestCaseAbstract {

    /**
     * ODOMfactory for factoring ODOMObservables
     */
    JDOMFactory factory = new ODOMFactory();

    /**
     * Test the event with a valid JDOM Element.
     */
    public void testEventWithJDOMElement() throws Exception {
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        Element source = factory.element("testElement");
        ElementSelectionChangeEvent event = new ElementSelectionChangeEvent(source);
        String mustBeNull = "Value should be null";
        assertNull(mustBeNull, event.getNewValue());
        assertNull(mustBeNull, event.getOldValue());
        String match = "Values should match";
        assertEquals(match, ChangeQualifier.NONE, event.getChangeQualifier());
        assertEquals(match, source, event.getSource());
    }

    /**
     * Test the event with a valid ODOMChangeEvent object.
     */
    public void testEventWithODOMChangeEvent() throws Exception {
        ODOMElement source = (ODOMElement) factory.element("test");
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        Element oldObject = factory.element("oldObject");
        Element newObject = factory.element("newObject");
        ODOMChangeEvent event = ODOMChangeEvent.createNew(source, oldObject, newObject, ChangeQualifier.NAMESPACE);
        ElementSelectionChangeEvent selectionChangeEvent = new ElementSelectionChangeEvent(event);
        final String match = "Values should match";
        assertEquals(match, newObject, selectionChangeEvent.getNewValue());
        assertEquals(match, oldObject, selectionChangeEvent.getOldValue());
        assertEquals(match, ChangeQualifier.NAMESPACE, event.getChangeQualifier());
        assertEquals(match, source, event.getSource());
    }
}
