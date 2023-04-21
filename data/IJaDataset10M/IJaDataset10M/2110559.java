package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class IExhibitSrv_GetDataDomain_RequestStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_DomainID_QNAME = new QName("", "DomainID");

    private static final javax.xml.namespace.QName ns3_int_TYPE_QNAME = SchemaConstants.QNAME_TYPE_INT;

    private CombinedSerializer ns3_myns3__int__int_Int_Serializer;

    private static final int myDOMAINID_INDEX = 0;

    public IExhibitSrv_GetDataDomain_RequestStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns3_myns3__int__int_Int_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, int.class, ns3_int_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetDataDomain_RequestStruct instance = new pack.IExhibitSrv_GetDataDomain_RequestStruct();
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_DomainID_QNAME)) {
                member = ns3_myns3__int__int_Int_Serializer.deserialize(ns1_DomainID_QNAME, reader, context);
                instance.setDomainID(((java.lang.Integer) member).intValue());
                reader.nextElementContent();
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_DomainID_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object) instance : (java.lang.Object) state);
    }

    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetDataDomain_RequestStruct instance = (pack.IExhibitSrv_GetDataDomain_RequestStruct) obj;
    }

    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetDataDomain_RequestStruct instance = (pack.IExhibitSrv_GetDataDomain_RequestStruct) obj;
        ns3_myns3__int__int_Int_Serializer.serialize(new java.lang.Integer(instance.getDomainID()), ns1_DomainID_QNAME, null, writer, context);
    }
}
