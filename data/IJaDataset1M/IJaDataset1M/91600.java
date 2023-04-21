package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class IExhibitSrv_GetPhoto_ResponseStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_Photo_QNAME = new QName("", "Photo");

    private static final javax.xml.namespace.QName ns3_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;

    private CombinedSerializer ns3_myns3_string__java_lang_String_String_Serializer;

    private static final javax.xml.namespace.QName ns1_return_QNAME = new QName("", "return");

    private static final javax.xml.namespace.QName ns3_boolean_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BOOLEAN;

    private CombinedSerializer ns3_myns3__boolean__boolean_Boolean_Serializer;

    private static final int myPHOTO_INDEX = 0;

    private static final int my_RETURN_INDEX = 1;

    public IExhibitSrv_GetPhoto_ResponseStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns3_myns3_string__java_lang_String_String_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.lang.String.class, ns3_string_TYPE_QNAME);
        ns3_myns3__boolean__boolean_Boolean_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, boolean.class, ns3_boolean_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetPhoto_ResponseStruct instance = new pack.IExhibitSrv_GetPhoto_ResponseStruct();
        pack.IExhibitSrv_GetPhoto_ResponseStruct_SOAPBuilder builder = null;
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        reader.nextElementContent();
        for (int i = 0; i < 2; i++) {
            elementName = reader.getName();
            if (reader.getState() == XMLReader.END) {
                break;
            }
            if (elementName.equals(ns1_Photo_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_Photo_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new pack.IExhibitSrv_GetPhoto_ResponseStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myPHOTO_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setPhoto((java.lang.String) member);
                }
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_return_QNAME)) {
                member = ns3_myns3__boolean__boolean_Boolean_Serializer.deserialize(ns1_return_QNAME, reader, context);
                instance.set_return(((Boolean) member).booleanValue());
                reader.nextElementContent();
                continue;
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_return_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object) instance : (java.lang.Object) state);
    }

    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetPhoto_ResponseStruct instance = (pack.IExhibitSrv_GetPhoto_ResponseStruct) obj;
    }

    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetPhoto_ResponseStruct instance = (pack.IExhibitSrv_GetPhoto_ResponseStruct) obj;
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getPhoto(), ns1_Photo_QNAME, null, writer, context);
        ns3_myns3__boolean__boolean_Boolean_Serializer.serialize(new Boolean(instance.is_return()), ns1_return_QNAME, null, writer, context);
    }

    protected void verifyName(XMLReader reader, javax.xml.namespace.QName expectedName) throws java.lang.Exception {
    }
}
