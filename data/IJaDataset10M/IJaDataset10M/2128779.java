package org.tripcom.ws.compwsmx.showcase;

import org.apache.axiom.om.OMElement;

public abstract class BookingService {

    protected static final boolean DEBUG = true;

    protected String tripFrom;

    protected String tripTo;

    public abstract OMElement bookTrip(OMElement input);
}
