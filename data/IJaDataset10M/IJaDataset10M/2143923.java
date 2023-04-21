package com.bitgate.util.services.engine.tags.u;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Node;
import com.bitgate.util.services.engine.DocumentTag;
import com.bitgate.util.services.engine.RenderEngine;
import com.bitgate.util.services.engine.TagInspector;
import com.bitgate.util.services.engine.tags.ElementDescriber;
import com.bitgate.util.services.protocol.WebResponseCodes;

/**
 * This class allows a user to redirect a browser to a different page or URL.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/services/engine/tags/u/Redirect.java#10 $
 */
public class Redirect extends DocumentTag implements ElementDescriber {

    public Redirect() {
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
        StringBuffer redirectBuffer = null;
        redirectBuffer = TagInspector.processBody(this, c);
        if (redirectBuffer == null || redirectBuffer.equals("")) {
            c.setExceptionState(true, "Redirect element requires a location to be redirected to.");
            return new StringBuffer();
        }
        boolean allowed = true;
        Node parentNode = thisNode;
        while ((parentNode = parentNode.getParentNode()) != null) {
            String nodeName = parentNode.getNodeName().toLowerCase();
            if (nodeName != null && nodeName.equals("u:documentbody")) {
                allowed = false;
            }
        }
        if (allowed) {
            c.setBypassRendering(true);
            HashMap headers = new HashMap();
            headers.put("Location", redirectBuffer.toString());
            WebResponseCodes.genericResponseWithHeaders(c.getWorkerContext(), WebResponseCodes.HTTP_MOVED_TEMP, "Please tune your browser to '" + redirectBuffer + "' to continue.", headers);
        } else {
            c.setExceptionState(true, "Redirect can only be used inside a u:document block.");
        }
        return new StringBuffer();
    }
}
