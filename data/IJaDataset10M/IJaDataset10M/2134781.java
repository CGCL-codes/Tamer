package noNamespace.impl;

/**
 * An XML RequestTransaction(@).
 *
 * This is a complex type.
 */
public class RequestTransactionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements noNamespace.RequestTransaction {

    private static final long serialVersionUID = 1L;

    public RequestTransactionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName TYPE$0 = new javax.xml.namespace.QName("", "Type");

    private static final javax.xml.namespace.QName INSTALLMENTCNT$2 = new javax.xml.namespace.QName("", "InstallmentCnt");

    private static final javax.xml.namespace.QName AMOUNT$4 = new javax.xml.namespace.QName("", "Amount");

    private static final javax.xml.namespace.QName CURRENCYCODE$6 = new javax.xml.namespace.QName("", "CurrencyCode");

    private static final javax.xml.namespace.QName ORIGINALRETREFNUM$8 = new javax.xml.namespace.QName("", "OriginalRetrefNum");

    private static final javax.xml.namespace.QName CARDHOLDERPRESENTCODE$10 = new javax.xml.namespace.QName("", "CardholderPresentCode");

    private static final javax.xml.namespace.QName MOTOIND$12 = new javax.xml.namespace.QName("", "MotoInd");

    private static final javax.xml.namespace.QName SECURE3D$14 = new javax.xml.namespace.QName("", "Secure3D");

