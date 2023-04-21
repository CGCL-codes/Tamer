package com.nimkathana.swx.xmlrpc;

import waba.sys.*;
import waba.util.Vector;
import com.nimkathana.swx.util.*;
import superwaba.ext.xplat.xml.AttributeList;
import superwaba.ext.xplat.xml.ContentHandler;
import com.nimkathana.swx.xmlrpc.crypt.Blowfish;

/**
 * Methods here are called by the xml parser used in XmlRpcClient
 *@version 
 *	April 2004
 *@author
 * Nimkathana
 * (<a href="http://www.nimkathana.com">www.nimkathana.com</a>)
 */
public class XmlRpcContentHandler implements ContentHandler {

    private Vector values;

    private Object result;

    private Blowfish blowfish;

    private StringBuffer cdata;

    private XmlRpcValue currentValue;

    private boolean fault, readCdata, isStructName;

    public static final int VALUE = 8;

    public static final int MEMBER = 9;

    public static final int FAULT = 10;

    public static final int METHODNAME = 11;

    public static final int NAME = 12;

    public static final int I4 = 13;

    public XmlRpcContentHandler() {
        result = null;
        fault = false;
        blowfish = null;
        readCdata = false;
        currentValue = null;
        isStructName = false;
        values = new Vector();
        cdata = new StringBuffer(128);
    }

    /**
	 * Tells if a fault occurred during parsing
	 *@return
	 *	true if a fault occurred, false otherwise
	 */
    public boolean didFault() {
        return fault;
    }

    /**
	 * Gets the object unmarshalled from the last
	 * xml-rpc response parsing
	 *@return
	 *	The xml-rpc unmarshalled object
	 */
    public Object getResult() {
        return result;
    }

    /**
	 * Sets the Blowfish object holding the proper decryption key. 
	 * For when you are expecting Blowfish encrypted data from the server.
	 *@param blow
	 * The Blowfish object used to decrypt incoming data
	 */
    public void setDecryptor(Blowfish blow) {
        blowfish = blow;
    }

    public void comment(String s) {
        ;
    }

    public void characters(String chars) {
        if (readCdata) {
            if ((blowfish != null) && !isStructName && !fault) chars = blowfish.decrypt(chars);
            isStructName = false;
            cdata.append(chars);
        }
    }

    public void endElement(int tag) {
        if (currentValue != null && readCdata) {
            currentValue.characterData(cdata.toString());
            cdata.setLength(0);
            readCdata = false;
        }
        if (tag == VALUE) {
            int depth = values.size();
            if (depth < 2 || values.items[depth - 2].hashCode() != XmlRpcValue.STRUCT) {
                XmlRpcValue v = currentValue;
                values.pop();
                if (depth < 2) {
                    result = v.getValue();
                    currentValue = null;
                } else {
                    currentValue = (XmlRpcValue) values.peek();
                    currentValue.endElement(v);
                }
            }
        }
        if (tag == MEMBER) {
            XmlRpcValue v = currentValue;
            values.pop();
            currentValue = (XmlRpcValue) values.peek();
            currentValue.endElement(v);
        } else if (tag == METHODNAME) {
            cdata.setLength(0);
            readCdata = false;
        }
    }

    public void startElement(int tag, AttributeList atts) {
        switch(tag) {
            case XmlRpcValue.ARRAY:
            case XmlRpcValue.STRUCT:
                currentValue.setType(tag);
                break;
            case FAULT:
                fault = true;
                break;
            case NAME:
                isStructName = true;
            case METHODNAME:
            case XmlRpcValue.STRING:
                cdata.setLength(0);
                readCdata = true;
                break;
            case I4:
                tag = XmlRpcValue.INTEGER;
            case XmlRpcValue.DATE:
            case XmlRpcValue.BASE64:
            case XmlRpcValue.DOUBLE:
            case XmlRpcValue.BOOLEAN:
            case XmlRpcValue.INTEGER:
                currentValue.setType(tag);
                cdata.setLength(0);
                readCdata = true;
                break;
            case VALUE:
                XmlRpcValue v = new XmlRpcValue();
                values.push(v);
                currentValue = v;
                cdata.setLength(0);
                readCdata = true;
                break;
            default:
                break;
        }
    }
}
