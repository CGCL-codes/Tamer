package com.itextpdf.text.xml.xmp;

import java.util.ArrayList;
import com.itextpdf.text.xml.XMLUtil;

/**
 * StringBuffer to construct an XMP array.
 */
public class XmpArray extends ArrayList<String> {

    private static final long serialVersionUID = 5722854116328732742L;

    /** An array that is unordered. */
    public static final String UNORDERED = "rdf:Bag";

    /** An array that is ordered. */
    public static final String ORDERED = "rdf:Seq";

    /** An array with alternatives. */
    public static final String ALTERNATIVE = "rdf:Alt";

    /** the type of array. */
    protected String type;

    /**
	 * Creates an XmpArray.
	 * @param type the type of array: UNORDERED, ORDERED or ALTERNATIVE.
	 */
    public XmpArray(String type) {
        this.type = type;
    }

    /**
	 * Returns the String representation of the XmpArray.
	 * @return a String representation
	 */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer("<");
        buf.append(type);
        buf.append('>');
        String s;
        for (String string : this) {
            s = string;
            buf.append("<rdf:li>");
            buf.append(XMLUtil.escapeXML(s, false));
            buf.append("</rdf:li>");
        }
        buf.append("</");
        buf.append(type);
        buf.append('>');
        return buf.toString();
    }
}
