package com.volantis.mcs.protocols.response.attributes;

import com.volantis.mcs.protocols.MCSAttributes;

/**
 * Holds attributes specific to validation element from widgets-response namespace 
 */
public class ResponseValidationAttributes extends MCSAttributes {

    /**
     * The result attribute.
     */
    private String result;

    /**
     * @return Returns the result.
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result The result to set.
     */
    public void setResult(String result) {
        this.result = result;
    }
}
