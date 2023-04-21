package org.apache.xmlbeans.impl.xb.xsdschema.impl;

/**
 * A document containing one minInclusive(@http://www.w3.org/2001/XMLSchema) element.
 *
 * This is a complex type.
 */
public class MinInclusiveDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.xmlbeans.impl.xb.xsdschema.MinInclusiveDocument {

    public MinInclusiveDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName MININCLUSIVE$0 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "minInclusive");

    /**
     * Gets the "minInclusive" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.Facet getMinInclusive() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.Facet target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.Facet) get_store().find_element_user(MININCLUSIVE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "minInclusive" element
     */
    public void setMinInclusive(org.apache.xmlbeans.impl.xb.xsdschema.Facet minInclusive) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.Facet target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.Facet) get_store().find_element_user(MININCLUSIVE$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.impl.xb.xsdschema.Facet) get_store().add_element_user(MININCLUSIVE$0);
            }
            target.set(minInclusive);
        }
    }

    /**
     * Appends and returns a new empty "minInclusive" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.Facet addNewMinInclusive() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.Facet target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.Facet) get_store().add_element_user(MININCLUSIVE$0);
            return target;
        }
    }
}
