package ParsingFramework.MicroOperations;

import ParsingFramework.IParseOperations;

/**
 * Micro Parser for Parsing and Composing Integers from/to XML
 * data.
 * @author Paul Grace
 */
public class XMLLong {

    private static IParseOperations systemParser;

    public XMLLong(IParseOperations parent) {
        XMLLong.systemParser = parent;
    }

    /**
     * Covert a bit array to a Java int value
     * @param data The array of bits
     * @return a Java int
     */
    public long inXMLLong(String data) {
        return Long.valueOf(data);
    }

    /**
     *
     * @param Value
     * @param Size
     * @return
     */
    public String outXMLLong(Long Value, int val) {
        return Value.toString();
    }
}
