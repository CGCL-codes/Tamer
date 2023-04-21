package com.vangent.hieos.services.xcpd.gateway.serviceimpl;

import com.vangent.hieos.xutil.exception.SOAPFaultException;
import com.vangent.hieos.xutil.exception.XdsWSException;
import org.apache.log4j.Logger;

/**
 * Class to handle all asynchronous web service requests to XCPD Initiating Gateway (IG).
 *
 * @author Bernie Thuman
 */
public class XCPDInitiatingGatewayAsync extends XCPDInitiatingGateway {

    private static final Logger logger = Logger.getLogger(XCPDInitiatingGatewayAsync.class);

    /**
     * This method ensures that an asynchronous request has been sent. It evaluates the message
     * context to dtermine if "ReplyTo" is non-null and is not anonymous. It also ensures that
     * "MessageID" is non-null. It throws an exception if that is not the case.
     * @throws XdsWSException
     */
    @Override
    protected void validateWS() throws SOAPFaultException {
        validateAsyncWS();
    }
}
