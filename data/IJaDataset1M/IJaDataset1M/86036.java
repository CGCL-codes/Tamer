package org.systemsbiology.apps.corragui.client.controller.request;

import org.systemsbiology.apps.corragui.client.constants.RequestType;

/**
 * Request for the current version of Corra.
 * 
 * @author Mark Christiansen
 * @version 1.0
 */
@SuppressWarnings("serial")
public class VersionRequest extends Request {

    public VersionRequest() {
    }

    /**
	 * Returns <code>RequestType.VERSION</code>
	 */
    public RequestType getRequestType() {
        return RequestType.VERSION;
    }
}
