package org.ulpgc.ws.sessionexternal;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_return_QNAME = new QName("", "return");

    private static final javax.xml.namespace.QName ns2_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;

    private CombinedSerializer ns2_myns2_string__java_lang_String_String_Serializer;

    private static final int my_RETURN_INDEX = 0;

    public Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns2_myns2_string__java_lang_String_String_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.lang.String.class, ns2_string_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct instance = new org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct();
        org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct_SOAPBuilder builder = null;
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_return_QNAME)) {
                member = ns2_myns2_string__java_lang_String_String_Serializer.deserialize(ns1_return_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, my_RETURN_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.set_return((java.lang.String) member);
                }
                reader.nextElementContent();
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_return_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object) instance : (java.lang.Object) state);
    }

    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct instance = (org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct) obj;
    }

    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct instance = (org.ulpgc.ws.sessionexternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct) obj;
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.get_return(), ns1_return_QNAME, null, writer, context);
    }

    protected void verifyName(XMLReader reader, javax.xml.namespace.QName expectedName) throws java.lang.Exception {
    }
}
