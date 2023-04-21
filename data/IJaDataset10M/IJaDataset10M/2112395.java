package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class ExplicantoWebService_storeUserList_RequestStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final QName ns1_WSAuthentication_1_QNAME = new QName("", "WSAuthentication_1");

    private static final QName ns2_WSAuthentication_TYPE_QNAME = new QName("http:///de.beas.explicanto.types/ExplicantoServer", "WSAuthentication");

    private CombinedSerializer ns2_myWSAuthentication_SOAPSerializer;

    private static final QName ns1_List_2_QNAME = new QName("", "List_2");

    private static final QName ns4_list_TYPE_QNAME = InternalEncodingConstants.QNAME_TYPE_LIST;

    private CombinedSerializer ns4_myns4_list__CollectionInterfaceSerializer;

    private static final int myWSAUTHENTICATION_1_INDEX = 0;

    private static final int myLIST_2_INDEX = 1;

    public ExplicantoWebService_storeUserList_RequestStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myWSAuthentication_SOAPSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, de.bea.services.vidya.client.datasource.types.WSAuthentication.class, ns2_WSAuthentication_TYPE_QNAME);
        ns4_myns4_list__CollectionInterfaceSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.util.List.class, ns4_list_TYPE_QNAME);
    }

    public Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct instance = new de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct();
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct_SOAPBuilder builder = null;
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
                        builder = new de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myWSAUTHENTICATION_1_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setWSAuthentication_1((de.bea.services.vidya.client.datasource.types.WSAuthentication) member);
                }
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_List_2_QNAME)) {
                member = ns4_myns4_list__CollectionInterfaceSerializer.deserialize(ns1_List_2_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myLIST_2_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setList_2((java.util.List) member);
                }
                reader.nextElementContent();
                continue;
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_List_2_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (Object) instance : (Object) state);
    }

    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct) obj;
    }

    public void doSerializeInstance(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_storeUserList_RequestStruct) obj;
        ns2_myWSAuthentication_SOAPSerializer.serialize(instance.getWSAuthentication_1(), ns1_WSAuthentication_1_QNAME, null, writer, context);
        ns4_myns4_list__CollectionInterfaceSerializer.serialize(instance.getList_2(), ns1_List_2_QNAME, null, writer, context);
    }
}
