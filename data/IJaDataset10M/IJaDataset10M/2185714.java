package com.jtri.exception;

import org.apache.commons.beanutils.ConversionException;

/**
 * @author atorres
 * @version 1.0, 12/03/2005
 * 
 */
public class KeyConversionException extends ConversionException implements IKeyException {

    String[] params;

    public void setParams(String[] params) {
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }

    public KeyConversionException(String key) {
        super(key);
    }

    public KeyConversionException(String key, String[] params) {
        super(key);
        this.params = params;
    }
}
