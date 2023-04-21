package org.sf.jspread.marketdata.inetats.webservice;

public class StockListType implements java.io.Serializable {

    private org.sf.jspread.marketdata.inetats.webservice.StockType[] stock;

    private org.sf.jspread.marketdata.inetats.webservice.RecNumbType recNumb;

    private org.apache.axis.types.NormalizedString listName;

    private org.apache.axis.types.NormalizedString sort;

    private org.apache.axis.types.NormalizedString order;

    public StockListType() {
    }

    public org.sf.jspread.marketdata.inetats.webservice.StockType[] getStock() {
        return stock;
    }

    public void setStock(org.sf.jspread.marketdata.inetats.webservice.StockType[] stock) {
        this.stock = stock;
    }

    public org.sf.jspread.marketdata.inetats.webservice.StockType getStock(int i) {
        return stock[i];
    }

    public void setStock(int i, org.sf.jspread.marketdata.inetats.webservice.StockType value) {
        this.stock[i] = value;
    }

    public org.sf.jspread.marketdata.inetats.webservice.RecNumbType getRecNumb() {
        return recNumb;
    }

    public void setRecNumb(org.sf.jspread.marketdata.inetats.webservice.RecNumbType recNumb) {
        this.recNumb = recNumb;
    }

    public org.apache.axis.types.NormalizedString getListName() {
        return listName;
    }

    public void setListName(org.apache.axis.types.NormalizedString listName) {
        this.listName = listName;
    }

    public org.apache.axis.types.NormalizedString getSort() {
        return sort;
    }

    public void setSort(org.apache.axis.types.NormalizedString sort) {
        this.sort = sort;
    }

    public org.apache.axis.types.NormalizedString getOrder() {
        return order;
    }

    public void setOrder(org.apache.axis.types.NormalizedString order) {
        this.order = order;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StockListType)) return false;
        StockListType other = (StockListType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.stock == null && other.getStock() == null) || (this.stock != null && java.util.Arrays.equals(this.stock, other.getStock()))) && ((this.recNumb == null && other.getRecNumb() == null) || (this.recNumb != null && this.recNumb.equals(other.getRecNumb()))) && ((this.listName == null && other.getListName() == null) || (this.listName != null && this.listName.equals(other.getListName()))) && ((this.sort == null && other.getSort() == null) || (this.sort != null && this.sort.equals(other.getSort()))) && ((this.order == null && other.getOrder() == null) || (this.order != null && this.order.equals(other.getOrder())));
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
        if (getStock() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getStock()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStock(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRecNumb() != null) {
            _hashCode += getRecNumb().hashCode();
        }
        if (getListName() != null) {
            _hashCode += getListName().hashCode();
        }
        if (getSort() != null) {
            _hashCode += getSort().hashCode();
        }
        if (getOrder() != null) {
            _hashCode += getOrder().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(StockListType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "stockListType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("recNumb");
        attrField.setXmlName(new javax.xml.namespace.QName("", "recNumb"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "recNumbType"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("listName");
        attrField.setXmlName(new javax.xml.namespace.QName("", "listName"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "normalizedString"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("sort");
        attrField.setXmlName(new javax.xml.namespace.QName("", "sort"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "normalizedString"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("order");
        attrField.setXmlName(new javax.xml.namespace.QName("", "order"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "normalizedString"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stock");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stock"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "stockType"));
        elemField.setMinOccurs(0);
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
