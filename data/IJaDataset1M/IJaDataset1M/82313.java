package com.volantis.mcs.protocols.widgets;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Widget module used within XHTMLFull protocol.
 */
public interface XHTMLFullWidgetModule extends WidgetModule {

    /**
     * Processes the specified body element, so that the onload attribute
     * contains an invocation of the Widget.startup(). This is required by the
     * 'vfc-base.js' library to work.
     * 
     * @param protocol The protocol used.
     * @param element The body element.
     */
    public void processBodyElementForStartup(VolantisProtocol protocol, Element element);
}
