package com.trollworks.ttk.text;

import java.io.InputStream;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/** Provides simple XML parsing. */
public class XMLParser {

    private static final String SEPARATOR = " ";

    private XMLStreamReader mReader;

    private int mDepth;

    private String mMarker;

    /**
	 * Creates a new {@link XMLParser}.
	 * 
	 * @param stream The {@link InputStream} to read from.
	 */
    public XMLParser(InputStream stream) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        mReader = factory.createXMLStreamReader(stream);
    }

    /** @return The {@link Location}. */
    public Location getLocation() {
        return mReader.getLocation();
    }

    /** @return The current line:column position. */
    public String getLocationAsString() {
        Location location = mReader.getLocation();
        return location.getLineNumber() + ":" + location.getColumnNumber();
    }

    /** @return A marker for determining if you've come to the end of a specific tag. */
    public String getMarker() {
        return mMarker;
    }

    /** @return The current tag's name, or <code>null</code>. */
    public String getCurrentTag() {
        return mReader.getLocalName();
    }

    /**
	 * Advances to the next position.
	 * 
	 * @return The next tag's name, or <code>null</code>.
	 */
    public String nextTag() throws XMLStreamException {
        return nextTag(null);
    }

    /**
	 * Advances to the next position.
	 * 
	 * @param marker If this is not <code>null</code>, when an end tag matches this marker return
	 *            <code>null</code>.
	 * @return The next tag's name, or <code>null</code>.
	 */
    public String nextTag(String marker) throws XMLStreamException {
        while (mReader.hasNext()) {
            switch(mReader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = mReader.getLocalName();
                    mMarker = mDepth++ + SEPARATOR + name;
                    return name;
                case XMLStreamConstants.END_ELEMENT:
                    mMarker = --mDepth + SEPARATOR + mReader.getLocalName();
                    if (marker != null && marker.equals(mMarker)) {
                        return null;
                    }
                    break;
                case XMLStreamConstants.START_DOCUMENT:
                    mMarker = null;
                    if (marker != null) {
                        return null;
                    }
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    mMarker = null;
                    return null;
            }
        }
        return null;
    }

    /** Skips the end of the current tag, bypassing its children. */
    public void skip() throws XMLStreamException {
        skip(getMarker());
    }

    /** @param marker Up to the end of the tag this marker came from will be skipped. */
    public void skip(String marker) throws XMLStreamException {
        while (nextTag(marker) != null) {
        }
    }

    /**
	 * @param name The name of the attribute to check.
	 * @return Whether the attribute is present.
	 */
    public boolean hasAttribute(String name) {
        return getAttribute(name) != null;
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @return The attribute value, or <code>null</code>.
	 */
    public String getAttribute(String name) {
        return mReader.getAttributeValue(null, name);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present.
	 * @return The value of the attribute.
	 */
    public String getAttribute(String name, String def) {
        String value = getAttribute(name);
        return value != null ? value : def;
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @return Whether or not the attribute is present and set to a 'true' value.
	 */
    public boolean isAttributeSet(String name) {
        return Numbers.getBoolean(getAttribute(name));
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @return Whether or not the attribute is present and set to a 'true' value.
	 */
    public boolean isAttributeSet(String name, boolean def) {
        return Numbers.getBoolean(getAttribute(name, Boolean.toString(def)));
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @return The value of the attribute.
	 */
    public short getShortAttribute(String name) {
        return Numbers.getShort(getAttribute(name));
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @return The value of the attribute.
	 */
    public short getShortAttribute(String name, short def) {
        return Numbers.getShort(getAttribute(name), def);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public short getShortAttribute(String name, short def, short min, short max) {
        return Numbers.getShort(getAttribute(name), def, min, max);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param radix The radix to use during conversion.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public short getShortAttribute(String name, int radix, short def, short min, short max) {
        return Numbers.getShort(getAttribute(name), radix, def, min, max);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @return The value of the attribute.
	 */
    public int getIntegerAttribute(String name) {
        return Numbers.getInteger(getAttribute(name));
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @return The value of the attribute.
	 */
    public int getIntegerAttribute(String name, int def) {
        return Numbers.getInteger(getAttribute(name), def);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public int getIntegerAttribute(String name, int def, int min, int max) {
        return Numbers.getInteger(getAttribute(name), def, min, max);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param radix The radix to use during conversion.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public int getIntegerAttribute(String name, int radix, int def, int min, int max) {
        return Numbers.getInteger(getAttribute(name), radix, def, min, max);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @return The value of the attribute.
	 */
    public long getLongAttribute(String name) {
        return Numbers.getLong(getAttribute(name));
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @return The value of the attribute.
	 */
    public long getLongAttribute(String name, long def) {
        return Numbers.getLong(getAttribute(name), def);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public long getLongAttribute(String name, long def, long min, long max) {
        return Numbers.getLong(getAttribute(name), def, min, max);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param radix The radix to use during conversion.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public long getLongAttribute(String name, int radix, long def, long min, long max) {
        return Numbers.getLong(getAttribute(name), radix, def, min, max);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @return The value of the attribute.
	 */
    public float getFloatAttribute(String name) {
        return Numbers.getFloat(getAttribute(name));
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @return The value of the attribute.
	 */
    public float getFloatAttribute(String name, float def) {
        return Numbers.getFloat(getAttribute(name), def);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public float getFloatAttribute(String name, float def, float min, float max) {
        return Numbers.getFloat(getAttribute(name), def, min, max);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @return The value of the attribute.
	 */
    public double getDoubleAttribute(String name) {
        return Numbers.getDouble(getAttribute(name));
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @return The value of the attribute.
	 */
    public double getDoubleAttribute(String name, double def) {
        return Numbers.getDouble(getAttribute(name), def);
    }

    /**
	 * @param name The name of the attribute to retrieve.
	 * @param def The default value to use if the attribute value isn't present or cannot be
	 *            converted.
	 * @param min The minimum value permitted.
	 * @param max The maximum value permitted.
	 * @return The value of the attribute.
	 */
    public double getDoubleAttribute(String name, double def, double min, double max) {
        return Numbers.getDouble(getAttribute(name), def, min, max);
    }

    /** @return The number of attributes. */
    public int getAttributeCount() {
        return mReader.getAttributeCount();
    }

    /**
	 * @param index The index of the attribute.
	 * @return The attribute value.
	 */
    public String getAttributeName(int index) {
        return mReader.getAttributeLocalName(index);
    }

    /**
	 * @param index The index of the attribute.
	 * @return The attribute value.
	 */
    public String getAttributeValue(int index) {
        return mReader.getAttributeValue(index);
    }

    /** @return The text of the current element. */
    public String getText() throws XMLStreamException {
        String text = mReader.getElementText();
        mMarker = --mDepth + SEPARATOR + mReader.getLocalName();
        return text;
    }

    /** Closes this {@link XMLParser}. No further reading can be attempted with it. */
    public void close() throws XMLStreamException {
        mReader.close();
        mReader = null;
    }
}
