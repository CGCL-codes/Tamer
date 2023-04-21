package _2.plan.ucc.ean.impl;

/**
 * An XML TradeItemLocationProfileType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public class TradeItemLocationProfileTypeImpl extends _2.plan.ucc.ean.impl.PlanDocumentTypeImpl implements _2.plan.ucc.ean.TradeItemLocationProfileType {

    public TradeItemLocationProfileTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName PROFILESTATUS$0 = new javax.xml.namespace.QName("", "profileStatus");

    private static final javax.xml.namespace.QName ITEMMANAGEMENTPROFILE$2 = new javax.xml.namespace.QName("", "itemManagementProfile");

    private static final javax.xml.namespace.QName EXTENSION$4 = new javax.xml.namespace.QName("", "extension");

    /**
     * Gets the "profileStatus" element
     */
    public _2.plan.ucc.ean.ProfileStatusCodeListType.Enum getProfileStatus() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(PROFILESTATUS$0, 0);
            if (target == null) {
                return null;
            }
            return (_2.plan.ucc.ean.ProfileStatusCodeListType.Enum) target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "profileStatus" element
     */
    public _2.plan.ucc.ean.ProfileStatusCodeListType xgetProfileStatus() {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ProfileStatusCodeListType target = null;
            target = (_2.plan.ucc.ean.ProfileStatusCodeListType) get_store().find_element_user(PROFILESTATUS$0, 0);
            return target;
        }
    }

    /**
     * Sets the "profileStatus" element
     */
    public void setProfileStatus(_2.plan.ucc.ean.ProfileStatusCodeListType.Enum profileStatus) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(PROFILESTATUS$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(PROFILESTATUS$0);
            }
            target.setEnumValue(profileStatus);
        }
    }

    /**
     * Sets (as xml) the "profileStatus" element
     */
    public void xsetProfileStatus(_2.plan.ucc.ean.ProfileStatusCodeListType profileStatus) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ProfileStatusCodeListType target = null;
            target = (_2.plan.ucc.ean.ProfileStatusCodeListType) get_store().find_element_user(PROFILESTATUS$0, 0);
            if (target == null) {
                target = (_2.plan.ucc.ean.ProfileStatusCodeListType) get_store().add_element_user(PROFILESTATUS$0);
            }
            target.set(profileStatus);
        }
    }

    /**
     * Gets array of all "itemManagementProfile" elements
     */
    public _2.plan.ucc.ean.ItemManagementProfileType[] getItemManagementProfileArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ITEMMANAGEMENTPROFILE$2, targetList);
            _2.plan.ucc.ean.ItemManagementProfileType[] result = new _2.plan.ucc.ean.ItemManagementProfileType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "itemManagementProfile" element
     */
    public _2.plan.ucc.ean.ItemManagementProfileType getItemManagementProfileArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ItemManagementProfileType target = null;
            target = (_2.plan.ucc.ean.ItemManagementProfileType) get_store().find_element_user(ITEMMANAGEMENTPROFILE$2, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "itemManagementProfile" element
     */
    public int sizeOfItemManagementProfileArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(ITEMMANAGEMENTPROFILE$2);
        }
    }

    /**
     * Sets array of all "itemManagementProfile" element
     */
    public void setItemManagementProfileArray(_2.plan.ucc.ean.ItemManagementProfileType[] itemManagementProfileArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(itemManagementProfileArray, ITEMMANAGEMENTPROFILE$2);
        }
    }

    /**
     * Sets ith "itemManagementProfile" element
     */
    public void setItemManagementProfileArray(int i, _2.plan.ucc.ean.ItemManagementProfileType itemManagementProfile) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ItemManagementProfileType target = null;
            target = (_2.plan.ucc.ean.ItemManagementProfileType) get_store().find_element_user(ITEMMANAGEMENTPROFILE$2, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(itemManagementProfile);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "itemManagementProfile" element
     */
    public _2.plan.ucc.ean.ItemManagementProfileType insertNewItemManagementProfile(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ItemManagementProfileType target = null;
            target = (_2.plan.ucc.ean.ItemManagementProfileType) get_store().insert_element_user(ITEMMANAGEMENTPROFILE$2, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "itemManagementProfile" element
     */
    public _2.plan.ucc.ean.ItemManagementProfileType addNewItemManagementProfile() {
        synchronized (monitor()) {
            check_orphaned();
            _2.plan.ucc.ean.ItemManagementProfileType target = null;
            target = (_2.plan.ucc.ean.ItemManagementProfileType) get_store().add_element_user(ITEMMANAGEMENTPROFILE$2);
            return target;
        }
    }

    /**
     * Removes the ith "itemManagementProfile" element
     */
    public void removeItemManagementProfile(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ITEMMANAGEMENTPROFILE$2, i);
        }
    }

    /**
     * Gets the "extension" element
     */
    public _2.ucc.ean.ExtensionType getExtension() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.ExtensionType target = null;
            target = (_2.ucc.ean.ExtensionType) get_store().find_element_user(EXTENSION$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * True if has "extension" element
     */
    public boolean isSetExtension() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(EXTENSION$4) != 0;
        }
    }

    /**
     * Sets the "extension" element
     */
    public void setExtension(_2.ucc.ean.ExtensionType extension) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.ExtensionType target = null;
            target = (_2.ucc.ean.ExtensionType) get_store().find_element_user(EXTENSION$4, 0);
            if (target == null) {
                target = (_2.ucc.ean.ExtensionType) get_store().add_element_user(EXTENSION$4);
            }
            target.set(extension);
        }
    }

    /**
     * Appends and returns a new empty "extension" element
     */
    public _2.ucc.ean.ExtensionType addNewExtension() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.ExtensionType target = null;
            target = (_2.ucc.ean.ExtensionType) get_store().add_element_user(EXTENSION$4);
            return target;
        }
    }

    /**
     * Unsets the "extension" element
     */
    public void unsetExtension() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTENSION$4, 0);
        }
    }
}
