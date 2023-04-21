package bn.mq.bean;

public class IdentidadInfo_Helper {

    private static final com.ibm.ws.webservices.engine.description.TypeDesc typeDesc = new com.ibm.ws.webservices.engine.description.TypeDesc(IdentidadInfo.class);

    static {
        typeDesc.setOption("buildNum", "o0444.10");
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("desError");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "desError"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("error");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "error"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("LIdentidad");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "LIdentidad"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://interfaz.mq.bn", "ArrayOf_tns2_IdentidadResume"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("numIncidencias");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "numIncidencias"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("reservado");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "reservado"));
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
        return new IdentidadInfo_Ser(javaType, xmlType, typeDesc);
    }

    ;

    /**
     * Get Custom Deserializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class javaType, javax.xml.namespace.QName xmlType) {
        return new IdentidadInfo_Deser(javaType, xmlType, typeDesc);
    }

    ;
}
