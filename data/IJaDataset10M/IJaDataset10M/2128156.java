package biz.sfsservices.ebiz;

/**
 * Handling-Classes: Attributes of a XML-Node
 * 
 * @author pire, bens, aben
 */
public class XMLAttribute {

    private String name;

    private String value;

    /**
     *
     * @param name transfer of the XML-Node name
     */
    public XMLAttribute(String name) {
        this.name = name;
        this.value = "";
    }

    /**
     *
     * @param name transfer of the XML-Node name
     * @param value transfer of the XML-Node value
     */
    public XMLAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * makes a new XMLAttribute-Array based on an attribute-String
     * @param attributeString: attributes as string in that format: "aa='10' bb='20'",
     *         accepts " and ' as Value-Delimiter
     * @return array of Attributes or null on error
     */
    public static XMLAttribute[] makeAttributeList(String attributeString) {
        if (attributeString == null) return null;
        attributeString = attributeString.trim();
        if (attributeString.equals("")) return null;
        return makeAttributeList((XMLAttribute[]) null, attributeString);
    }

    public static XMLAttribute[] makeAttributeList(XMLAttribute[] oldList, String attributeString) {
        if (attributeString == null) return oldList;
        attributeString = attributeString.trim();
        if (attributeString.equals("")) return oldList;
        int pos = attributeString.indexOf("=");
        if (pos < 0) return oldList;
        String attrName = attributeString.substring(0, pos).trim();
        String helper = attributeString.substring(pos + 1).trim();
        char endChar = helper.charAt(0);
        if (endChar != '"' && endChar != '\'') return oldList;
        pos = helper.indexOf(endChar, 1);
        if (pos < 0) return oldList;
        String value = helper.substring(1, pos);
        helper = helper.substring(pos + 1).trim();
        XMLAttribute[] newList = addAttributeToList(oldList, attrName, value);
        return makeAttributeList(newList, helper);
    }

    /**
     * adds a new attribute-element to an existing list
     * @param oldList old list (may be "null" if old list is empty)
     * @param newName name of new attribute
     * @param newValue value of new attribute
     * @return the new list 
     */
    public static XMLAttribute[] addAttributeToList(XMLAttribute[] oldList, String newName, String newValue) {
        int oldLen = 0;
        if (oldList != null) oldLen = oldList.length;
        XMLAttribute[] newList = new XMLAttribute[oldLen + 1];
        for (int i = 0; i < oldLen; i++) newList[i] = oldList[i];
        newList[oldLen] = new XMLAttribute(newName, newValue);
        return newList;
    }

    /**
     *
     * @param name transfer of the XML-Node name
     * @param value transfer of the XML-Node value
     * @return give a list with the XML Attributes back
     */
    public static XMLAttribute[] makeAttributeList(String name, String value) {
        XMLAttribute[] list = new XMLAttribute[1];
        list[0] = new XMLAttribute(name, value);
        return list;
    }

    /**
     *
     * @return give the XML-Node name and the value as String back
     */
    public String toString() {
        return name + "=" + '"' + value + '"';
    }
}
