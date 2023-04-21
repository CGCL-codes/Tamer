package com.lowagie.text.html;

import java.util.TreeMap;
import java.util.Iterator;

/**
 * This class contains a series of attributes of a Tag.
 *
 * @author  bruno@lowagie.com
 * @version 0.02, 2000/02/02
 *
 * @since   iText0.30
 */
class HtmlAttributes extends TreeMap {

    /**
	 * Constructs this object.
	 *
	 * @since	iText0.39
	 * @author	Paulo Soares
	 */
    public HtmlAttributes() {
        super(new com.lowagie.text.html.StringCompare());
    }

    /**
	 * Shows all the attributes as a <CODE>String</CODE> that can be
	 * inserted into a tag.
	 *
	 * @return	a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        String key;
        String value;
        for (java.util.Iterator i = keySet().iterator(); i.hasNext(); ) {
            key = (String) i.next();
            value = (String) get(key);
            buffer.append(' ');
            buffer.append(key);
            if (value != null) {
                buffer.append('=');
                buffer.append('"');
                buffer.append(value);
                buffer.append('"');
            }
        }
        return buffer.toString();
    }
}
