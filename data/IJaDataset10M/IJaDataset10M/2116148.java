package org.arch4j.property;

import org.arch4j.logging.LoggingCategory;
import org.arch4j.logging.LoggingProvider;
import java.io.*;
import java.text.MessageFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

/**
 * <p> This class is used to modify (add to, update, or delete from) an XML property file.
 * It will also re-write the property file to disk whenever a change is made.
 *
 * @author Ross E. Greinke
 * @version $Revision: 1.7 $
 */
public class XMLPropertyWriter implements PropertyConstants {

    /** The logging category. Not created until the first time it is used. */
    static LoggingCategory loggingCategory = null;

    /** The error message to use for an invalid property. */
    private static final MessageFormat invalidPropertyMessage = new MessageFormat("Error trying to {0} an invalid property with values " + "[key={1}], [value={2}], [type={3}] to domain [name={4}]");

    private static final MessageFormat storageMessage = new MessageFormat("Failed to store the properties to file: [{0}]");

    /** The property map (from an XMLPropertyManager) which is modified. */
    private Map properties = null;

    /** The property file used to store the property data. */
    private File propertyFile = null;

    /**
   * Constructs a <code>XMLPropertyWriter</code>, which will be used to modify
   * and store XML properties.
   *
   * @param properties The property map to fill in.
   * @param propertyFile The XML property file to parse.
   */
    public XMLPropertyWriter(Map properties, File propertyFile) {
        this.properties = properties;
        this.propertyFile = propertyFile;
    }

    /**
   * Returns the logging category to use for logging messages.
   * Since this class is part of the property management subsystem
   * and logging depends on it,
   * try not to access the logging provider unless absolutely necessary.
   * This will help keep property management startup clean.
   *
   * @return The <code>LoggingCategory</code> to use for logging.
   */
    static LoggingCategory getLogger() {
        if (loggingCategory == null) {
            loggingCategory = LoggingProvider.getProvider().getCategory(XMLPropertyWriter.class.getName());
        }
        return loggingCategory;
    }

    /**
   * Lookup the property map for the given domain name.
   * If the map doesn't exist, one is created for the domain name.
   * @param domainName The domain name key to use to find a property map.
   * @return The property map, a <code>Map</code>, mapping property keys to arrays of values.
   */
    private Map getDomainMap(String domainName) {
        Map domainMap = (Map) this.properties.get(domainName);
        if (domainMap == null) {
            domainMap = new TreeMap();
            this.properties.put(domainName, domainMap);
        }
        return domainMap;
    }

    /**
   * Add the given Property object to the property map identified by the given domain name.
   * If the add was successful, the property file is re-written with the new data.
   *
   * @param domainName The domain name key to use to find the correct property map.
   * @param property The property to add.
   */
    public void add(String domainName, Property property) {
        if (property == null) {
            return;
        } else if (!property.isValid()) {
            logInvalidProperty(property, ADD_STRING, domainName);
            return;
        }
        boolean addWasSuccessful = false;
        switch(property.getType().charAt(0)) {
            case BOOLEAN_CHAR:
                addWasSuccessful = addBoolean(domainName, property);
                break;
            case CHAR_CHAR:
                addWasSuccessful = addChar(domainName, property);
                break;
            case DOUBLE_CHAR:
                addWasSuccessful = addDouble(domainName, property);
                break;
            case FLOAT_CHAR:
                addWasSuccessful = addFloat(domainName, property);
                break;
            case INT_CHAR:
                addWasSuccessful = addInt(domainName, property);
                break;
            case LONG_CHAR:
                addWasSuccessful = addLong(domainName, property);
                break;
            case STRING_CHAR:
                addWasSuccessful = addString(domainName, property);
                break;
            default:
                break;
        }
        if (addWasSuccessful) {
            store();
        }
    }

