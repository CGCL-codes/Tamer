package ParsingFramework.MicroOperations;

import ParsingFramework.IParseOperations;

/**
 * Micro Parser for Parsing and Composing Integers from/to XML
 * data.
 * @author Paul Grace
 */
public class XMLBoolean {

    private static IParseOperations systemParser;

    public XMLBoolean(IParseOperations parent) {
        XMLBoolean.systemParser = parent;
    }

    /**
     * Covert a bit array to a Java int value
     * @param data The array of bits
     * @return a Java int
     */
    public boolean inXMLBoolean(String data) {
        if (data.equalsIgnoreCase("1")) return true; else return false;
    }

    /**
     *
     * @param Value
     * @param Size
     * @return
     */
    public String outXMLBoolean(Boolean Value, int y) {
        if (Value.booleanValue() == true) return "1"; else return "0";
    }
}
