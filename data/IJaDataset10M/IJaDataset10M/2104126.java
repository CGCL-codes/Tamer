package com.oneandone.sushi.metadata.xml;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

public class SAXLoaderException extends SAXParseException {

    public SAXLoaderException(String message, Locator locator) {
        super(message, locator);
    }

    public SAXLoaderException(String message, Locator locator, Throwable cause) {
        this(message, locator);
        initCause(cause);
    }
}
