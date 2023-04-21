package _2.ucc.ean.impl;

/**
 * An XML MultiLongDescriptionType(@urn:ean.ucc:2).
 *
 * This is a complex type.
 */
public class MultiLongDescriptionTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements _2.ucc.ean.MultiLongDescriptionType {

    public MultiLongDescriptionTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName DESCRIPTION$0 = new javax.xml.namespace.QName("", "description");

    /**
     * Gets array of all "description" elements
     */
    public _2.ucc.ean.LongDescriptionType[] getDescriptionArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DESCRIPTION$0, targetList);
            _2.ucc.ean.LongDescriptionType[] result = new _2.ucc.ean.LongDescriptionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "description" element
     */
    public _2.ucc.ean.LongDescriptionType getDescriptionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.LongDescriptionType target = null;
            target = (_2.ucc.ean.LongDescriptionType) get_store().find_element_user(DESCRIPTION$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "description" element
     */
    public int sizeOfDescriptionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(DESCRIPTION$0);
        }
    }

    /**
     * Sets array of all "description" element
     */
    public void setDescriptionArray(_2.ucc.ean.LongDescriptionType[] descriptionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(descriptionArray, DESCRIPTION$0);
        }
    }

    /**
     * Sets ith "description" element
     */
    public void setDescriptionArray(int i, _2.ucc.ean.LongDescriptionType description) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.LongDescriptionType target = null;
            target = (_2.ucc.ean.LongDescriptionType) get_store().find_element_user(DESCRIPTION$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(description);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "description" element
     */
    public _2.ucc.ean.LongDescriptionType insertNewDescription(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.LongDescriptionType target = null;
            target = (_2.ucc.ean.LongDescriptionType) get_store().insert_element_user(DESCRIPTION$0, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "description" element
     */
    public _2.ucc.ean.LongDescriptionType addNewDescription() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.LongDescriptionType target = null;
            target = (_2.ucc.ean.LongDescriptionType) get_store().add_element_user(DESCRIPTION$0);
            return target;
        }
    }

    /**
     * Removes the ith "description" element
     */
    public void removeDescription(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DESCRIPTION$0, i);
        }
    }
}
