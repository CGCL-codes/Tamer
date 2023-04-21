package de.abg.jreichert.serviceqos.calc.divider.serviceapi.impl;

/**
 * A document containing one divideResponse(@http://serviceapi.divider.calc.serviceqos.jreichert.abg.de/) element.
 *
 * This is a complex type.
 */
public class DivideResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponseDocument {

    public DivideResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName DIVIDERESPONSE$0 = new javax.xml.namespace.QName("http://serviceapi.divider.calc.serviceqos.jreichert.abg.de/", "divideResponse");

    /**
     * Gets the "divideResponse" element
     */
    public de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse getDivideResponse() {
        synchronized (monitor()) {
            check_orphaned();
            de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse target = null;
            target = (de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse) get_store().find_element_user(DIVIDERESPONSE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "divideResponse" element
     */
    public void setDivideResponse(de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse divideResponse) {
        synchronized (monitor()) {
            check_orphaned();
            de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse target = null;
            target = (de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse) get_store().find_element_user(DIVIDERESPONSE$0, 0);
            if (target == null) {
                target = (de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse) get_store().add_element_user(DIVIDERESPONSE$0);
            }
            target.set(divideResponse);
        }
    }

    /**
     * Appends and returns a new empty "divideResponse" element
     */
    public de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse addNewDivideResponse() {
        synchronized (monitor()) {
            check_orphaned();
            de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse target = null;
            target = (de.abg.jreichert.serviceqos.calc.divider.serviceapi.DivideResponse) get_store().add_element_user(DIVIDERESPONSE$0);
            return target;
        }
    }
}
