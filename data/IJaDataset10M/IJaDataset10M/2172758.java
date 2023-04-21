package com.vayoodoot.util;

import com.vayoodoot.exception.VDException;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Apr 21, 2007
 * Time: 1:08:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class VDThreadException extends VDException {

    public VDThreadException(String messageId) {
        super(messageId);
    }

    public VDThreadException(String messageId, Throwable prevException) {
        super(messageId, prevException);
    }

    public String toString() {
        return ("MessageException: " + message);
    }
}
