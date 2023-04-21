package org.ogce.xregistry.utils;

import xsul.MLogger;

public class XRegistryClientException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected static MLogger log = MLogger.getLogger(XRegClientConstants.LOGGER_NAME);

    public XRegistryClientException() {
        super();
    }

    public XRegistryClientException(String message, Throwable cause) {
        super(message, cause);
        log.caught(cause);
    }

    public XRegistryClientException(String message) {
        super(message);
    }

    public XRegistryClientException(Throwable cause) {
        super(cause);
        log.caught(cause);
    }
}
