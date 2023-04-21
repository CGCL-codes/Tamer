package com.googlecode.acpj.patterns;

import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * Simple class representing a request for service that includes some data with 
 * the request and also provides a WritePort to which the result should be sent.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class RequestWithData<DT, CT> extends Request<CT> {

    private DT requestData = null;

    public RequestWithData(DT requestData, WritePort<CT> callbackPort) {
        super(callbackPort);
        this.setRequestData(requestData);
    }

    public void setRequestData(DT requestData) {
        this.requestData = requestData;
    }

    public DT getRequestData() {
        return this.requestData;
    }
}