    /**
   * Add a new boolean property to the main property map.
   * The array will be created if the property key does not yet exist.
   * Otherwise, the array will grow to hold the new property value.
   *
   * @param domainName The domain name key to use to find the property map.
   * @param property The new Property to add.
   * @return <code>true</code>, if the Property was added successfully.
   */
    private boolean addBoolean(String domainName, Property property) {
        boolean newValue;
        if (TRUE_STRING.equalsIgnoreCase(property.getValue()) || FALSE_STRING.equalsIgnoreCase(property.getValue())) {
            newValue = TRUE_STRING.equalsIgnoreCase(property.getValue());
        } else {
            logInvalidProperty(property, ADD_STRING, domainName);
            return false;
        }
        Map domainMap = getDomainMap(domainName);
        boolean[] array = (boolean[]) domainMap.get(property.getKey());
        if (array == null) {
            domainMap.put(property.getKey(), new boolean[] { newValue });
        } else {
            boolean[] grow = new boolean[array.length + 1];
            System.arraycopy(array, 0, grow, 0, array.length);
            grow[array.length] = newValue;
            domainMap.put(property.getKey(), grow);
        }
        return true;
    }

    /**
   * Add a new char property to the main property map.
   * The array will be created if the property key does not yet exist.
   * Otherwise, the array will grow to hold the new property value.
   *
   * @param domainName The domain name key to use to find the property map.
   * @param property The new Property to add.
   * @return <code>true</code>, if the Property was added successfully.
   */
    private boolean addChar(String domainName, Property property) {
        char newValue;
        if (property.getValue().length() == 1) {
            newValue = property.getValue().charAt(0);
        } else {
            logInvalidProperty(property, ADD_STRING, domainName);
            return false;
        }
        Map domainMap = getDomainMap(domainName);
        char[] array = (char[]) domainMap.get(property.getKey());
        if (array == null) {
            domainMap.put(property.getKey(), new char[] { newValue });
        } else {
            char[] grow = new char[array.length + 1];
            System.arraycopy(array, 0, grow, 0, array.length);
            grow[array.length] = newValue;
            domainMap.put(property.getKey(), grow);
        }
        return true;
    }

    /**
   * Add a new double property to the main property map.
   * The array will be created if the property key does not yet exist.
   * Otherwise, the array will grow to hold the new property value.
   *
   * @param domainName The domain name key to use to find the property map.
   * @param property The new Property to add.
   * @return <code>true</code>, if the Property was added successfully.
   */
    private boolean addDouble(String domainName, Property property) {
        double newValue;
        try {
            newValue = Double.parseDouble(property.getValue());
        } catch (Exception e) {
            logInvalidProperty(property, ADD_STRING, domainName);
            return false;
        }
        Map domainMap = getDomainMap(domainName);
        double[] array = (double[]) domainMap.get(property.getKey());
        if (array == null) {
            domainMap.put(property.getKey(), new double[] { newValue });
        } else {
            double[] grow = new double[array.length + 1];
            System.arraycopy(array, 0, grow, 0, array.length);
            grow[array.length] = newValue;
            domainMap.put(property.getKey(), grow);
        }
        return true;
    }

    /**
   * Add a new float property to the main property map.
   * The array will be created if the property key does not yet exist.
   * Otherwise, the array will grow to hold the new property value.
   *
   * @param domainName The domain name key to use to find the property map.
   * @param property The new Property to add.
   * @return <code>true</code>, if the Property was added successfully.
   */
    private boolean addFloat(String domainName, Property property) {
        float newValue;
        try {
            newValue = Float.parseFloat(property.getValue());
        } catch (Exception e) {
            logInvalidProperty(property, ADD_STRING, domainName);
            return false;
        }
        Map domainMap = getDomainMap(domainName);
        float[] array = (float[]) domainMap.get(property.getKey());
        if (array == null) {
            domainMap.put(property.getKey(), new float[] { newValue });
        } else {
            float[] grow = new float[array.length + 1];
            System.arraycopy(array, 0, grow, 0, array.length);
            grow[array.length] = newValue;
            domainMap.put(property.getKey(), grow);
        }
        return true;
    }

