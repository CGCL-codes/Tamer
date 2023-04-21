package com.pentagaia.tb.start.impl.kernel0951;

import java.util.Properties;
import com.pentagaia.tb.start.api.IPdsPropertyParser;
import com.sun.sgs.impl.kernel.StandardProperties;

/**
 * A parser to verify the properties
 * 
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 */
public class PropertyParser0951 implements IPdsPropertyParser {

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.start.api.IPdsPropertyParser#refactor(java.util.Properties, java.util.Properties)
     */
    public void refactor(final Properties systemProperties, final Properties applicationProperties) {
        if (systemProperties.containsKey(StandardProperties.APP_NAME)) {
            throw new IllegalArgumentException("Application name was specified in system properties");
        }
        if (applicationProperties.getProperty(StandardProperties.APP_NAME) == null) {
            throw new IllegalStateException("Missing required property " + StandardProperties.APP_NAME + " from application config.");
        }
        if (applicationProperties.getProperty(StandardProperties.APP_ROOT) == null) {
            throw new IllegalStateException("Missing required property " + StandardProperties.APP_ROOT + " from application config.");
        }
        if (applicationProperties.getProperty(StandardProperties.APP_LISTENER) == null) {
            throw new IllegalStateException("Missing required property " + StandardProperties.APP_LISTENER + " from application config.");
        }
        if (applicationProperties.getProperty(StandardProperties.APP_PORT) == null) {
            throw new IllegalStateException("Missing required property " + StandardProperties.APP_PORT + " from application config.");
        }
    }
}
