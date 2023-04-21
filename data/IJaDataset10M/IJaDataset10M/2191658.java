package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class WSFrame_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final QName ns1_uid_QNAME = new QName("", "uid");

    private static final QName ns3_long_TYPE_QNAME = SchemaConstants.QNAME_TYPE_LONG;

    private CombinedSerializer ns3_myns3__long__long_Long_Serializer;

    private static final int myUID_INDEX = 0;

    public WSFrame_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns3_myns3__long__long_Long_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, long.class, ns3_long_TYPE_QNAME);
    }

    public Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.WSFrame instance = new de.bea.services.vidya.client.datasource.types.WSFrame();
        Object member;
        boolean isComplete = true;
        QName elementName;
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_uid_QNAME)) {
                member = ns3_myns3__long__long_Long_Serializer.deserialize(ns1_uid_QNAME, reader, context);
                instance.setUid(((Long) member).longValue());
                reader.nextElementContent();
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (Object) instance : (Object) state);
    }

    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.WSFrame instance = (de.bea.services.vidya.client.datasource.types.WSFrame) obj;
    }

    public void doSerializeInstance(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.WSFrame instance = (de.bea.services.vidya.client.datasource.types.WSFrame) obj;
        ns3_myns3__long__long_Long_Serializer.serialize(new Long(instance.getUid()), ns1_uid_QNAME, null, writer, context);
    }
}
