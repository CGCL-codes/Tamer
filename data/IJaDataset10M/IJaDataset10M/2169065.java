package it.jplag.jplagClient;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.xsd.XSDConstants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class MailTemplateArray_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_items_QNAME = new QName("", "items");

    private static final javax.xml.namespace.QName ns3_MailTemplate_TYPE_QNAME = new QName("http://www.ipd.uni-karlsruhe.de/jplag/types", "MailTemplate");

    private CombinedSerializer ns3_myMailTemplate_LiteralSerializer;

    public MailTemplateArray_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle) {
        this(type, encodingStyle, false);
    }

    public MailTemplateArray_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns3_myMailTemplate_LiteralSerializer = (CombinedSerializer) registry.getSerializer("", it.jplag.jplagClient.MailTemplate.class, ns3_MailTemplate_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        it.jplag.jplagClient.MailTemplateArray instance = new it.jplag.jplagClient.MailTemplateArray();
        java.lang.Object member = null;
        javax.xml.namespace.QName elementName;
        java.util.List values;
        java.lang.Object value;
        reader.nextElementContent();
        elementName = reader.getName();
        if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_items_QNAME))) {
            values = new ArrayList();
            for (; ; ) {
                elementName = reader.getName();
                if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_items_QNAME))) {
                    value = ns3_myMailTemplate_LiteralSerializer.deserialize(ns1_items_QNAME, reader, context);
                    if (value == null) {
                        throw new DeserializationException("literal.unexpectedNull");
                    }
                    values.add(value);
                    reader.nextElementContent();
                } else {
                    break;
                }
            }
            member = new it.jplag.jplagClient.MailTemplate[values.size()];
            member = values.toArray((Object[]) member);
            instance.setItems((it.jplag.jplagClient.MailTemplate[]) member);
        } else {
            instance.setItems(new it.jplag.jplagClient.MailTemplate[0]);
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (java.lang.Object) instance;
    }

    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        it.jplag.jplagClient.MailTemplateArray instance = (it.jplag.jplagClient.MailTemplateArray) obj;
    }

    public void doSerialize(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        it.jplag.jplagClient.MailTemplateArray instance = (it.jplag.jplagClient.MailTemplateArray) obj;
        if (instance.getItems() != null) {
            for (int i = 0; i < instance.getItems().length; ++i) {
                ns3_myMailTemplate_LiteralSerializer.serialize(instance.getItems()[i], ns1_items_QNAME, null, writer, context);
            }
        }
    }
}
