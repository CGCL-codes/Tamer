package org.isurf.dimintegration.callback;

import org.isurf.spmiddleware.model.epcis.EPCISDocument;

/**
 * Callbackinterface for sending EPCIS events to an EPCIS repository.
 */
public interface EPCISCallback {

    /**
	 * Sends the EPCIS events to the Data Integrator Module.
	 *
	 * @param epcisEvent The EPCIS event.
	 */
    void epcisCallback(EPCISDocument epcisEvent);

    /**
	 * Sets the callback URL.
	 * @param url
	 */
    void setUrl(String url);

    /**
	 * Boolean to configure whether or not callback should be made.
	 * @param callbackEnabled
	 */
    void setCallbackEnabled(boolean callbackEnabled);
}
