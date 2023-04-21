package net.raymanoz.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlainProperties implements Properties {

    private java.util.Properties primaryProperties;

    private java.util.Properties secondaryProperties;

    public static PlainProperties createFromResource(String resourceName) {
        try {
            InputStream inputStream = ClassLoader.getSystemResource(resourceName).openStream();
            return new PlainProperties(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream loadResource(String resourceName) {
        try {
            return ClassLoader.getSystemResource(resourceName).openStream();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Loading Resource: %s threw %s", resourceName, e.getMessage()), e);
        }
    }

    public static PlainProperties createFromResource(String primaryResourceName, String secondaryResourceName) {
        return new PlainProperties(loadResource(primaryResourceName), loadResource(secondaryResourceName));
    }

    public PlainProperties() {
        primaryProperties = new java.util.Properties();
        secondaryProperties = new java.util.Properties();
    }

    public PlainProperties(String properties) {
        this(new ByteArrayInputStream(properties.getBytes()));
    }

    public PlainProperties(String primaryProperties, String secondaryProperties) {
        this(new ByteArrayInputStream(primaryProperties.getBytes()), new ByteArrayInputStream(secondaryProperties.getBytes()));
    }

    public PlainProperties(InputStream stream) {
        this();
        try {
            primaryProperties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlainProperties(InputStream primaryStream, InputStream secondaryStream) {
        this();
        try {
            primaryProperties.load(primaryStream);
            secondaryProperties.load(secondaryStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return primaryProperties.getProperty(key, secondaryProperties.getProperty(key));
    }

    public void setProperty(String key, String value) {
        primaryProperties.setProperty(key, value);
    }
}
