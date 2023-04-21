package _2.deliver.ucc.ean.impl;

/**
 * An XML InventoryItemLocationInformationType(@urn:ean.ucc:deliver:2).
 *
 * This is a complex type.
 */
public class InventoryItemLocationInformationTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements _2.deliver.ucc.ean.InventoryItemLocationInformationType {

    public InventoryItemLocationInformationTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName TRADEITEMIDENTIFICATION$0 = new javax.xml.namespace.QName("", "tradeItemIdentification");

    private static final javax.xml.namespace.QName INVENTORYLOCATION$2 = new javax.xml.namespace.QName("", "inventoryLocation");

    private static final javax.xml.namespace.QName INVENTORYACTIVITYLINEITEM$4 = new javax.xml.namespace.QName("", "inventoryActivityLineItem");

    private static final javax.xml.namespace.QName INVENTORYSTATUSLINEITEM$6 = new javax.xml.namespace.QName("", "inventoryStatusLineItem");

    /**
     * Gets the "tradeItemIdentification" element
     */
    public _2.ucc.ean.TradeItemIdentificationType getTradeItemIdentification() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.TradeItemIdentificationType target = null;
            target = (_2.ucc.ean.TradeItemIdentificationType) get_store().find_element_user(TRADEITEMIDENTIFICATION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "tradeItemIdentification" element
     */
    public void setTradeItemIdentification(_2.ucc.ean.TradeItemIdentificationType tradeItemIdentification) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.TradeItemIdentificationType target = null;
            target = (_2.ucc.ean.TradeItemIdentificationType) get_store().find_element_user(TRADEITEMIDENTIFICATION$0, 0);
            if (target == null) {
                target = (_2.ucc.ean.TradeItemIdentificationType) get_store().add_element_user(TRADEITEMIDENTIFICATION$0);
            }
            target.set(tradeItemIdentification);
        }
    }

    /**
     * Appends and returns a new empty "tradeItemIdentification" element
     */
    public _2.ucc.ean.TradeItemIdentificationType addNewTradeItemIdentification() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.TradeItemIdentificationType target = null;
            target = (_2.ucc.ean.TradeItemIdentificationType) get_store().add_element_user(TRADEITEMIDENTIFICATION$0);
            return target;
        }
    }

    /**
     * Gets the "inventoryLocation" element
     */
    public _2.ucc.ean.PartyIdentificationType getInventoryLocation() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.PartyIdentificationType target = null;
            target = (_2.ucc.ean.PartyIdentificationType) get_store().find_element_user(INVENTORYLOCATION$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "inventoryLocation" element
     */
    public void setInventoryLocation(_2.ucc.ean.PartyIdentificationType inventoryLocation) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.PartyIdentificationType target = null;
            target = (_2.ucc.ean.PartyIdentificationType) get_store().find_element_user(INVENTORYLOCATION$2, 0);
            if (target == null) {
                target = (_2.ucc.ean.PartyIdentificationType) get_store().add_element_user(INVENTORYLOCATION$2);
            }
            target.set(inventoryLocation);
        }
    }

    /**
     * Appends and returns a new empty "inventoryLocation" element
     */
    public _2.ucc.ean.PartyIdentificationType addNewInventoryLocation() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.PartyIdentificationType target = null;
            target = (_2.ucc.ean.PartyIdentificationType) get_store().add_element_user(INVENTORYLOCATION$2);
            return target;
        }
    }

    /**
     * Gets array of all "inventoryActivityLineItem" elements
     */
    public _2.deliver.ucc.ean.InventoryActivityLineItemType[] getInventoryActivityLineItemArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(INVENTORYACTIVITYLINEITEM$4, targetList);
            _2.deliver.ucc.ean.InventoryActivityLineItemType[] result = new _2.deliver.ucc.ean.InventoryActivityLineItemType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "inventoryActivityLineItem" element
     */
    public _2.deliver.ucc.ean.InventoryActivityLineItemType getInventoryActivityLineItemArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.deliver.ucc.ean.InventoryActivityLineItemType target = null;
            target = (_2.deliver.ucc.ean.InventoryActivityLineItemType) get_store().find_element_user(INVENTORYACTIVITYLINEITEM$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "inventoryActivityLineItem" element
     */
    public int sizeOfInventoryActivityLineItemArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(INVENTORYACTIVITYLINEITEM$4);
        }
    }

    /**
     * Sets array of all "inventoryActivityLineItem" element
     */
    public void setInventoryActivityLineItemArray(_2.deliver.ucc.ean.InventoryActivityLineItemType[] inventoryActivityLineItemArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(inventoryActivityLineItemArray, INVENTORYACTIVITYLINEITEM$4);
        }
    }

    /**
     * Sets ith "inventoryActivityLineItem" element
     */
    public void setInventoryActivityLineItemArray(int i, _2.deliver.ucc.ean.InventoryActivityLineItemType inventoryActivityLineItem) {
        synchronized (monitor()) {
            check_orphaned();
            _2.deliver.ucc.ean.InventoryActivityLineItemType target = null;
            target = (_2.deliver.ucc.ean.InventoryActivityLineItemType) get_store().find_element_user(INVENTORYACTIVITYLINEITEM$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(inventoryActivityLineItem);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "inventoryActivityLineItem" element
     */
    public _2.deliver.ucc.ean.InventoryActivityLineItemType insertNewInventoryActivityLineItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.deliver.ucc.ean.InventoryActivityLineItemType target = null;
            target = (_2.deliver.ucc.ean.InventoryActivityLineItemType) get_store().insert_element_user(INVENTORYACTIVITYLINEITEM$4, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "inventoryActivityLineItem" element
     */
    public _2.deliver.ucc.ean.InventoryActivityLineItemType addNewInventoryActivityLineItem() {
        synchronized (monitor()) {
            check_orphaned();
            _2.deliver.ucc.ean.InventoryActivityLineItemType target = null;
            target = (_2.deliver.ucc.ean.InventoryActivityLineItemType) get_store().add_element_user(INVENTORYACTIVITYLINEITEM$4);
            return target;
        }
    }

    /**
     * Removes the ith "inventoryActivityLineItem" element
     */
    public void removeInventoryActivityLineItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(INVENTORYACTIVITYLINEITEM$4, i);
        }
    }

    /**
     * Gets array of all "inventoryStatusLineItem" elements
     */
    public _2.ucc.ean.InventoryStatusLineItemType[] getInventoryStatusLineItemArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(INVENTORYSTATUSLINEITEM$6, targetList);
            _2.ucc.ean.InventoryStatusLineItemType[] result = new _2.ucc.ean.InventoryStatusLineItemType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "inventoryStatusLineItem" element
     */
    public _2.ucc.ean.InventoryStatusLineItemType getInventoryStatusLineItemArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.InventoryStatusLineItemType target = null;
            target = (_2.ucc.ean.InventoryStatusLineItemType) get_store().find_element_user(INVENTORYSTATUSLINEITEM$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "inventoryStatusLineItem" element
     */
    public int sizeOfInventoryStatusLineItemArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(INVENTORYSTATUSLINEITEM$6);
        }
    }

    /**
     * Sets array of all "inventoryStatusLineItem" element
     */
    public void setInventoryStatusLineItemArray(_2.ucc.ean.InventoryStatusLineItemType[] inventoryStatusLineItemArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(inventoryStatusLineItemArray, INVENTORYSTATUSLINEITEM$6);
        }
    }

    /**
     * Sets ith "inventoryStatusLineItem" element
     */
    public void setInventoryStatusLineItemArray(int i, _2.ucc.ean.InventoryStatusLineItemType inventoryStatusLineItem) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.InventoryStatusLineItemType target = null;
            target = (_2.ucc.ean.InventoryStatusLineItemType) get_store().find_element_user(INVENTORYSTATUSLINEITEM$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(inventoryStatusLineItem);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "inventoryStatusLineItem" element
     */
    public _2.ucc.ean.InventoryStatusLineItemType insertNewInventoryStatusLineItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.InventoryStatusLineItemType target = null;
            target = (_2.ucc.ean.InventoryStatusLineItemType) get_store().insert_element_user(INVENTORYSTATUSLINEITEM$6, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "inventoryStatusLineItem" element
     */
    public _2.ucc.ean.InventoryStatusLineItemType addNewInventoryStatusLineItem() {
        synchronized (monitor()) {
            check_orphaned();
            _2.ucc.ean.InventoryStatusLineItemType target = null;
            target = (_2.ucc.ean.InventoryStatusLineItemType) get_store().add_element_user(INVENTORYSTATUSLINEITEM$6);
            return target;
        }
    }

    /**
     * Removes the ith "inventoryStatusLineItem" element
     */
    public void removeInventoryStatusLineItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(INVENTORYSTATUSLINEITEM$6, i);
        }
    }
}
