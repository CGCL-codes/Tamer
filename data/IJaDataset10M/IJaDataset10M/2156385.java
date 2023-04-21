package org.conann.exceptions;

/**
 Indicates problems with a specific configuration.
 Typically these relate to badly configured web-beans.xml.
 */
public class WebBeansConfigurationException extends WebBeansMetadataException {

    public WebBeansConfigurationException(String message, Object... messageParams) {
        super(message, messageParams);
    }

    public WebBeansConfigurationException(Throwable cause, String message, Object... messageParams) {
        super(cause, message, messageParams);
    }
}
