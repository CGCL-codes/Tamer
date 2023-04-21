package com.browseengine.local.service.tiger;

/**
 * @author spackle
 *
 */
public class TigerParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public TigerParseException(String s) {
        super(s);
    }

    public TigerParseException(String s, Throwable cause) {
        super(s, cause);
    }
}
