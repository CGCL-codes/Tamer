package org.openliberty.arisidbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.openliberty.arisid.AttributeValue;

/**
 * PropertyValue is used to hold property values to be added in
 * Attribute Authority for a DigitalSubject.
 * A PropertyValue may be either String or Binary based on the datatype
 * of the Property in Attribute Authority.
 */
public class PropertyValue extends ArrayList {

    private static final long serialVersionUID = -4567314476612111601L;

    private final String propName;

    private String propLocale = null;

    /**
     * PropertyValue is initialized with attributeName and attributeValues List
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     */
    public PropertyValue(String name, List values) {
        super();
        this.propName = name;
        if (values != null) this.addAll(values);
    }

    /**
     * PropertyValue is initialized with attributeName and attributeValue
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property Value
     */
    public PropertyValue(String name, Object value) {
        super();
        this.propName = name;
        this.add(value);
    }

    /**
     * PropertyValue is initialized with attributeName, attributeValues List and locale
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     * @param locale
     *            Language code
     */
    public PropertyValue(String name, List values, String locale) {
        super();
        this.propName = name;
        if (values != null) this.addAll(values);
        this.propLocale = locale;
    }

    /**
     * PropertyValue is initialized with attributeName, attributeValues List and locale
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     * @param locale
     *            Locale value
     */
    public PropertyValue(String name, List values, Locale locale) {
        super();
        this.propName = name;
        if (values != null) this.addAll(values);
        this.propLocale = IGFObjectManager.getLocaleString(locale);
    }

    /**
     * PropertyValue is initialized with attributeName and attributeValue
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property Value
     * @param locale
     *            Language code
     */
    public PropertyValue(String name, Object value, String locale) {
        super();
        this.propName = name;
        this.add(value);
        this.propLocale = locale;
    }

    /**
     * PropertyValue is initialized with attributeName and attributeValue
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property Value
     * @param locale
     *            Locale value
     */
    public PropertyValue(String name, Object value, Locale locale) {
        super();
        this.propName = name;
        this.add(value);
        this.propLocale = IGFObjectManager.getLocaleString(locale);
    }

    /**
     * Returns the name of the Property
     * 
     * @return Property Name
     */
    public String getName() {
        return propName;
    }

    /**
     * Returns the locale of the Property
     * 
     * @return Property Locale
     */
    public String getLocale() {
        return propLocale;
    }

    /**
     * Returns the Property Values converted to String. If the PropertyValue
     * is Byte array this is converted to Base64 encoded value.
     * 
     * @return List of Property Values 
     */
    public List getStringValues() {
        List<String> values = new ArrayList<String>();
        for (int i = 0; i < this.size(); i++) {
            Object value = this.get(i);
            if (value instanceof byte[]) values.add(AttributeValue.base64Encode((byte[]) value)); else if (value instanceof String) values.add((String) value); else if (value instanceof Integer) values.add(((Integer) value).toString());
        }
        return values;
    }

    /**
     * Compares the supplied value with each of the Property Values and
     * returns TRUE if the value matches
     * 
     * @param val
     *            Property Value to be compared
     * @return TRUE if the supplied value matches with one of
     *         the values of the Property
     */
    public boolean compare(String val) {
        for (int i = 0; i < this.size(); i++) {
            Object value = this.get(i);
            if (value instanceof String && val.equals(value)) return true;
        }
        return false;
    }

    /**
     * Compares the supplied value with each of the Property Values and
     * returns TRUE if the value matches ignoring case
     * 
     * @param val
     *            Property Value to be compared
     * @return TRUE if the supplied value matches with one of
     *         the values of the Property
     */
    public boolean compareIgnoreCase(String val) {
        for (int i = 0; i < this.size(); i++) {
            Object value = this.get(i);
            if (value instanceof String && val.equalsIgnoreCase((String) value)) return true;
        }
        return false;
    }
}
