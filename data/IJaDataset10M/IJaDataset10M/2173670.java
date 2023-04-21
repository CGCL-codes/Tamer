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

public class SetMailTemplateParams_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_type_QNAME = new QName("", "type");

    private static final javax.xml.namespace.QName ns2_int_TYPE_QNAME = SchemaConstants.QNAME_TYPE_INT;

    private CombinedSerializer ns2_myns2__int__int_Int_Serializer;

    private static final javax.xml.namespace.QName ns1_template_QNAME = new QName("", "template");

    private static final javax.xml.namespace.QName ns3_MailTemplate_TYPE_QNAME = new QName("http://www.ipd.uni-karlsruhe.de/jplag/types", "MailTemplate");

    private CombinedSerializer ns3_myMailTemplate_LiteralSerializer;

    public SetMailTemplateParams_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle) {
        this(type, encodingStyle, false);
    }

    public SetMailTemplateParams_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myns2__int__int_Int_Serializer = (CombinedSerializer) registry.getSerializer("", int.class, ns2_int_TYPE_QNAME);
        ns3_myMailTemplate_LiteralSerializer = (CombinedSerializer) registry.getSerializer("", it.jplag.jplagClient.MailTemplate.class, ns3_MailTemplate_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        it.jplag.jplagClient.SetMailTemplateParams instance = new it.jplag.jplagClient.SetMailTemplateParams();
        java.lang.Object member = null;
        javax.xml.namespace.QName elementName;
        java.util.List values;
        java.lang.Object value;
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_type_QNAME)) {
                member = ns2_myns2__int__int_Int_Serializer.deserialize(ns1_type_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setType(((java.lang.Integer) member).intValue());
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_type_QNAME, reader.getName() });
            }
        } else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_template_QNAME)) {
                member = ns3_myMailTemplate_LiteralSerializer.deserialize(ns1_template_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setTemplate((it.jplag.jplagClient.MailTemplate) member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_template_QNAME, reader.getName() });
            }
        } else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (java.lang.Object) instance;
    }

    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        it.jplag.jplagClient.SetMailTemplateParams instance = (it.jplag.jplagClient.SetMailTemplateParams) obj;
    }

    public void doSerialize(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        it.jplag.jplagClient.SetMailTemplateParams instance = (it.jplag.jplagClient.SetMailTemplateParams) obj;
        if (new java.lang.Integer(instance.getType()) == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2__int__int_Int_Serializer.serialize(new java.lang.Integer(instance.getType()), ns1_type_QNAME, null, writer, context);
        if (instance.getTemplate() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myMailTemplate_LiteralSerializer.serialize(instance.getTemplate(), ns1_template_QNAME, null, writer, context);
    }
}
