package noNamespace.impl;

/**
 * An XML Customer(@).
 *
 * This is a complex type.
 */
public class CustomerImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements noNamespace.Customer {

    private static final long serialVersionUID = 1L;

    public CustomerImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName IPADDRESS$0 = new javax.xml.namespace.QName("", "IPAddress");

    /**
     * Gets the "IPAddress" element
     */
    public java.lang.String getIPAddress() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(IPADDRESS$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "IPAddress" element
     */
    public org.apache.xmlbeans.XmlString xgetIPAddress() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(IPADDRESS$0, 0);
            return target;
        }
    }

    /**
     * True if has "IPAddress" element
     */
    public boolean isSetIPAddress() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(IPADDRESS$0) != 0;
        }
    }

    /**
     * Sets the "IPAddress" element
     */
    public void setIPAddress(java.lang.String ipAddress) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(IPADDRESS$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(IPADDRESS$0);
            }
            target.setStringValue(ipAddress);
        }
    }

    /**
     * Sets (as xml) the "IPAddress" element
     */
    public void xsetIPAddress(org.apache.xmlbeans.XmlString ipAddress) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(IPADDRESS$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(IPADDRESS$0);
            }
            target.set(ipAddress);
        }
    }

    /**
     * Unsets the "IPAddress" element
     */
    public void unsetIPAddress() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(IPADDRESS$0, 0);
        }
    }
}
