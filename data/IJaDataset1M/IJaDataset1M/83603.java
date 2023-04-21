package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class ExplicantoWebService_coursePreview_ResponseStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final QName ns1_result_QNAME = new QName("", "result");

    private static final QName ns2_WSResponse_TYPE_QNAME = new QName("http:///de.beas.explicanto.types/ExplicantoServer", "WSResponse");

    private CombinedSerializer ns2_myWSResponse_SOAPSerializer;

    private static final int myRESULT_INDEX = 0;

    public ExplicantoWebService_coursePreview_ResponseStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myWSResponse_SOAPSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, de.bea.services.vidya.client.datasource.types.WSResponse.class, ns2_WSResponse_TYPE_QNAME);
    }

    public Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct instance = new de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct();
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct_SOAPBuilder builder = null;
        Object member;
        boolean isComplete = true;
        QName elementName;
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_result_QNAME)) {
                member = ns2_myWSResponse_SOAPSerializer.deserialize(ns1_result_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myRESULT_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setResult((de.bea.services.vidya.client.datasource.types.WSResponse) member);
                }
                reader.nextElementContent();
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_result_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (Object) instance : (Object) state);
    }

    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct) obj;
    }

    public void doSerializeInstance(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_coursePreview_ResponseStruct) obj;
        ns2_myWSResponse_SOAPSerializer.serialize(instance.getResult(), ns1_result_QNAME, null, writer, context);
    }

    protected void verifyName(XMLReader reader, QName expectedName) throws Exception {
    }
}
