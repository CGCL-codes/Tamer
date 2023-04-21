package jgb.handlers.swing;

import jgb.builder.TagHandler;
import jgb.builder.WindowContext;
import jgb.handlers.TagHandlerAbstractTest;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

public class TestRegisterTagHandler extends TagHandlerAbstractTest {

    private WindowContext eventContext;

    private EventObject eventObject;

    private List listeners;

    private WindowContext windowContext;

    public TestRegisterTagHandler(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        listeners = new ArrayList();
        Object parentObject = new Object() {

            public void addWindowListener(WindowListener l) {
                listeners.add(l);
            }

            public void addMouseListener(MouseListener l) {
                listeners.add(l);
            }

            public void addHyperlinkListener(HyperlinkListener l) {
                listeners.add(l);
            }
        };
        windowContext = new WindowContext() {

            public Object getObject(String name) {
                return null;
            }
        };
        context.put(TagHandler.WINDOW_CONTEXT_KEY, windowContext);
        stackManager.pushCurrentObject(parentObject);
        objectsMap.put("anObject", this);
    }

    public void testRegistersListener() throws SAXException {
        atts.put("class", "java.awt.event.WindowListener");
        atts.put("event", "windowOpened");
        atts.put("manager", "anObject");
        atts.put("method", "windowOpenedOnAnObject");
        handler.startElement("register", context, atts);
        assertEquals(1, listeners.size());
        WindowListener l = (WindowListener) listeners.get(0);
        assertNotNull("registered an object", l);
        final WindowEvent generatedEvent = new WindowEvent(new JFrame(), 1);
        l.windowOpened(generatedEvent);
        assertSame(generatedEvent, eventObject);
        assertSame(windowContext, eventContext);
    }

    public void testDoesNotNotifyIfNotProperEventName() throws SAXException {
        atts.put("class", "java.awt.event.WindowListener");
        atts.put("event", "windowOpened");
        atts.put("manager", "anObject");
        atts.put("method", "windowOpenedOnAnObject");
        handler.startElement("register", context, atts);
        WindowListener l = (WindowListener) listeners.get(0);
        final WindowEvent generatedEvent = new WindowEvent(new JFrame(), 1);
        l.windowClosed(generatedEvent);
        assertNull("did not receive an event", eventObject);
        assertNull("did not receive an event", eventContext);
    }

    public void testInstantiatesNewListenerEachTime() throws SAXException {
        atts.put("class", "java.awt.event.MouseListener");
        atts.put("event", "mouseClicked");
        atts.put("manager", "anObject");
        atts.put("method", "mouseClicked");
        handler.startElement("register", context, atts);
        handler.endElement("register", context);
        atts.put("class", "java.awt.event.MouseListener");
        atts.put("event", "mouseEntered");
        atts.put("manager", "anObject");
        atts.put("method", "mouseEntered");
        handler.startElement("register", context, atts);
        handler.endElement("register", context);
        atts.put("class", "java.awt.event.MouseListener");
        atts.put("event", "mouseEntered");
        atts.put("manager", "anObject");
        atts.put("method", "mouseEntered");
        handler.startElement("register", context, atts);
        handler.endElement("register", context);
        assertEquals(3, listeners.size());
    }

    public void testSearchesInJavaAwtEventByDefault() throws SAXException {
        atts.put("class", "MouseListener");
        atts.put("event", "mouseClicked");
        atts.put("manager", "anObject");
        atts.put("method", "mouseClicked");
        handler.startElement("register", context, atts);
    }

    public void testSearchesInJavaxSwingEventByDefault() throws SAXException {
        atts.put("class", "HyperlinkListener");
        atts.put("event", "hyperlinkUpdate");
        atts.put("manager", "anObject");
        atts.put("method", "mouseClicked");
        handler.startElement("register", context, atts);
    }

    public void testRegisterRefusesToUseNonEventObjectObjects() throws SAXException {
        atts.put("class", "java.util.LinkedList");
        atts.put("event", "add");
        atts.put("manager", "anObject");
        atts.put("method", "mouseClicked");
        try {
            handler.startElement("register", context, atts);
            fail("Failed to report invalid event listener class");
        } catch (SAXException possibleSuccess) {
            assertEquals(IllegalArgumentException.class, possibleSuccess.getException().getClass());
            assertTrue(-1 != possibleSuccess.getMessage().indexOf("register listener class must be an interface"));
            assertTrue(-1 != possibleSuccess.getException().getMessage().indexOf(LinkedList.class.getName()));
        }
    }

    public void testReportsSaxExceptionIfMethodDoesNotExist() throws SAXException {
        atts.put("class", "java.awt.event.ComponentListener");
        atts.put("event", "componentHidden");
        atts.put("manager", "anObject");
        atts.put("method", "mouseEntered");
        try {
            handler.startElement("register", context, atts);
            fail("Failed to report exception when cannot register listener with caller");
        } catch (SAXException success) {
        }
    }

    protected TagHandler createHandler() {
        return new RegisterTagHandler();
    }

    public void windowOpenedOnAnObject(EventObject o, WindowContext context) {
        this.eventObject = o;
        this.eventContext = context;
    }

    public void windowClosedOnAnObject(EventObject o, WindowContext context) {
        fail("Should never be called");
    }

    public void mouseClicked(EventObject o, WindowContext context) {
        fail("Should never be called");
    }

    public void mouseEntered(EventObject o, WindowContext context) {
        fail("Should never be called");
    }
}
