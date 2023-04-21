package _2.ucc.ean.impl;

/**
 * An XML EntityIdentificationType(@urn:ean.ucc:2).
 *
 * This is a complex type.
 */
public class EntityIdentificationTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements _2.ucc.ean.EntityIdentificationType {

    public EntityIdentificationTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName UNIQUECREATORIDENTIFICATION$0 = new javax.xml.namespace.QName("", "uniqueCreatorIdentification");

    private static final javax.xml.namespace.QName CONTENTOWNER$2 = new javax.xml.namespace.QName("", "contentOwner");

    /**
     * Gets the "uniqueCreatorIdentification" element
     */
    public java.lang.String getUniqueCreatorIdentification() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(UNIQUECREATORIDENTIFICATION$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "uniqueCreatorIdentification" element
     */
    public _2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification xgetUniqueCreatorIdentification() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification target = null;
            target = (_2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification) get_store().find_element_user(UNIQUECREATORIDENTIFICATION$0, 0);
            return target;
        }
    }

    /**
     * Sets the "uniqueCreatorIdentification" element
     */
    public void setUniqueCreatorIdentification(java.lang.String uniqueCreatorIdentification) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(UNIQUECREATORIDENTIFICATION$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(UNIQUECREATORIDENTIFICATION$0);
            }
            target.setStringValue(uniqueCreatorIdentification);
        }
    }

    /**
     * Sets (as xml) the "uniqueCreatorIdentification" element
     */
    public void xsetUniqueCreatorIdentification(_2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification uniqueCreatorIdentification) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification target = null;
            target = (_2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification) get_store().find_element_user(UNIQUECREATORIDENTIFICATION$0, 0);
            if (target == null) {
                target = (_2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification) get_store().add_element_user(UNIQUECREATORIDENTIFICATION$0);
            }
            target.set(uniqueCreatorIdentification);
        }
    }

    /**
     * Gets the "contentOwner" element
     */
    public _2.ucc.ean.PartyIdentificationType getContentOwner() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.PartyIdentificationType target = null;
            target = (_2.ucc.ean.PartyIdentificationType) get_store().find_element_user(CONTENTOWNER$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "contentOwner" element
     */
    public void setContentOwner(_2.ucc.ean.PartyIdentificationType contentOwner) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.PartyIdentificationType target = null;
            target = (_2.ucc.ean.PartyIdentificationType) get_store().find_element_user(CONTENTOWNER$2, 0);
            if (target == null) {
                target = (_2.ucc.ean.PartyIdentificationType) get_store().add_element_user(CONTENTOWNER$2);
            }
            target.set(contentOwner);
        }
    }

    /**
     * Appends and returns a new empty "contentOwner" element
     */
    public _2.ucc.ean.PartyIdentificationType addNewContentOwner() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.PartyIdentificationType target = null;
            target = (_2.ucc.ean.PartyIdentificationType) get_store().add_element_user(CONTENTOWNER$2);
            return target;
        }
    }

    /**
     * An XML uniqueCreatorIdentification(@).
     *
     * This is an atomic type that is a restriction of _2.ucc.ean.EntityIdentificationType$UniqueCreatorIdentification.
     */
    public static class UniqueCreatorIdentificationImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements _2.ucc.ean.EntityIdentificationType.UniqueCreatorIdentification {

        public UniqueCreatorIdentificationImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType, false);
        }

        protected UniqueCreatorIdentificationImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
