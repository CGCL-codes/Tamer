package com.bitgate.util.services.engine.tags.u;

import java.util.ArrayList;
import org.w3c.dom.Node;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.services.engine.DocumentTag;
import com.bitgate.util.services.engine.RenderEngine;
import com.bitgate.util.services.engine.TagInspector;
import com.bitgate.util.services.engine.tags.ElementDescriber;

/**
 * This element allows one to add a header to the connected client before sending data.  It would allow for things like content
 * overriding, location change, and other headers that could be sent to a web browser that can handle said headers.  It is
 * not possible to overrride the content length, or any other headers that are controlled by Webplasm.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/services/engine/tags/u/Header.java#11 $
 */
public class Header extends DocumentTag implements ElementDescriber {

    private String header, headerValue;

    public Header() {
    }

    public ArrayList getSubElements() {
        return new ArrayList();
    }

    public void prepareTag(Node n) {
        super.prepareTag(n);
    }

    public StringBuffer render(RenderEngine c) {
        if (c.isBreakState() || !c.canRender("u")) {
            return new StringBuffer();
        }
        header = TagInspector.processElement(header, c);
        headerValue = TagInspector.processElement(headerValue, c);
        c.getWorkerContext().addClientHeader(header + ": " + headerValue);
        Debug.inform("Adding browser header '" + header + ": " + headerValue + "'");
        return new StringBuffer();
    }
}
