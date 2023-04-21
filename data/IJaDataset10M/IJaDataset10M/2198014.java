package pe.bn.service.bean;

public class RequestGateway_Helper {

    private static final com.ibm.ws.webservices.engine.description.TypeDesc typeDesc = new com.ibm.ws.webservices.engine.description.TypeDesc(RequestGateway.class);

    static {
        typeDesc.setOption("buildNum", "o0444.10");
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("datos");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "datos"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("filler");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "filler"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("longitud");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "longitud"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("transid");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "transid"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
    }

    ;

    /**
     * Return type metadata object
     */
    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class javaType, javax.xml.namespace.QName xmlType) {
        return new RequestGateway_Ser(javaType, xmlType, typeDesc);
    }

    ;

    /**
     * Get Custom Deserializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class javaType, javax.xml.namespace.QName xmlType) {
        return new RequestGateway_Deser(javaType, xmlType, typeDesc);
    }

    ;
}
