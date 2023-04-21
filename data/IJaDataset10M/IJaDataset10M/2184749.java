package org.regilo.core.model.parsers;

import java.util.Date;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import redstone.xmlrpc.XmlRpcParser;

public abstract class AbstractParser extends XmlRpcParser {

    private boolean isFaultResponse;

    private Object returnValue;

    public boolean isFaultResponse() {
        return isFaultResponse;
    }

    protected void setFaultResponse(boolean isFaultResponse) {
        this.isFaultResponse = isFaultResponse;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    protected void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public void startElement(String uri, String name, String qualifiedName, Attributes attributes) throws SAXException {
        if (name.equals("fault")) {
            setFaultResponse(true);
        } else {
            super.startElement(uri, name, qualifiedName, attributes);
        }
    }

    /**
	 * Evaluate String as boolean value
	 * 
	 * @param text
	 * @return true if text is the string 1 or the string true
	 */
    public static boolean parseBoolean(String text) {
        if (text.equals("1") || text.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Evaluate Object as boolean value
	 * 
	 * @param text
	 * @return true if text is the string 1 or the string true
	 */
    public static boolean parseBoolean(Object text) {
        if (text instanceof Integer) {
            if ((Integer) text == 1) {
                return true;
            } else {
                return false;
            }
        } else if (text instanceof String) {
            return parseBoolean((String) text);
        } else {
            return false;
        }
    }

    /**
	 * Parse a String into an int
	 * 
	 * @param text
	 * @return int value of this text
	 */
    public static int parseInteger(String text) {
        return Integer.parseInt(text);
    }

    /**
	 * Parse a String representation of a epoch date into a Date object
	 * 
	 * @param text
	 * @return Date object representing a epoch value
	 */
    public static Date parseDate(String text) {
        long epoch = Long.parseLong(text);
        return new Date(epoch * 1000);
    }
}
