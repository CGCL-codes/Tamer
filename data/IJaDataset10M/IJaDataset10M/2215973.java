package org.ws4d.java.wsdl;

import org.ws4d.java.structures.ArrayList;
import org.ws4d.java.structures.DataStructure;
import org.ws4d.java.structures.EmptyStructures;
import org.ws4d.java.structures.HashMap;
import org.ws4d.java.structures.LinkedMap;
import org.ws4d.java.types.QName;
import org.ws4d.java.util.StringUtil;

/**
 * Implementation of the WSDL 1.1 Message.<br />
 * WSDL 1.1, 2.3 Messages
 */
public class WSDLMessage extends NamedItem {

    private WSDL wsdl;

    private HashMap parts;

    /**
	 * 
	 */
    public WSDLMessage() {
        super();
    }

    /**
	 * Creates a message with a given name.
	 * 
	 * @param name the name of the message.
	 */
    public WSDLMessage(QName name) {
        super(name);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(StringUtil.formatClassName(getClass()));
        sb.append("[ ");
        sb.append(super.toString());
        sb.append(", parts=").append(parts);
        sb.append(" ]");
        return sb.toString();
    }

    /**
	 * Adds a message part to this message.
	 * 
	 * @param part the message part to add.
	 */
    public void addPart(WSDLMessagePart part) {
        if (part == null) {
            return;
        }
        if (parts == null) {
            parts = new LinkedMap();
        }
        parts.put(part.getName(), part);
        part.setMessage(this);
    }

    /**
	 * @param name the local name of the part to return
	 * @return the named part or <code>null</code>
	 */
    public WSDLMessagePart getPart(String name) {
        return parts == null ? null : (WSDLMessagePart) parts.get(name);
    }

    /**
	 * Returns a <code>DatStructure</code> containing all the message parts from
	 * this message.
	 * 
	 * @return a <code>DatStructure</code> containing all the message parts from
	 *         this message.
	 */
    public DataStructure getParts() {
        return parts == null ? EmptyStructures.EMPTY_STRUCTURE : new ArrayList(parts.values());
    }

    /**
	 * @return the wsdl
	 */
    public WSDL getWsdl() {
        return wsdl;
    }

    /**
	 * @param wsdl the wsdl to set
	 */
    void setWsdl(WSDL wsdl) {
        this.wsdl = wsdl;
    }
}
