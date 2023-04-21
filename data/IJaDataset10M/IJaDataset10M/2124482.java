package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class ExplicantoWebService_loadCSDETemplate_RequestStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final QName ns1_WSAuthentication_1_QNAME = new QName("", "WSAuthentication_1");

    private static final QName ns2_WSAuthentication_TYPE_QNAME = new QName("http:///de.beas.explicanto.types/ExplicantoServer", "WSAuthentication");

    private CombinedSerializer ns2_myWSAuthentication_SOAPSerializer;

    private static final QName ns1_long_2_QNAME = new QName("", "long_2");

    private static final QName ns3_long_TYPE_QNAME = SchemaConstants.QNAME_TYPE_LONG;

    private CombinedSerializer ns3_myns3__long__long_Long_Serializer;

    private static final int myWSAUTHENTICATION_1_INDEX = 0;

    private static final int myLONG_2_INDEX = 1;

    public ExplicantoWebService_loadCSDETemplate_RequestStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myWSAuthentication_SOAPSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, de.bea.services.vidya.client.datasource.types.WSAuthentication.class, ns2_WSAuthentication_TYPE_QNAME);
        ns3_myns3__long__long_Long_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, long.class, ns3_long_TYPE_QNAME);
    }

    public Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct instance = new de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct();
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct_SOAPBuilder builder = null;
        Object member;
        boolean isComplete = true;
        QName elementName;
        reader.nextElementContent();
        for (int i = 0; i < 2; i++) {
            elementName = reader.getName();
            if (reader.getState() == XMLReader.END) {
                break;
            }
            if (elementName.equals(ns1_WSAuthentication_1_QNAME)) {
                member = ns2_myWSAuthentication_SOAPSerializer.deserialize(ns1_WSAuthentication_1_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myWSAUTHENTICATION_1_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setWSAuthentication_1((de.bea.services.vidya.client.datasource.types.WSAuthentication) member);
                }
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_long_2_QNAME)) {
                member = ns3_myns3__long__long_Long_Serializer.deserialize(ns1_long_2_QNAME, reader, context);
                instance.setLong_2(((Long) member).longValue());
                reader.nextElementContent();
                continue;
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_long_2_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (Object) instance : (Object) state);
    }

    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct) obj;
    }

    public void doSerializeInstance(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_loadCSDETemplate_RequestStruct) obj;
        ns2_myWSAuthentication_SOAPSerializer.serialize(instance.getWSAuthentication_1(), ns1_WSAuthentication_1_QNAME, null, writer, context);
        ns3_myns3__long__long_Long_Serializer.serialize(new Long(instance.getLong_2()), ns1_long_2_QNAME, null, writer, context);
    }
}
