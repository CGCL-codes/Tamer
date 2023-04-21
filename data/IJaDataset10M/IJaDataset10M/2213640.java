package blue.utility;

import electric.xml.Element;

/**
 * Utilities for XML handling with EXML library
 * 
 * @author steven
 */
public class XMLUtilities {

    public static int readInt(Element data, String nodeName) {
        return Integer.parseInt(data.getTextString(nodeName));
    }

    public static int readInt(Element data) {
        return Integer.parseInt(data.getTextString());
    }

    public static float readFloat(Element data, String nodeName) {
        return Float.parseFloat(data.getTextString(nodeName));
    }

    public static float readFloat(Element data) {
        return Float.parseFloat(data.getTextString());
    }

    public static boolean readBoolean(Element data, String nodeName) {
        return Boolean.valueOf(data.getTextString(nodeName)).booleanValue();
    }

    public static boolean readBoolean(Element data) {
        return Boolean.valueOf(data.getTextString()).booleanValue();
    }

    public static double readDouble(Element data) {
        return Double.parseDouble(data.getTextString());
    }

    public static Element writeInt(String nodeName, int val) {
        Element elem = new Element(nodeName);
        elem.setText(Integer.toString(val));
        return elem;
    }

    public static Element writeFloat(String nodeName, float val) {
        Element elem = new Element(nodeName);
        elem.setText(Float.toString(val));
        return elem;
    }

    public static Element writeDouble(String nodeName, double val) {
        Element elem = new Element(nodeName);
        elem.setText(Double.toString(val));
        return elem;
    }

    public static Element writeBoolean(String nodeName, boolean val) {
        Element elem = new Element(nodeName);
        elem.setText(Boolean.toString(val));
        return elem;
    }
}
