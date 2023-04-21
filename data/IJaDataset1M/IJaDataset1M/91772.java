package pe.bn.service.bean;

public class ResponseGateway_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {

    /**
     * Constructor
     */
    public ResponseGateway_Ser(java.lang.Class _javaType, javax.xml.namespace.QName _xmlType, com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }

    public void serialize(javax.xml.namespace.QName name, org.xml.sax.Attributes attributes, java.lang.Object value, com.ibm.ws.webservices.engine.encoding.SerializationContext context) throws java.io.IOException {
        context.startElement(name, addAttributes(attributes, value, context));
        addElements(value, context);
        context.endElement();
    }

    protected org.xml.sax.Attributes addAttributes(org.xml.sax.Attributes attributes, java.lang.Object value, com.ibm.ws.webservices.engine.encoding.SerializationContext context) throws java.io.IOException {
        return attributes;
    }

    protected void addElements(java.lang.Object value, com.ibm.ws.webservices.engine.encoding.SerializationContext context) throws java.io.IOException {
        ResponseGateway bean = (ResponseGateway) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
            propQName = QName_0_0;
            propValue = bean.getDatos();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_4, true, null, context);
            }
            propQName = QName_0_1;
            propValue = bean.getFiller();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_4, true, null, context);
            }
            propQName = QName_0_5;
            propValue = bean.getMensaje();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_4, true, null, context);
            }
            propQName = QName_0_6;
            propValue = bean.getMsgno();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_4, true, null, context);
            }
        }
    }

    private static final javax.xml.namespace.QName QName_0_5 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "mensaje");

    private static final javax.xml.namespace.QName QName_0_1 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "filler");

    private static final javax.xml.namespace.QName QName_0_6 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "msgno");

    private static final javax.xml.namespace.QName QName_0_0 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "datos");

    private static final javax.xml.namespace.QName QName_1_4 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string");
}
