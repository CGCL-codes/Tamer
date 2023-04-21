package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class IExhibitSrv_EditDemandVP_ResponseStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_return_QNAME = new QName("", "return");

    private static final javax.xml.namespace.QName ns3_boolean_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BOOLEAN;

    private CombinedSerializer ns3_myns3__boolean__boolean_Boolean_Serializer;

    private static final int my_RETURN_INDEX = 0;

    public IExhibitSrv_EditDemandVP_ResponseStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns3_myns3__boolean__boolean_Boolean_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, boolean.class, ns3_boolean_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_EditDemandVP_ResponseStruct instance = new pack.IExhibitSrv_EditDemandVP_ResponseStruct();
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_return_QNAME)) {
                member = ns3_myns3__boolean__boolean_Boolean_Serializer.deserialize(ns1_return_QNAME, reader, context);
                instance.set_return(((Boolean) member).booleanValue());
                reader.nextElementContent();
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_return_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object) instance : (java.lang.Object) state);
    }

    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_EditDemandVP_ResponseStruct instance = (pack.IExhibitSrv_EditDemandVP_ResponseStruct) obj;
    }

    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_EditDemandVP_ResponseStruct instance = (pack.IExhibitSrv_EditDemandVP_ResponseStruct) obj;
        ns3_myns3__boolean__boolean_Boolean_Serializer.serialize(new Boolean(instance.is_return()), ns1_return_QNAME, null, writer, context);
    }

    protected void verifyName(XMLReader reader, javax.xml.namespace.QName expectedName) throws java.lang.Exception {
    }
}
