package com.amazon.ws;

public class CartAddRequest implements java.io.Serializable {

    private java.lang.String cartId;

    private java.lang.String HMAC;

    private java.lang.String mergeCart;

    private com.amazon.ws.CartAddRequestItemsItem[] items;

    private java.lang.String[] responseGroup;

    public CartAddRequest() {
    }

    public CartAddRequest(java.lang.String cartId, java.lang.String HMAC, java.lang.String mergeCart, com.amazon.ws.CartAddRequestItemsItem[] items, java.lang.String[] responseGroup) {
        this.cartId = cartId;
        this.HMAC = HMAC;
        this.mergeCart = mergeCart;
        this.items = items;
        this.responseGroup = responseGroup;
    }

    /**
     * Gets the cartId value for this CartAddRequest.
     * 
     * @return cartId
     */
    public java.lang.String getCartId() {
        return cartId;
    }

    /**
     * Sets the cartId value for this CartAddRequest.
     * 
     * @param cartId
     */
    public void setCartId(java.lang.String cartId) {
        this.cartId = cartId;
    }

    /**
     * Gets the HMAC value for this CartAddRequest.
     * 
     * @return HMAC
     */
    public java.lang.String getHMAC() {
        return HMAC;
    }

    /**
     * Sets the HMAC value for this CartAddRequest.
     * 
     * @param HMAC
     */
    public void setHMAC(java.lang.String HMAC) {
        this.HMAC = HMAC;
    }

    /**
     * Gets the mergeCart value for this CartAddRequest.
     * 
     * @return mergeCart
     */
    public java.lang.String getMergeCart() {
        return mergeCart;
    }

    /**
     * Sets the mergeCart value for this CartAddRequest.
     * 
     * @param mergeCart
     */
    public void setMergeCart(java.lang.String mergeCart) {
        this.mergeCart = mergeCart;
    }

    /**
     * Gets the items value for this CartAddRequest.
     * 
     * @return items
     */
    public com.amazon.ws.CartAddRequestItemsItem[] getItems() {
        return items;
    }

    /**
     * Sets the items value for this CartAddRequest.
     * 
     * @param items
     */
    public void setItems(com.amazon.ws.CartAddRequestItemsItem[] items) {
        this.items = items;
    }

    /**
     * Gets the responseGroup value for this CartAddRequest.
     * 
     * @return responseGroup
     */
    public java.lang.String[] getResponseGroup() {
        return responseGroup;
    }

    /**
     * Sets the responseGroup value for this CartAddRequest.
     * 
     * @param responseGroup
     */
    public void setResponseGroup(java.lang.String[] responseGroup) {
        this.responseGroup = responseGroup;
    }

    public java.lang.String getResponseGroup(int i) {
        return this.responseGroup[i];
    }

    public void setResponseGroup(int i, java.lang.String _value) {
        this.responseGroup[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CartAddRequest)) return false;
        CartAddRequest other = (CartAddRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.cartId == null && other.getCartId() == null) || (this.cartId != null && this.cartId.equals(other.getCartId()))) && ((this.HMAC == null && other.getHMAC() == null) || (this.HMAC != null && this.HMAC.equals(other.getHMAC()))) && ((this.mergeCart == null && other.getMergeCart() == null) || (this.mergeCart != null && this.mergeCart.equals(other.getMergeCart()))) && ((this.items == null && other.getItems() == null) || (this.items != null && java.util.Arrays.equals(this.items, other.getItems()))) && ((this.responseGroup == null && other.getResponseGroup() == null) || (this.responseGroup != null && java.util.Arrays.equals(this.responseGroup, other.getResponseGroup())));
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
        if (getCartId() != null) {
            _hashCode += getCartId().hashCode();
        }
        if (getHMAC() != null) {
            _hashCode += getHMAC().hashCode();
        }
        if (getMergeCart() != null) {
            _hashCode += getMergeCart().hashCode();
        }
        if (getItems() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getItems()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItems(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getResponseGroup() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getResponseGroup()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResponseGroup(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CartAddRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", "CartAddRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cartId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", "CartId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HMAC");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", "HMAC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mergeCart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", "MergeCart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("items");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", "Items"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", ">>CartAddRequest>Items>Item"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", "Item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.amazon.com/AWSECommerceService/2005-03-23", "ResponseGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