    /**
   * Add a new int property to the main property map.
   * The array will be created if the property key does not yet exist.
   * Otherwise, the array will grow to hold the new property value.
   *
   * @param domainName The domain name key to use to find the property map.
   * @param property The new Property to add.
   * @return <code>true</code>, if the Property was added successfully.
   */
    private boolean addInt(String domainName, Property property) {
        int newValue;
        try {
            newValue = Integer.parseInt(property.getValue());
        } catch (Exception e) {
            logInvalidProperty(property, ADD_STRING, domainName);
            return false;
        }
        Map domainMap = getDomainMap(domainName);
        int[] array = (int[]) domainMap.get(property.getKey());
        if (array == null) {
            domainMap.put(property.getKey(), new int[] { newValue });
        } else {
            int[] grow = new int[array.length + 1];
            System.arraycopy(array, 0, grow, 0, array.length);
            grow[array.length] = newValue;
            domainMap.put(property.getKey(), grow);
        }
        return true;
    }

    /**
   * Add a new long property to the main property map.
   * The array will be created if the property key does not yet exist.
   * Otherwise, the array will grow to hold the new property value.
   *
   * @param domainName The domain name key to use to find the property map.
   * @param property The new Property to add.
   * @return <code>true</code>, if the Property was added successfully.
   */
    private boolean addLong(String domainName, Property property) {
        long newValue;
        try {
            newValue = Long.parseLong(property.getValue());
        } catch (Exception e) {
            logInvalidProperty(property, ADD_STRING, domainName);
            return false;
        }
        Map domainMap = getDomainMap(domainName);
        long[] array = (long[]) domainMap.get(property.getKey());
        if (array == null) {
            domainMap.put(property.getKey(), new long[] { newValue });
        } else {
            long[] grow = new long[array.length + 1];
            System.arraycopy(array, 0, grow, 0, array.length);
            grow[array.length] = newValue;
            domainMap.put(property.getKey(), grow);
        }
        return true;
    }

    /**
   * Add a new String property to the main property map.
   * The array will be created if the property key does not yet exist.
   * Otherwise, the array will grow to hold the new property value.
   *
   * @param domainName The domain name key to use to find the property map.
   * @param property The new Property to add.
   * @return <code>true</code>, if the Property was added successfully.
   */
    private boolean addString(String domainName, Property property) {
        Map domainMap = getDomainMap(domainName);
        String[] array = (String[]) domainMap.get(property.getKey());
        if (array == null) {
            domainMap.put(property.getKey(), new String[] { property.getValue() });
        } else {
            String[] grow = new String[array.length + 1];
            System.arraycopy(array, 0, grow, 0, array.length);
            grow[array.length] = property.getValue();
            domainMap.put(property.getKey(), grow);
        }
        return true;
    }

    /**
   * Logs an error for the given invalid Property object.
   *
   * @param invalidProperty The invalid property, a <code>Property</code>.
   * @param action The action that uncovered the invalid property (add, update, delete).
   * @param domainName The domain name containing the property.
   */
    private void logInvalidProperty(Property invalidProperty, String action, String domainName) {
        getLogger().error(invalidPropertyMessage.format(new Object[] { action, invalidProperty.getKey(), invalidProperty.getValue(), invalidProperty.getType(), domainName }));
    }

