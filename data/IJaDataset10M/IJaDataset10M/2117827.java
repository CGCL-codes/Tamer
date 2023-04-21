package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class IExhibitSrv_EditDemandVP_RequestStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final javax.xml.namespace.QName ns1_DemandID_QNAME = new QName("", "DemandID");

    private static final javax.xml.namespace.QName ns3_int_TYPE_QNAME = SchemaConstants.QNAME_TYPE_INT;

    private CombinedSerializer ns3_myns3__int__int_Int_Serializer;

    private static final javax.xml.namespace.QName ns1_LotValue_QNAME = new QName("", "LotValue");

    private static final javax.xml.namespace.QName ns3_double_TYPE_QNAME = SchemaConstants.QNAME_TYPE_DOUBLE;

    private CombinedSerializer ns3_myns3__double__double_Double_Serializer;

    private static final javax.xml.namespace.QName ns1_Price_QNAME = new QName("", "Price");

    private static final javax.xml.namespace.QName ns1_DateEnd_QNAME = new QName("", "DateEnd");

    private static final javax.xml.namespace.QName ns3_dateTime_TYPE_QNAME = SchemaConstants.QNAME_TYPE_DATE_TIME;

    private CombinedSerializer ns3_myns3_dateTime__java_util_Calendar_DateTimeCalendar_Serializer;

    private static final int myDEMANDID_INDEX = 0;

    private static final int myLOTVALUE_INDEX = 1;

    private static final int myPRICE_INDEX = 2;

    private static final int myDATEEND_INDEX = 3;

    public IExhibitSrv_EditDemandVP_RequestStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns3_myns3__int__int_Int_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, int.class, ns3_int_TYPE_QNAME);
        ns3_myns3__double__double_Double_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, double.class, ns3_double_TYPE_QNAME);
        ns3_myns3_dateTime__java_util_Calendar_DateTimeCalendar_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.util.Calendar.class, ns3_dateTime_TYPE_QNAME);
    }

    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_EditDemandVP_RequestStruct instance = new pack.IExhibitSrv_EditDemandVP_RequestStruct();
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        reader.nextElementContent();
        for (int i = 0; i < 4; i++) {
            elementName = reader.getName();
            if (reader.getState() == XMLReader.END) {
                break;
            }
            if (elementName.equals(ns1_DemandID_QNAME)) {
                member = ns3_myns3__int__int_Int_Serializer.deserialize(ns1_DemandID_QNAME, reader, context);
                instance.setDemandID(((java.lang.Integer) member).intValue());
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_LotValue_QNAME)) {
                member = ns3_myns3__double__double_Double_Serializer.deserialize(ns1_LotValue_QNAME, reader, context);
                instance.setLotValue(((Double) member).doubleValue());
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_Price_QNAME)) {
                member = ns3_myns3__double__double_Double_Serializer.deserialize(ns1_Price_QNAME, reader, context);
                instance.setPrice(((Double) member).doubleValue());
                reader.nextElementContent();
                continue;
            }
            if (elementName.equals(ns1_DateEnd_QNAME)) {
                member = ns3_myns3_dateTime__java_util_Calendar_DateTimeCalendar_Serializer.deserialize(ns1_DateEnd_QNAME, reader, context);
                instance.setDateEnd((java.util.Calendar) member);
                reader.nextElementContent();
                continue;
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] { ns1_DateEnd_QNAME, elementName });
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object) instance : (java.lang.Object) state);
    }

    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_EditDemandVP_RequestStruct instance = (pack.IExhibitSrv_EditDemandVP_RequestStruct) obj;
    }

    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        pack.IExhibitSrv_EditDemandVP_RequestStruct instance = (pack.IExhibitSrv_EditDemandVP_RequestStruct) obj;
        ns3_myns3__int__int_Int_Serializer.serialize(new java.lang.Integer(instance.getDemandID()), ns1_DemandID_QNAME, null, writer, context);
        ns3_myns3__double__double_Double_Serializer.serialize(new Double(instance.getLotValue()), ns1_LotValue_QNAME, null, writer, context);
        ns3_myns3__double__double_Double_Serializer.serialize(new Double(instance.getPrice()), ns1_Price_QNAME, null, writer, context);
        ns3_myns3_dateTime__java_util_Calendar_DateTimeCalendar_Serializer.serialize(instance.getDateEnd(), ns1_DateEnd_QNAME, null, writer, context);
    }
}