    /**
     * Gets the "Type" element
     */
    public java.lang.String getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(TYPE$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Type" element
     */
    public org.apache.xmlbeans.XmlString xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(TYPE$0, 0);
            return target;
        }
    }

    /**
     * True if has "Type" element
     */
    public boolean isSetType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(TYPE$0) != 0;
        }
    }

    /**
     * Sets the "Type" element
     */
    public void setType(java.lang.String type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(TYPE$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(TYPE$0);
            }
            target.setStringValue(type);
        }
    }

    /**
     * Sets (as xml) the "Type" element
     */
    public void xsetType(org.apache.xmlbeans.XmlString type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(TYPE$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(TYPE$0);
            }
            target.set(type);
        }
    }

    /**
     * Unsets the "Type" element
     */
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TYPE$0, 0);
        }
    }

    /**
     * Gets the "InstallmentCnt" element
     */
    public java.lang.String getInstallmentCnt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(INSTALLMENTCNT$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "InstallmentCnt" element
     */
    public org.apache.xmlbeans.XmlString xgetInstallmentCnt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(INSTALLMENTCNT$2, 0);
            return target;
        }
    }

    /**
     * True if has "InstallmentCnt" element
     */
    public boolean isSetInstallmentCnt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(INSTALLMENTCNT$2) != 0;
        }
    }

    /**
     * Sets the "InstallmentCnt" element
     */
    public void setInstallmentCnt(java.lang.String installmentCnt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(INSTALLMENTCNT$2, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(INSTALLMENTCNT$2);
            }
            target.setStringValue(installmentCnt);
        }
    }

    /**
     * Sets (as xml) the "InstallmentCnt" element
     */
    public void xsetInstallmentCnt(org.apache.xmlbeans.XmlString installmentCnt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(INSTALLMENTCNT$2, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(INSTALLMENTCNT$2);
            }
            target.set(installmentCnt);
        }
    }

    /**
     * Unsets the "InstallmentCnt" element
     */
    public void unsetInstallmentCnt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(INSTALLMENTCNT$2, 0);
        }
    }

    /**
     * Gets the "Amount" element
     */
    public java.lang.String getAmount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(AMOUNT$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Amount" element
     */
    public org.apache.xmlbeans.XmlString xgetAmount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(AMOUNT$4, 0);
            return target;
        }
    }

    /**
     * True if has "Amount" element
     */
    public boolean isSetAmount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(AMOUNT$4) != 0;
        }
    }

    /**
     * Sets the "Amount" element
     */
    public void setAmount(java.lang.String amount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(AMOUNT$4, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(AMOUNT$4);
            }
            target.setStringValue(amount);
        }
    }

    /**
     * Sets (as xml) the "Amount" element
     */
    public void xsetAmount(org.apache.xmlbeans.XmlString amount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(AMOUNT$4, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(AMOUNT$4);
            }
            target.set(amount);
        }
    }

    /**
     * Unsets the "Amount" element
     */
    public void unsetAmount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(AMOUNT$4, 0);
        }
    }

    /**
     * Gets the "CurrencyCode" element
     */
    public java.lang.String getCurrencyCode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(CURRENCYCODE$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "CurrencyCode" element
     */
    public org.apache.xmlbeans.XmlString xgetCurrencyCode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(CURRENCYCODE$6, 0);
            return target;
        }
    }

    /**
     * True if has "CurrencyCode" element
     */
    public boolean isSetCurrencyCode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(CURRENCYCODE$6) != 0;
        }
    }

    /**
     * Sets the "CurrencyCode" element
     */
    public void setCurrencyCode(java.lang.String currencyCode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(CURRENCYCODE$6, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(CURRENCYCODE$6);
            }
            target.setStringValue(currencyCode);
        }
    }

    /**
     * Sets (as xml) the "CurrencyCode" element
     */
    public void xsetCurrencyCode(org.apache.xmlbeans.XmlString currencyCode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(CURRENCYCODE$6, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(CURRENCYCODE$6);
            }
            target.set(currencyCode);
        }
    }

    /**
     * Unsets the "CurrencyCode" element
     */
    public void unsetCurrencyCode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CURRENCYCODE$6, 0);
        }
    }

    /**
     * Gets the "OriginalRetrefNum" element
     */
    public java.lang.String getOriginalRetrefNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(ORIGINALRETREFNUM$8, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "OriginalRetrefNum" element
     */
    public org.apache.xmlbeans.XmlString xgetOriginalRetrefNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(ORIGINALRETREFNUM$8, 0);
            return target;
        }
    }

    /**
     * True if has "OriginalRetrefNum" element
     */
    public boolean isSetOriginalRetrefNum() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(ORIGINALRETREFNUM$8) != 0;
        }
    }

    /**
     * Sets the "OriginalRetrefNum" element
     */
    public void setOriginalRetrefNum(java.lang.String originalRetrefNum) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(ORIGINALRETREFNUM$8, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(ORIGINALRETREFNUM$8);
            }
            target.setStringValue(originalRetrefNum);
        }
    }

    /**
     * Sets (as xml) the "OriginalRetrefNum" element
     */
    public void xsetOriginalRetrefNum(org.apache.xmlbeans.XmlString originalRetrefNum) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(ORIGINALRETREFNUM$8, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(ORIGINALRETREFNUM$8);
            }
            target.set(originalRetrefNum);
        }
    }

    /**
     * Unsets the "OriginalRetrefNum" element
     */
    public void unsetOriginalRetrefNum() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ORIGINALRETREFNUM$8, 0);
        }
    }

    /**
     * Gets the "CardholderPresentCode" element
     */
    public java.lang.String getCardholderPresentCode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(CARDHOLDERPRESENTCODE$10, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "CardholderPresentCode" element
     */
    public org.apache.xmlbeans.XmlString xgetCardholderPresentCode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(CARDHOLDERPRESENTCODE$10, 0);
            return target;
        }
    }

    /**
     * True if has "CardholderPresentCode" element
     */
    public boolean isSetCardholderPresentCode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(CARDHOLDERPRESENTCODE$10) != 0;
        }
    }

    /**
     * Sets the "CardholderPresentCode" element
     */
    public void setCardholderPresentCode(java.lang.String cardholderPresentCode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(CARDHOLDERPRESENTCODE$10, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(CARDHOLDERPRESENTCODE$10);
            }
            target.setStringValue(cardholderPresentCode);
        }
    }

    /**
     * Sets (as xml) the "CardholderPresentCode" element
     */
    public void xsetCardholderPresentCode(org.apache.xmlbeans.XmlString cardholderPresentCode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(CARDHOLDERPRESENTCODE$10, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(CARDHOLDERPRESENTCODE$10);
            }
            target.set(cardholderPresentCode);
        }
    }

    /**
     * Unsets the "CardholderPresentCode" element
     */
    public void unsetCardholderPresentCode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CARDHOLDERPRESENTCODE$10, 0);
        }
    }

    /**
     * Gets the "MotoInd" element
     */
    public java.lang.String getMotoInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(MOTOIND$12, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "MotoInd" element
     */
    public org.apache.xmlbeans.XmlString xgetMotoInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(MOTOIND$12, 0);
            return target;
        }
    }

    /**
     * True if has "MotoInd" element
     */
    public boolean isSetMotoInd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(MOTOIND$12) != 0;
        }
    }

    /**
     * Sets the "MotoInd" element
     */
    public void setMotoInd(java.lang.String motoInd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_element_user(MOTOIND$12, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_element_user(MOTOIND$12);
            }
            target.setStringValue(motoInd);
        }
    }

    /**
     * Sets (as xml) the "MotoInd" element
     */
    public void xsetMotoInd(org.apache.xmlbeans.XmlString motoInd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString) get_store().find_element_user(MOTOIND$12, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString) get_store().add_element_user(MOTOIND$12);
            }
            target.set(motoInd);
        }
    }

    /**
     * Unsets the "MotoInd" element
     */
    public void unsetMotoInd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MOTOIND$12, 0);
        }
    }

    /**
     * Gets the "Secure3D" element
     */
    public noNamespace.Secure3D getSecure3D() {
        synchronized (monitor()) {
            check_orphaned();
            noNamespace.Secure3D target = null;
            target = (noNamespace.Secure3D) get_store().find_element_user(SECURE3D$14, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * True if has "Secure3D" element
     */
    public boolean isSetSecure3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(SECURE3D$14) != 0;
        }
    }

    /**
     * Sets the "Secure3D" element
     */
    public void setSecure3D(noNamespace.Secure3D secure3D) {
        synchronized (monitor()) {
            check_orphaned();
            noNamespace.Secure3D target = null;
            target = (noNamespace.Secure3D) get_store().find_element_user(SECURE3D$14, 0);
            if (target == null) {
                target = (noNamespace.Secure3D) get_store().add_element_user(SECURE3D$14);
            }
            target.set(secure3D);
        }
    }

    /**
     * Appends and returns a new empty "Secure3D" element
     */
    public noNamespace.Secure3D addNewSecure3D() {
        synchronized (monitor()) {
            check_orphaned();
            noNamespace.Secure3D target = null;
            target = (noNamespace.Secure3D) get_store().add_element_user(SECURE3D$14);
            return target;
        }
    }

    /**
     * Unsets the "Secure3D" element
     */
    public void unsetSecure3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SECURE3D$14, 0);
        }
    }
}
