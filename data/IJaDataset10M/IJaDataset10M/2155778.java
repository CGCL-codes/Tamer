package tr.com.srdc.www.manufacturer.impl;

/**
 * A document containing one phMessageResp(@http://www.srdc.com.tr/manufacturer/) element.
 *
 * This is a complex type.
 */
public class PhMessageRespDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements tr.com.srdc.www.manufacturer.PhMessageRespDocument {

    public PhMessageRespDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName PHMESSAGERESP$0 = new javax.xml.namespace.QName("http://www.srdc.com.tr/manufacturer/", "phMessageResp");

    /**
     * Gets the "phMessageResp" element
     */
    public tr.com.srdc.www.manufacturer.ResponseType getPhMessageResp() {
        synchronized (monitor()) {
            check_orphaned();
            tr.com.srdc.www.manufacturer.ResponseType target = null;
            target = (tr.com.srdc.www.manufacturer.ResponseType) get_store().find_element_user(PHMESSAGERESP$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "phMessageResp" element
     */
    public void setPhMessageResp(tr.com.srdc.www.manufacturer.ResponseType phMessageResp) {
        synchronized (monitor()) {
            check_orphaned();
            tr.com.srdc.www.manufacturer.ResponseType target = null;
            target = (tr.com.srdc.www.manufacturer.ResponseType) get_store().find_element_user(PHMESSAGERESP$0, 0);
            if (target == null) {
                target = (tr.com.srdc.www.manufacturer.ResponseType) get_store().add_element_user(PHMESSAGERESP$0);
            }
            target.set(phMessageResp);
        }
    }

    /**
     * Appends and returns a new empty "phMessageResp" element
     */
    public tr.com.srdc.www.manufacturer.ResponseType addNewPhMessageResp() {
        synchronized (monitor()) {
            check_orphaned();
            tr.com.srdc.www.manufacturer.ResponseType target = null;
            target = (tr.com.srdc.www.manufacturer.ResponseType) get_store().add_element_user(PHMESSAGERESP$0);
            return target;
        }
    }
}
