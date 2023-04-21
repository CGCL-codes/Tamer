package org.epo.jbps.heart.events;

import org.epo.jbps.bpaInterface.*;

/**
 * Output Interface Print event
 * Indicates the output interface finish printing job
 * @author Infotel Conseil
 */
public class OIEvtPrint extends OIEvent {

    /**
	 * The BPA Request
	 */
    public BpaRequest request = null;

    /**
	 * IOEvtPrint constructor.
	 * @param myReq Request
	 */
    public OIEvtPrint(BpaRequest myReq) {
        setRequest(myReq);
    }

    /**
	 * Returns the request corresponding to the event 
	 * @return epo.jbps.heart.request.Request
	 */
    public BpaRequest getRequest() {
        return request;
    }

    /**
	 * getType method.
	 * @return char
	 */
    public char getType() {
        return EVT_PRINT;
    }

    /**
	 * Sets the request corresponding to the event
	 * @param newValue epo.jbps.heart.request.Request
	 */
    public void setRequest(BpaRequest newValue) {
        this.request = newValue;
    }
}
