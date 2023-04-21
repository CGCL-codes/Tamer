package Businessitems;

public class Book_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {

    /**
     * Constructor
     */
    public Book_Ser(java.lang.Class _javaType, javax.xml.namespace.QName _xmlType, com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
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
        Book bean = (Book) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
            propQName = QName_0_6;
            propValue = bean.getName();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_2, true, null, context);
            }
            propQName = QName_0_7;
            propValue = bean.getPrice();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_2, true, null, context);
            }
            propQName = QName_0_8;
            propValue = bean.getIndex();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_2, true, null, context);
            }
            propQName = QName_0_9;
            propValue = bean.getOutAllow();
            if (propValue != null && !context.shouldSendXSIType()) {
                context.simpleElement(propQName, null, propValue.toString());
            } else {
                serializeChild(propQName, null, propValue, QName_1_2, true, null, context);
            }
        }
    }

    private static final javax.xml.namespace.QName QName_0_6 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "Name");

    private static final javax.xml.namespace.QName QName_0_9 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "OutAllow");

    private static final javax.xml.namespace.QName QName_0_7 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "Price");

    private static final javax.xml.namespace.QName QName_1_2 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string");

    private static final javax.xml.namespace.QName QName_0_8 = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "Index");
}
