package com.volantis.mcs.xdime.response;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.response.attributes.ResponseProgressAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of AJAX resposne for Progress widget.  
 */
public class ResponseProgressElement extends ResponseElement {

    public ResponseProgressElement(XDIMEContextInternal context) {
        super(ResponseElements.PROGRESS, context);
        protocolAttributes = new ResponseProgressAttributes();
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        MarinerPageContext pageContext = getPageContext(context);
        OutputBuffer buffer = pageContext.getCurrentOutputBuffer();
        buffer.writeText("{percentage: ");
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    public void callCloseOnProtocol(XDIMEContextInternal context) {
        MarinerPageContext pageContext = getPageContext(context);
        OutputBuffer buffer = pageContext.getCurrentOutputBuffer();
        buffer.writeText("}");
    }
}
