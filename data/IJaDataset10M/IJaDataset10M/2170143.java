package com.nimkathana.swx.xmlrpc;

import waba.util.Vector;
import waba.sys.Convert;
import waba.util.Hashtable;
import com.nimkathana.swx.util.*;

/**
 * Used to represent an XML-RPC value while a request is being parsed.
 *@version 
 *	April 2004
 *@author
 * Maintained by Nimkathana
 * (<a href="http://www.nimkathana.com">www.nimkathana.com</a>)
 *@author
 * Original by IOP GmbH 
 * (<a href="http://www.iop.de">www.iop.de</a>)
 */
public class XmlRpcValue {

    private static final String types[] = { "String", "SwxInteger", "SwxBoolean", "Double", "Date", "Base64", "Struct", "Array" };

    public static final int STRING = 0;

    public static final int INTEGER = 1;

    public static final int BOOLEAN = 2;

    public static final int DOUBLE = 3;

    public static final int DATE = 4;

    public static final int BASE64 = 5;

    public static final int STRUCT = 6;

    public static final int ARRAY = 7;

    private int type;

    private Vector array;

    private Object value;

    private Hashtable struct;

    private String nextMemberName;

    public XmlRpcValue() {
        this.type = STRING;
    }

    public String toString() {
        return (types[type] + " element " + value);
    }

    public int hashCode() {
        return type;
    }

    /**
	 * Gets the corresponding object of this XmlRpcValue
	 *@return
	 * This XmlRpcValue's object
	 */
    public Object getValue() {
        return value;
    }

    /**
     * Notification that a new child element has been parsed.
	 *@param child
	 * The child that was parsed
     */
    public void endElement(XmlRpcValue child) {
        if (type == ARRAY) array.addElement(child.value); else if (type == STRUCT) struct.put(nextMemberName, child.value);
    }

    /**
     * Set the type of this value. 
	 * If it's a container, this creates the corresponding java container.
	 *@param type
	 * One of this class' public fields 
     */
    public void setType(int type) {
        this.type = type;
        if (type == ARRAY) value = array = new Vector();
        if (type == STRUCT) value = struct = new Hashtable(4);
    }

    /**
     * Set the character data for the element and 
	 * interprets it according to the element type
	 *@param cdata
	 * The character data to set and interpret
     */
    public void characterData(String cdata) {
        switch(type) {
            case INTEGER:
                value = new SwxInteger(Convert.toInt(cdata.trim()));
                break;
            case BOOLEAN:
                value = new SwxBoolean("1".equals(cdata.trim()));
                break;
            case DOUBLE:
                value = new SwxFloat(Convert.toFloat(cdata.trim()));
                break;
            case DATE:
                value = new Iso8601DateTimeConverter().parse(cdata.trim());
                break;
            case BASE64:
                value = Base64.decode(cdata);
                break;
            case STRING:
                value = cdata;
                break;
            case STRUCT:
                nextMemberName = cdata;
                break;
        }
    }
}
