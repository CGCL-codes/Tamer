package com.carbonfive.sstemplates;

import org.slf4j.*;

public class SsTemplateException extends Exception {

    private static final Logger log = LoggerFactory.getLogger(SsTemplateException.class);

    public SsTemplateException() {
        super();
    }

    public SsTemplateException(String message) {
        super(message);
    }

    public SsTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public SsTemplateException(Throwable cause) {
        super(cause);
    }
}
