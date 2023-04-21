package org.mcbrooks.twitter.api;

import org.dom4j.Element;

/**
 * Helper class to deal with element and attribute values from an xml document
 * returns null if values are not present
 * 
 * @author mbrooks
 */
public abstract class XmlParser {

    /** parses a Float from an element attribute, returning null if not present */
    public static Float parseFloatAttribute(Element elem, String attr) {
        Float num = null;
        if (elem != null && elem.attribute(attr) != null) {
            num = Float.parseFloat(elem.attribute(attr).getStringValue());
        }
        return num;
    }

    /** parses a Float from an element value, returning null if not present */
    public static Float parseFloatElementData(Element elem) {
        Float num = null;
        if (elem != null) {
            num = Float.parseFloat(elem.getStringValue());
        }
        return num;
    }

    /** parses an Integer from an element attribute, returning null if not present */
    public static Integer parseIntAttribute(Element elem, String attr) {
        Integer num = null;
        if (elem != null && elem.attribute(attr) != null) {
            String str = elem.attribute(attr).getStringValue();
            if (!str.isEmpty()) {
                num = Integer.parseInt(str);
            }
        }
        return num;
    }

    /** parses an Integer from an element value, returning null if not present */
    public static Integer parseIntElementData(Element elem) {
        Integer num = null;
        if (elem != null) {
            String str = elem.getStringValue();
            if (!str.isEmpty()) {
                num = Integer.parseInt(str);
            }
        }
        return num;
    }

    /** parses a String from an element attribute, returning null if not present */
    public static String parseStringAttribute(Element elem, String attr) {
        String str = null;
        if (elem != null && elem.attribute(attr) != null) {
            str = elem.attribute(attr).getStringValue();
        }
        return str;
    }

    /** parses a String from an element value, returning null if not present */
    public static String parseStringElementData(Element elem) {
        String str = null;
        if (elem != null) {
            str = elem.getStringValue();
        }
        return str;
    }

    /**
   * Parses a boolean from an element
   * 
   * @param elem
   * @return boolean value
   */
    public static boolean parseBooleanElement(Element elem) {
        boolean bool = false;
        if (elem != null) {
            bool = new Boolean(elem.getStringValue()).booleanValue();
        }
        return bool;
    }
}