    /**
   * Writes the data in the property map out to the property file.
   */
    private void store() {
        if (this.propertyFile.exists()) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(this.propertyFile)));
                writer.println(toXml());
                writer.flush();
            } catch (IOException e) {
                getLogger().error(storageMessage.format(new Object[] { this.propertyFile.getPath() }), e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    /**
   * Returns the properties formatted as an XML document string.
   * @return The properties formatted as an XML document string.
   */
    private String toXml() {
        StringBuffer buffer = new StringBuffer();
        String nl = System.getProperty("line.separator");
        buffer.append(XML_DECLARATION).append(nl);
        buffer.append("<").append(TOP_LEVEL_TAG_NAME).append(">").append(nl);
        Map currentMap = null;
        for (Iterator i = this.properties.keySet().iterator(); i.hasNext(); ) {
            String domainName = (String) i.next();
            buffer.append("  <").append(DOMAIN_TAG_NAME).append(" ").append(NAME_TAG_NAME).append("=\"").append(domainName).append("\"").append(">").append(nl);
            currentMap = (Map) this.properties.get(domainName);
            for (Iterator j = currentMap.keySet().iterator(); j.hasNext(); ) {
                String key = (String) j.next();
                Object array = currentMap.get(key);
                if (array instanceof boolean[]) {
                    boolean[] values = (boolean[]) array;
                    for (int k = 0; k < values.length; k++) {
                        buffer.append(toXml(key, String.valueOf(values[k]), BOOLEAN_NAME)).append(nl);
                    }
                } else if (array instanceof char[]) {
                    char[] values = (char[]) array;
                    for (int k = 0; k < values.length; k++) {
                        buffer.append(toXml(key, String.valueOf(values[k]), CHAR_NAME)).append(nl);
                    }
                } else if (array instanceof double[]) {
                    double[] values = (double[]) array;
                    for (int k = 0; k < values.length; k++) {
                        buffer.append(toXml(key, String.valueOf(values[k]), DOUBLE_NAME)).append(nl);
                    }
                } else if (array instanceof float[]) {
                    float[] values = (float[]) array;
                    for (int k = 0; k < values.length; k++) {
                        buffer.append(toXml(key, String.valueOf(values[k]), FLOAT_NAME)).append(nl);
                    }
                } else if (array instanceof int[]) {
                    int[] values = (int[]) array;
                    for (int k = 0; k < values.length; k++) {
                        buffer.append(toXml(key, String.valueOf(values[k]), INT_NAME)).append(nl);
                    }
                } else if (array instanceof long[]) {
                    long[] values = (long[]) array;
                    for (int k = 0; k < values.length; k++) {
                        buffer.append(toXml(key, String.valueOf(values[k]), LONG_NAME)).append(nl);
                    }
                } else if (array instanceof String[]) {
                    String[] values = (String[]) array;
                    for (int k = 0; k < values.length; k++) {
                        buffer.append(toXml(key, values[k], STRING_NAME)).append(nl);
                    }
                }
            }
            buffer.append("  </").append(DOMAIN_TAG_NAME).append(">").append(nl);
        }
        buffer.append("</").append(TOP_LEVEL_TAG_NAME).append(">").append(nl);
        return buffer.toString();
    }

    /**
   * Returns the given property attributes as an XML string.
   *
   * @param key The property key name.
   * @param value The property value already converted to a string.
   * @param type The property type (boolean, long, etc).
   * @return The given property attributes as an XML string.
   */
    private String toXml(String key, String value, String type) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    <").append(PROPERTY_TAG_NAME).append(" ").append(KEY_TAG_NAME).append("=\"").append(key).append("\" ").append(VALUE_TAG_NAME).append("=\"").append(convert(value)).append("\" ").append(TYPE_TAG_NAME).append("=\"").append(type).append("\" ").append("/>");
        return buffer.toString();
    }

    /**
   * Returns a well-formed valid XML string from the given string.
   *
   * @param toEncode The String to encode.
   * @return A well-formed valid XML string from the given string.
   */
    private String convert(String toEncode) {
        StringBuffer buffer = new StringBuffer(toEncode.length());
        for (int i = 0; i < toEncode.length(); i++) {
            char current = toEncode.charAt(i);
            switch(current) {
                case '&':
                    buffer.append("&amp;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '\"':
                    buffer.append("&quot;");
                    break;
                case '\'':
                    buffer.append("&apos;");
                    break;
                default:
                    buffer.append(current);
                    break;
            }
        }
        return buffer.toString();
    }
}
