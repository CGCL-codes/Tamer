package com.volantis.mcs.xdime.response;

import com.volantis.mcs.protocols.response.attributes.ResponseCarouselAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of carousel element from widgets-response namespace.
 * 
 * Elements from this namespace are used for building responses to
 * AJAX requests in a device-independent way. This particular element 
 * is a container for the content returned in response to AJAX
 * request from Carousel widget 
 */
public class ResponseCarouselElement extends ResponseElement {

    public ResponseCarouselElement(XDIMEContextInternal context) {
        super(ResponseElements.CAROUSEL, context);
        protocolAttributes = new ResponseCarouselAttributes();
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    public void callCloseOnProtocol(XDIMEContextInternal context) {
    }
}
