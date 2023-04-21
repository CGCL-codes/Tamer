package sds.base;

/** Info about an XML subelement. */
public class XML_SubElemInfo {

    /** The only allowed constructor. */
    public XML_SubElemInfo(String theName, XML_SubElemType theType, int theOffset) {
        name = theName;
        type = theType;
        offset = theOffset;
    }

    /** The subelement's name. */
    public String name;

    /** The subelement's type. */
    public XML_SubElemType type;

    /** Index into XML_Parseable.children. */
    public int offset;
}

;
