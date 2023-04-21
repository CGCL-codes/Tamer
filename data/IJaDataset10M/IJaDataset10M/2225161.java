package org.tripcom.dam.frontend;

/**
 * Any of the agents collaborating in the use case
 * 				execution.
 */
public class Actor implements java.io.Serializable {

    private java.lang.String actorName;

    private java.lang.String actorRole;

    private java.lang.String actorCity;

    private java.lang.String actorCountry;

    private java.math.BigInteger[] actorBankAccount;

    private java.math.BigInteger[] actorCreditCard;

    private org.tripcom.dam.frontend.BlackListItem[] actorBlackList;

    public Actor() {
    }

    public Actor(java.lang.String actorName, java.lang.String actorRole, java.lang.String actorCity, java.lang.String actorCountry, java.math.BigInteger[] actorBankAccount, java.math.BigInteger[] actorCreditCard, org.tripcom.dam.frontend.BlackListItem[] actorBlackList) {
        this.actorName = actorName;
        this.actorRole = actorRole;
        this.actorCity = actorCity;
        this.actorCountry = actorCountry;
        this.actorBankAccount = actorBankAccount;
        this.actorCreditCard = actorCreditCard;
        this.actorBlackList = actorBlackList;
    }

    /**
     * Gets the actorName value for this Actor.
     * 
     * @return actorName
     */
    public java.lang.String getActorName() {
        return actorName;
    }

    /**
     * Sets the actorName value for this Actor.
     * 
     * @param actorName
     */
    public void setActorName(java.lang.String actorName) {
        this.actorName = actorName;
    }

    /**
     * Gets the actorRole value for this Actor.
     * 
     * @return actorRole   * The kind of actor. Must be specified in lower
     * 						case and without spaces. In case of more than
     * 						one word, use following syntax: serviceProvider,
     * 						contentProvider
     */
    public java.lang.String getActorRole() {
        return actorRole;
    }

    /**
     * Sets the actorRole value for this Actor.
     * 
     * @param actorRole   * The kind of actor. Must be specified in lower
     * 						case and without spaces. In case of more than
     * 						one word, use following syntax: serviceProvider,
     * 						contentProvider
     */
    public void setActorRole(java.lang.String actorRole) {
        this.actorRole = actorRole;
    }

    /**
     * Gets the actorCity value for this Actor.
     * 
     * @return actorCity
     */
    public java.lang.String getActorCity() {
        return actorCity;
    }

    /**
     * Sets the actorCity value for this Actor.
     * 
     * @param actorCity
     */
    public void setActorCity(java.lang.String actorCity) {
        this.actorCity = actorCity;
    }

    /**
     * Gets the actorCountry value for this Actor.
     * 
     * @return actorCountry
     */
    public java.lang.String getActorCountry() {
        return actorCountry;
    }

    /**
     * Sets the actorCountry value for this Actor.
     * 
     * @param actorCountry
     */
    public void setActorCountry(java.lang.String actorCountry) {
        this.actorCountry = actorCountry;
    }

    /**
     * Gets the actorBankAccount value for this Actor.
     * 
     * @return actorBankAccount
     */
    public java.math.BigInteger[] getActorBankAccount() {
        return actorBankAccount;
    }

    /**
     * Sets the actorBankAccount value for this Actor.
     * 
     * @param actorBankAccount
     */
    public void setActorBankAccount(java.math.BigInteger[] actorBankAccount) {
        this.actorBankAccount = actorBankAccount;
    }

    public java.math.BigInteger getActorBankAccount(int i) {
        return this.actorBankAccount[i];
    }

    public void setActorBankAccount(int i, java.math.BigInteger _value) {
        this.actorBankAccount[i] = _value;
    }

    /**
     * Gets the actorCreditCard value for this Actor.
     * 
     * @return actorCreditCard
     */
    public java.math.BigInteger[] getActorCreditCard() {
        return actorCreditCard;
    }

    /**
     * Sets the actorCreditCard value for this Actor.
     * 
     * @param actorCreditCard
     */
    public void setActorCreditCard(java.math.BigInteger[] actorCreditCard) {
        this.actorCreditCard = actorCreditCard;
    }

    public java.math.BigInteger getActorCreditCard(int i) {
        return this.actorCreditCard[i];
    }

    public void setActorCreditCard(int i, java.math.BigInteger _value) {
        this.actorCreditCard[i] = _value;
    }

    /**
     * Gets the actorBlackList value for this Actor.
     * 
     * @return actorBlackList
     */
    public org.tripcom.dam.frontend.BlackListItem[] getActorBlackList() {
        return actorBlackList;
    }

    /**
     * Sets the actorBlackList value for this Actor.
     * 
     * @param actorBlackList
     */
    public void setActorBlackList(org.tripcom.dam.frontend.BlackListItem[] actorBlackList) {
        this.actorBlackList = actorBlackList;
    }

    public org.tripcom.dam.frontend.BlackListItem getActorBlackList(int i) {
        return this.actorBlackList[i];
    }

    public void setActorBlackList(int i, org.tripcom.dam.frontend.BlackListItem _value) {
        this.actorBlackList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Actor)) return false;
        Actor other = (Actor) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.actorName == null && other.getActorName() == null) || (this.actorName != null && this.actorName.equals(other.getActorName()))) && ((this.actorRole == null && other.getActorRole() == null) || (this.actorRole != null && this.actorRole.equals(other.getActorRole()))) && ((this.actorCity == null && other.getActorCity() == null) || (this.actorCity != null && this.actorCity.equals(other.getActorCity()))) && ((this.actorCountry == null && other.getActorCountry() == null) || (this.actorCountry != null && this.actorCountry.equals(other.getActorCountry()))) && ((this.actorBankAccount == null && other.getActorBankAccount() == null) || (this.actorBankAccount != null && java.util.Arrays.equals(this.actorBankAccount, other.getActorBankAccount()))) && ((this.actorCreditCard == null && other.getActorCreditCard() == null) || (this.actorCreditCard != null && java.util.Arrays.equals(this.actorCreditCard, other.getActorCreditCard()))) && ((this.actorBlackList == null && other.getActorBlackList() == null) || (this.actorBlackList != null && java.util.Arrays.equals(this.actorBlackList, other.getActorBlackList())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getActorName() != null) {
            _hashCode += getActorName().hashCode();
        }
        if (getActorRole() != null) {
            _hashCode += getActorRole().hashCode();
        }
        if (getActorCity() != null) {
            _hashCode += getActorCity().hashCode();
        }
        if (getActorCountry() != null) {
            _hashCode += getActorCountry().hashCode();
        }
        if (getActorBankAccount() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getActorBankAccount()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActorBankAccount(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getActorCreditCard() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getActorCreditCard()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActorCreditCard(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getActorBlackList() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getActorBlackList()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActorBlackList(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Actor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "Actor"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "actorName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorRole");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "actorRole"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorCity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "actorCity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorCountry");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "actorCountry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorBankAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "actorBankAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorCreditCard");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "actorCreditCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorBlackList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "actorBlackList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dam.tripcom.org/frontend", "BlackListItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
}
