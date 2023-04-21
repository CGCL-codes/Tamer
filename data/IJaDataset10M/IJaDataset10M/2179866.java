package com.vayoodoot.partner;

import com.vayoodoot.exception.VDException;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: May 16, 2007
 * Time: 3:25:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PartnerException extends VDException {

    public PartnerException(String messageId) {
        super(messageId);
    }

    public PartnerException(String messageId, Throwable prevException) {
        super(messageId, prevException);
    }

    public String toString() {
        return ("PartnerException: " + message);
    }
}
