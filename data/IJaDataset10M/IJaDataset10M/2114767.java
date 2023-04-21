package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class IExhibitSrv_GetOfferInfoForEdit_ResponseStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_Offer_QNAME = new QName("", "Offer");

    private static final javax.xml.namespace.QName ns2_TOfferInfo_TYPE_QNAME = new QName("urn:uExhibitClasses", "TOfferInfo");

    private CombinedSerializer ns2_myTOfferInfo_SOAPSerializer;

    private static final javax.xml.namespace.QName ns1_Previews_QNAME = new QName("", "Previews");

    private static final javax.xml.namespace.QName ns2_TArrayOfPhoto_TYPE_QNAME = new QName("urn:uExhibitClasses", "TArrayOfPhoto");

    private CombinedSerializer ns2_myns2_TArrayOfPhoto__TPhotoArray_SOAPSerializer1;

    private static final javax.xml.namespace.QName ns1_return_QNAME = new QName("", "return");

    private static final javax.xml.namespace.QName ns3_boolean_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BOOLEAN;

    private CombinedSerializer ns3_myns3__boolean__boolean_Boolean_Serializer;

    private static final int myOFFER_INDEX = 0;

    private static final int myPREVIEWS_INDEX = 1;

    private static final int my_RETURN_INDEX = 2;

    public IExhibitSrv_GetOfferInfoForEdit_ResponseStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns2_myTOfferInfo_SOAPSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, pack.TOfferInfo.class, ns2_TOfferInfo_TYPE_QNAME);
        ns2_myns2_TArrayOfPhoto__TPhotoArray_SOAPSerializer1 = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, pack.TPhoto[].class, ns2_TArrayOfPhoto_TYPE_QNAME);
        ns3_myns3__boolean__boolean_Boolean_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, boolean.class, ns3_boolean_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct instance = new pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct();
        pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct_SOAPBuilder builder = null;
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        reader.nextElementContent();
        for (int i = 0; i < 3; i++) {
            elementName = reader.getName();
            if (reader.getState() == XMLReader.END) {
                break;
            }
            if (elementName.equals(ns1_Offer_QNAME)) {
                member = ns2_myTOfferInfo_SOAPSerializer.deserialize(ns1_Offer_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myOFFER_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setOffer((pack.TOfferInfo) member);
                }
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_Previews_QNAME)) {
                member = ns2_myns2_TArrayOfPhoto__TPhotoArray_SOAPSerializer1.deserialize(ns1_Previews_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myPREVIEWS_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setPreviews((pack.TPhoto[]) member);
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
        pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct instance = (pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct) obj;
    }

    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct instance = (pack.IExhibitSrv_GetOfferInfoForEdit_ResponseStruct) obj;
        ns2_myTOfferInfo_SOAPSerializer.serialize(instance.getOffer(), ns1_Offer_QNAME, null, writer, context);
        ns2_myns2_TArrayOfPhoto__TPhotoArray_SOAPSerializer1.serialize(instance.getPreviews(), ns1_Previews_QNAME, null, writer, context);
        ns3_myns3__boolean__boolean_Boolean_Serializer.serialize(new Boolean(instance.is_return()), ns1_return_QNAME, null, writer, context);
    }

    protected void verifyName(XMLReader reader, javax.xml.namespace.QName expectedName) throws java.lang.Exception {
    }
}
