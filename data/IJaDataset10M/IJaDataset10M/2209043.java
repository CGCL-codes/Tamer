package org.itsnat.impl.core.js.dom.event;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatDOMExtEvent;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentImpl;
import org.itsnat.impl.core.js.dom.event.domstd.JSDOMStdEventRenderImpl;
import org.itsnat.impl.core.js.dom.event.domext.JSDOMExtEventRenderImpl;
import org.itsnat.impl.core.path.NodeLocationWithParent;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class JSEventRenderImpl {

    /** Creates a new instance of JSEventRenderImpl */
    public JSEventRenderImpl() {
    }

    public static JSEventRenderImpl getJSEventRender(Event event, Browser browser) {
        if (event instanceof ItsNatDOMStdEvent) return JSDOMStdEventRenderImpl.getJSDOMStdEventRender((ItsNatDOMStdEvent) event, browser); else if (event instanceof ItsNatDOMExtEvent) return JSDOMExtEventRenderImpl.getJSDOMExtEventRender((ItsNatDOMExtEvent) event); else throw new ItsNatException("This event type is not supported", event);
    }

    public abstract String getDispatchEvent(NodeLocationWithParent nodeLoc, Event evt, String varResName, ClientAJAXDocumentImpl clientDoc);
}
