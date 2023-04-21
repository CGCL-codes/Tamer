package junitmetrics.configuration.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypedProperties extends Properties {

    private static final long serialVersionUID = 1L;

    public static TypedProperties getTypedProperties(String fileName) {
        try {
            TypedProperties properties = new TypedProperties();
            properties.initializeProperties(fileName);
            return properties;
        } catch (Exception e) {
            System.out.println("An error occured diring reading properties. Cause: " + e.getMessage());
            return null;
        }
    }

    public void initializeProperties(String fileName) throws IOException {
        BufferedReader propertiesReader = new BufferedReader(new FileReader(fileName));
        String currentLine;
        while ((currentLine = propertiesReader.readLine()) != null) {
            addProperty(currentLine);
        }
    }

    protected void addProperty(String propertiesFileLine) {
        if (propertiesFileLine != null && !(propertiesFileLine = propertiesFileLine.trim()).equals("") && !propertiesFileLine.startsWith("#")) {
            String[] keyValuePair = propertiesFileLine.split("=", 2);
            String key = keyValuePair[0];
            String value = keyValuePair[1];
            value = replaceSubpropertiesInValue(value);
            setProperty(key.trim(), value.trim());
        }
    }

    protected String replaceSubpropertiesInValue(String value) {
        Pattern pattern = Pattern.compile("\\$\\{.*?\\}");
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String subpropertyKey = matcher.group();
            subpropertyKey = subpropertyKey.substring(2, subpropertyKey.length() - 1).trim();
            value = matcher.replaceFirst(getProperty(subpropertyKey));
            matcher = pattern.matcher(value);
        }
        return value;
    }

    public String getProperty(String key) {
        try {
            return super.getProperty(key).trim();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        try {
            String property = getProperty(key).trim();
            return Boolean.parseBoolean(property);
        } catch (Exception e) {
            System.err.println("An error occurred during parsing boolean value " + key + ". Setting default value: " + defaultValue);
            return defaultValue;
        }
    }

    public int getIntegerProperty(String key, int defaultValue) {
        try {
            String property = getProperty(key).trim();
            return Integer.parseInt(property);
        } catch (Exception e) {
            System.err.println("An error occurred during parsing integer value " + key + ". Setting default value: " + defaultValue);
            return defaultValue;
        }
    }
}
