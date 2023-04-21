package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class IExhibitSrv_GetOwnInfo_ResponseStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_Page_QNAME = new QName("", "Page");

    private static final javax.xml.namespace.QName ns3_int_TYPE_QNAME = SchemaConstants.QNAME_TYPE_INT;

    private CombinedSerializer ns3_myns3__int__int_Int_Serializer;

    private static final javax.xml.namespace.QName ns1_TotalPages_QNAME = new QName("", "TotalPages");

    private static final javax.xml.namespace.QName ns1_OwnInfo_QNAME = new QName("", "OwnInfo");

    private static final javax.xml.namespace.QName ns2_TArrayOfOwnInfo_TYPE_QNAME = new QName("urn:uExhibitClasses", "TArrayOfOwnInfo");

    private CombinedSerializer ns2_myns2_TArrayOfOwnInfo__TOwnInfoArray_SOAPSerializer1;

    private static final javax.xml.namespace.QName ns1_return_QNAME = new QName("", "return");

    private static final javax.xml.namespace.QName ns3_boolean_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BOOLEAN;

    private CombinedSerializer ns3_myns3__boolean__boolean_Boolean_Serializer;

    private static final int myPAGE_INDEX = 0;

    private static final int myTOTALPAGES_INDEX = 1;

    private static final int myOWNINFO_INDEX = 2;

    private static final int my_RETURN_INDEX = 3;

    public IExhibitSrv_GetOwnInfo_ResponseStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns3_myns3__int__int_Int_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, int.class, ns3_int_TYPE_QNAME);
        ns2_myns2_TArrayOfOwnInfo__TOwnInfoArray_SOAPSerializer1 = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, pack.TOwnInfo[].class, ns2_TArrayOfOwnInfo_TYPE_QNAME);
        ns3_myns3__boolean__boolean_Boolean_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, boolean.class, ns3_boolean_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetOwnInfo_ResponseStruct instance = new pack.IExhibitSrv_GetOwnInfo_ResponseStruct();
        pack.IExhibitSrv_GetOwnInfo_ResponseStruct_SOAPBuilder builder = null;
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        reader.nextElementContent();
        for (int i = 0; i < 4; i++) {
            elementName = reader.getName();
            if (reader.getState() == XMLReader.END) {
                break;
            }
            if (elementName.equals(ns1_Page_QNAME)) {
                member = ns3_myns3__int__int_Int_Serializer.deserialize(ns1_Page_QNAME, reader, context);
                instance.setPage(((java.lang.Integer) member).intValue());
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_TotalPages_QNAME)) {
                member = ns3_myns3__int__int_Int_Serializer.deserialize(ns1_TotalPages_QNAME, reader, context);
                instance.setTotalPages(((java.lang.Integer) member).intValue());
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_OwnInfo_QNAME)) {
                member = ns2_myns2_TArrayOfOwnInfo__TOwnInfoArray_SOAPSerializer1.deserialize(ns1_OwnInfo_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new pack.IExhibitSrv_GetOwnInfo_ResponseStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myOWNINFO_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setOwnInfo((pack.TOwnInfo[]) member);
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
        pack.IExhibitSrv_GetOwnInfo_ResponseStruct instance = (pack.IExhibitSrv_GetOwnInfo_ResponseStruct) obj;
    }

    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetOwnInfo_ResponseStruct instance = (pack.IExhibitSrv_GetOwnInfo_ResponseStruct) obj;
        ns3_myns3__int__int_Int_Serializer.serialize(new java.lang.Integer(instance.getPage()), ns1_Page_QNAME, null, writer, context);
        ns3_myns3__int__int_Int_Serializer.serialize(new java.lang.Integer(instance.getTotalPages()), ns1_TotalPages_QNAME, null, writer, context);
        ns2_myns2_TArrayOfOwnInfo__TOwnInfoArray_SOAPSerializer1.serialize(instance.getOwnInfo(), ns1_OwnInfo_QNAME, null, writer, context);
        ns3_myns3__boolean__boolean_Boolean_Serializer.serialize(new Boolean(instance.is_return()), ns1_return_QNAME, null, writer, context);
    }

    protected void verifyName(XMLReader reader, javax.xml.namespace.QName expectedName) throws java.lang.Exception {
    }
}
