package org.baselinetest;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Helper class for validating the attributes on a SAX element.
 * 
 * @author jwan
 */
abstract class SAXParserAttributeHelper {

    /**
     * Validate that the given attributes contains the appropriate attribute at
     * the given index.
     * 
     * @param attributes
     *            The attributes of the currently parsed XML element.
     * @param index
     *            the index of the attribute to be checked.
     * @param expectedAttributeName
     *            the expected attribute name.
     * 
     * @throws org.xml.sax.SAXException
     *             if the attribute name does not match the expected attribute
     *             name.
     */
    public static void checkForExpectedAttributeName(Attributes attributes, String expectedAttributeName) throws SAXException {
        int index = attributes.getIndex(expectedAttributeName);
        if (index == -1) {
            throw new SAXException("Expected (" + expectedAttributeName + ")");
        }
    }

    /**
     * Validate that the given attributes contains the appropriate attribute at
     * the given index.
     * 
     * @param attributes
     *            The attributes of the currently parsed XML element.
     * @param index
     *            the index of the attribute to be checked.
     * @param expectedAttributeName
     *            the expected attribute name.
     * 
     * @throws org.xml.sax.SAXException
     *             if the attribute name does not match the expected attribute
     *             name.
     */
    public static boolean checkForOptionalAttributeName(Attributes attributes, String attributeName) throws SAXException {
        return attributes.getIndex(attributeName) != -1;
    }

    /**
     * Validate that the given attributes contains the appropriate number of
     * attributes.
     * 
     * @param attrs
     *            The attributes of the currently parsed XML element.
     * @param minExpectedAttrs
     *            the min number of expected attributes.
     * 
     * @throws SAXException
     *             if the attribute name does not match the expected attribute
     *             name.
     */
    public static void checkForExpectedNumberOfAttributes(Attributes attrs, int minExpectedAttrs) throws SAXException {
        int numAttributes = attrs.getLength();
        if (numAttributes < minExpectedAttrs) {
            throw new SAXException("Expected at least(" + minExpectedAttrs + ") got (" + numAttributes + ")");
        }
    }
}
