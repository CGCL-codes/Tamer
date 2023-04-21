package com.frameworkset.util;

import org.frameworkset.util.beans.BeansException;

/**
 * @author biaoping.yin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NoSupportTypeCastException extends BeansException {

    public NoSupportTypeCastException(String msg) {
        super(msg);
    }

    public NoSupportTypeCastException(Object value, Class requiredType, IllegalArgumentException ex) {
        super(value + " can not been change to " + requiredType, ex);
    }
}
