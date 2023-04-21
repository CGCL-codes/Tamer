package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestType;

/**
 * Rule for webd:get element.
 */
public class GetOperationRule extends OperationRule {

    /**
     * Initialise.
     */
    public GetOperationRule() {
        super(HTTPRequestType.GET);
    }
}
