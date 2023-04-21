package tr.com.srdc.www.retailer.impl;

/**
 * A document containing one tiirMessage(@http://www.srdc.com.tr/retailer/) element.
 *
 * This is a complex type.
 */
public class TiirMessageDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements tr.com.srdc.www.retailer.TiirMessageDocument {

    public TiirMessageDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName TIIRMESSAGE$0 = new javax.xml.namespace.QName("http://www.srdc.com.tr/retailer/", "tiirMessage");

    /**
     * Gets the "tiirMessage" element
     */
    public org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 getTiirMessage() {
        synchronized (monitor()) {
            check_orphaned();
            org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 target = null;
            target = (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1) get_store().find_element_user(TIIRMESSAGE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "tiirMessage" element
     */
    public void setTiirMessage(org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 tiirMessage) {
        synchronized (monitor()) {
            check_orphaned();
            org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 target = null;
            target = (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1) get_store().find_element_user(TIIRMESSAGE$0, 0);
            if (target == null) {
                target = (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1) get_store().add_element_user(TIIRMESSAGE$0);
            }
            target.set(tiirMessage);
        }
    }

    /**
     * Appends and returns a new empty "tiirMessage" element
     */
    public org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 addNewTiirMessage() {
        synchronized (monitor()) {
            check_orphaned();
            org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 target = null;
            target = (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1) get_store().add_element_user(TIIRMESSAGE$0);
            return target;
        }
    }
}
