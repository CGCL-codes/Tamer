package javax.swing.text.html;

import java.net.URL;
import javax.swing.BasicSwingTestCase;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.Element;

public class HTMLFrameHyperlinkEventTest extends BasicSwingTestCase {

    HTMLFrameHyperlinkEvent event;

    public void testHTMLFrameHyperlinkEventObjectEventTypeURLElementString() throws Exception {
        final Object source = new Object();
        final EventType type = EventType.ENTERED;
        final URL url = new URL("file:///");
        final Element element = new HTMLDocument().getDefaultRootElement();
        final String targetFrame = "targetFrame";
        HTMLFrameHyperlinkEvent event = new HTMLFrameHyperlinkEvent(source, type, url, element, targetFrame);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertSame(url, event.getURL());
        assertSame(element, event.getSourceElement());
        assertNull(event.getDescription());
        assertSame(targetFrame, event.getTarget());
        event = new HTMLFrameHyperlinkEvent(source, type, null, (Element) null, null);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertNull(event.getURL());
        assertNull(event.getSourceElement());
        assertNull(event.getDescription());
        assertNull(event.getTarget());
        new NullPointerCase() {

            public void exceptionalAction() throws Exception {
                new HTMLFrameHyperlinkEvent(null, type, null, (Element) null, null);
            }
        };
    }

    public void testHTMLFrameHyperlinkEventObjectEventTypeURLString() throws Exception {
        final Object source = new Object();
        final EventType type = EventType.ENTERED;
        final URL url = new URL("file:///");
        final String targetFrame = "targetFrame";
        HTMLFrameHyperlinkEvent event = new HTMLFrameHyperlinkEvent(source, type, url, targetFrame);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertSame(url, event.getURL());
        assertNull(event.getSourceElement());
        assertNull(event.getDescription());
        assertSame(targetFrame, event.getTarget());
        event = new HTMLFrameHyperlinkEvent(source, type, null, null);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertNull(event.getURL());
        assertNull(event.getSourceElement());
        assertNull(event.getDescription());
        assertNull(event.getTarget());
        new NullPointerCase() {

            public void exceptionalAction() throws Exception {
                new HTMLFrameHyperlinkEvent(null, type, null, null);
            }
        };
    }

    public void testHTMLFrameHyperlinkEventObjectEventTypeURLStringString() throws Exception {
        final Object source = new Object();
        final EventType type = EventType.ENTERED;
        final URL url = new URL("file:///");
        final String targetFrame = "targetFrame";
        final String descr = "description";
        HTMLFrameHyperlinkEvent event = new HTMLFrameHyperlinkEvent(source, type, url, descr, targetFrame);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertSame(url, event.getURL());
        assertNull(event.getSourceElement());
        assertSame(targetFrame, event.getTarget());
        assertSame(descr, event.getDescription());
        event = new HTMLFrameHyperlinkEvent(source, type, null, (String) null, (String) null);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertNull(event.getURL());
        assertNull(event.getSourceElement());
        assertNull(event.getDescription());
        assertNull(event.getTarget());
        new NullPointerCase() {

            public void exceptionalAction() throws Exception {
                new HTMLFrameHyperlinkEvent(null, type, null, (String) null, (String) null);
            }
        };
    }

    public void testHTMLFrameHyperlinkEventObjectEventTypeURLStringElementString() throws Exception {
        final Object source = new Object();
        final EventType type = EventType.ENTERED;
        final URL url = new URL("file:///");
        final Element element = new HTMLDocument().getDefaultRootElement();
        final String targetFrame = "targetFrame";
        final String descr = "description";
        HTMLFrameHyperlinkEvent event = new HTMLFrameHyperlinkEvent(source, type, url, descr, element, targetFrame);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertSame(url, event.getURL());
        assertSame(element, event.getSourceElement());
        assertSame(descr, event.getDescription());
        assertSame(targetFrame, event.getTarget());
        event = new HTMLFrameHyperlinkEvent(source, type, null, null, (Element) null, null);
        assertSame(source, event.getSource());
        assertSame(type, event.getEventType());
        assertNull(event.getURL());
        assertNull(event.getSourceElement());
        assertNull(event.getDescription());
        assertNull(event.getTarget());
        new NullPointerCase() {

            public void exceptionalAction() throws Exception {
                new HTMLFrameHyperlinkEvent(null, type, null, null, (Element) null, null);
            }
        };
    }
}
