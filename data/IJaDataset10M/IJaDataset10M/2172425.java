package de.abg.jreichert.serviceqos.calc.divider.serviceapi.impl;

/**
 * An XML divideResponse(@http://serviceapi.divider.calc.serviceqos.jreichert.abg.de/).
 *
 * This is a complex type.
 */
public class DivideResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse {

    public DivideResponseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName RETURN$0 = new javax.xml.namespace.QName("", "return");

    /**
     * Gets the "return" element
     */
    public int getReturn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(RETURN$0, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "return" element
     */
    public org.apache.xmlbeans.XmlInt xgetReturn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt) get_store().find_element_user(RETURN$0, 0);
            return target;
        }
    }

    /**
     * Sets the "return" element
     */
    public void setReturn(int xreturn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(RETURN$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(RETURN$0);
            }
            target.setIntValue(xreturn);
        }
    }

    /**
     * Sets (as xml) the "return" element
     */
    public void xsetReturn(org.apache.xmlbeans.XmlInt xreturn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt) get_store().find_element_user(RETURN$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt) get_store().add_element_user(RETURN$0);
            }
            target.set(xreturn);
        }
    }
}
