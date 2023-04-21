package com.bluecast.xml;

/**
 * A class to hold information about an attribute defined
 * within an XML document type declaration.
 *
 * @author Yuval Oren, yuval@bluecast.com
 * @version $Revision: 1.4 $
 */
public final class AttributeDefinition {

    public static final int IMPLIED = 1;

    public static final int REQUIRED = 2;

    public static final int FIXED = 3;

    public static final int ENUMERATION = 1;

    public static final int NOTATION = 2;

    public static final int CDATA = 3;

    public static final int ID = 4;

    public static final int IDREF = 5;

    public static final int IDREFS = 6;

    public static final int ENTITY = 7;

    public static final int ENTITIES = 8;

    public static final int NMTOKEN = 9;

    public static final int NMTOKENS = 10;

    private static final String[] valueTypeStrings = { null, "NMTOKEN", "NOTATION", "CDATA", "ID", "IDREF", "IDREFS", "ENTITY", "ENTITIES", "NMTOKEN", "NMTOKENS" };

    private static final String[] defaultTypeStrings = { null, "#IMPLIED", "#REQUIRED", "#FIXED" };

    String prefix, localName, qName;

    int valueType;

    int defaultType;

    String defaultValue;

    String[] possibleValues;

    public AttributeDefinition(String prefix, String localName, String qName, int valueType, String[] possibleValues, int defaultType, String defaultValue) {
        this.prefix = prefix;
        this.localName = localName;
        this.qName = qName;
        this.valueType = valueType;
        this.possibleValues = possibleValues;
        this.defaultType = defaultType;
        this.defaultValue = defaultValue;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLocalName() {
        return localName;
    }

    public String getQName() {
        return qName;
    }

    public int getValueType() {
        return valueType;
    }

    public String getValueTypeString() {
        return getValueTypeString(valueType);
    }

    public static String getValueTypeString(int valueType) {
        return valueTypeStrings[valueType];
    }

    public int getDefaultType() {
        return defaultType;
    }

    public String getDefaultTypeString() {
        return getDefaultTypeString(defaultType);
    }

    public static String getDefaultTypeString(int defaultType) {
        return defaultTypeStrings[defaultType];
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String[] getPossibleValues() {
        return possibleValues;
    }
}
