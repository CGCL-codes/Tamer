package com.skjegstad.soapoverudp.datatypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;

/**
 * Generic class for storing types that include "otherAttributes" generated
 * by JAXB.
 *
 * @author Magnus Skjegstad
 */
public class SOAPOverUDPGenericOtherAttributesType implements Cloneable {

    protected Map<QName, String> otherAttributes;

    public SOAPOverUDPGenericOtherAttributesType(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    public SOAPOverUDPGenericOtherAttributesType() {
    }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    @Override
    protected Object clone() {
        SOAPOverUDPGenericOtherAttributesType n = new SOAPOverUDPGenericOtherAttributesType();
        if (otherAttributes != null) {
            Map<QName, String> o = new HashMap<QName, String>();
            for (Entry<QName, String> a : otherAttributes.entrySet()) {
                QName nq = new QName(a.getKey().getNamespaceURI(), a.getKey().getLocalPart(), a.getKey().getPrefix());
                String ns = new String(a.getValue());
                o.put(nq, ns);
            }
            n.setOtherAttributes(o);
        }
        return n;
    }

    @Override
    public String toString() {
        String s = new String();
        if (otherAttributes != null) {
            for (Entry<QName, String> a : otherAttributes.entrySet()) {
                QName nq = new QName(a.getKey().getNamespaceURI(), a.getKey().getLocalPart(), a.getKey().getPrefix());
                String ns = new String(a.getValue());
                s += nq.toString() + "->" + ns;
            }
        } else s = "(null)";
        return "Other: " + s;
    }
}
