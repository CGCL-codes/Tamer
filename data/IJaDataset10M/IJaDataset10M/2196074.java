package noNamespace.impl;

/**
 * An XML HostMsgListType(@).
 *
 * This is a complex type.
 */
public class HostMsgListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements noNamespace.HostMsgListType {

    private static final long serialVersionUID = 1L;

    public HostMsgListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName HOSTMSG$0 = new javax.xml.namespace.QName("", "HostMsg");

    /**
     * Gets array of all "HostMsg" elements
     */
    public java.lang.String[] getHostMsgArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(HOSTMSG$0, targetList);
            java.lang.String[] result = new java.lang.String[targetList.size()];
            for (int i = 0, len = targetList.size(); i < len; i++) result[i] = ((org.apache.xmlbeans.SimpleValue) targetList.get(i)).getStringValue();
            return result;
        }
    }

    /**
     * Gets ith "HostMsg" element
     */
    public java.lang.String getHostMsgArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(HOSTMSG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) array of all "HostMsg" elements
     */
    public org.apache.xmlbeans.XmlString[] xgetHostMsgArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(HOSTMSG$0, targetList);
            org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets (as xml) ith "HostMsg" element
     */
    public org.apache.xmlbeans.XmlString xgetHostMsgArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(HOSTMSG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return (org.apache.xmlbeans.XmlString) target;
        }
    }

    /**
     * Returns number of "HostMsg" element
     */
    public int sizeOfHostMsgArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(HOSTMSG$0);
        }
    }

    /**
     * Sets array of all "HostMsg" element
     */
    public void setHostMsgArray(java.lang.String[] hostMsgArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(hostMsgArray, HOSTMSG$0);
        }
    }

    /**
     * Sets ith "HostMsg" element
     */
    public void setHostMsgArray(int i, java.lang.String hostMsg) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(HOSTMSG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(hostMsg);
        }
    }

    /**
     * Sets (as xml) array of all "HostMsg" element
     */
    public void xsetHostMsgArray(org.apache.xmlbeans.XmlString[] hostMsgArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(hostMsgArray, HOSTMSG$0);
        }
    }

    /**
     * Sets (as xml) ith "HostMsg" element
     */
    public void xsetHostMsgArray(int i, org.apache.xmlbeans.XmlString hostMsg) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(HOSTMSG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(hostMsg);
        }
    }

    /**
     * Inserts the value as the ith "HostMsg" element
     */
    public void insertHostMsg(int i, java.lang.String hostMsg) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = (org.apache.xmlbeans.SimpleValue) get_store().insert_element_user(HOSTMSG$0, i);
            target.setStringValue(hostMsg);
        }
    }

    /**
     * Appends the value as the last "HostMsg" element
     */
    public void addHostMsg(java.lang.String hostMsg) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(HOSTMSG$0);
            target.setStringValue(hostMsg);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "HostMsg" element
     */
    public org.apache.xmlbeans.XmlString insertNewHostMsg(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().insert_element_user(HOSTMSG$0, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "HostMsg" element
     */
    public org.apache.xmlbeans.XmlString addNewHostMsg() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(HOSTMSG$0);
            return target;
        }
    }

    /**
     * Removes the ith "HostMsg" element
     */
    public void removeHostMsg(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(HOSTMSG$0, i);
        }
    }
}
