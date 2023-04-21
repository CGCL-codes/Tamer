package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

public class WSCourseName_SOAPSerializer extends ObjectSerializerBase implements Initializable {

    private static final QName ns1_uid_QNAME = new QName("", "uid");

    private static final QName ns3_long_TYPE_QNAME = SchemaConstants.QNAME_TYPE_LONG;

    private CombinedSerializer ns3_myns3__long__long_Long_Serializer;

    private static final QName ns1_children_QNAME = new QName("", "children");

    private static final QName ns4_list_TYPE_QNAME = InternalEncodingConstants.QNAME_TYPE_LIST;

    private CombinedSerializer ns4_myns4_list__CollectionInterfaceSerializer;

    private static final QName ns1_lockCount_QNAME = new QName("", "lockCount");

    private static final QName ns1_lockTime_QNAME = new QName("", "lockTime");

    private static final QName ns3_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;

    private CombinedSerializer ns3_myns3_string__java_lang_String_String_Serializer;

    private static final QName ns1_lockUserID_QNAME = new QName("", "lockUserID");

    private static final QName ns1_rolesX_QNAME = new QName("", "rolesX");

    private static final QName ns1_status_QNAME = new QName("", "status");

    private static final int myUID_INDEX = 0;

    private static final int myCHILDREN_INDEX = 1;

    private static final int myLOCKCOUNT_INDEX = 2;

    private static final int myLOCKTIME_INDEX = 3;

    private static final int myLOCKUSERID_INDEX = 4;

    private static final int myROLESX_INDEX = 5;

    private static final int mySTATUS_INDEX = 6;

    public WSCourseName_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }

    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns3_myns3__long__long_Long_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, long.class, ns3_long_TYPE_QNAME);
        ns4_myns4_list__CollectionInterfaceSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.util.List.class, ns4_list_TYPE_QNAME);
        ns3_myns3_string__java_lang_String_String_Serializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.lang.String.class, ns3_string_TYPE_QNAME);
    }

    public Object doDeserialize(SOAPDeserializationState state, XMLReader reader, SOAPDeserializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.WSCourseName instance = new de.bea.services.vidya.client.datasource.types.WSCourseName();
        de.bea.services.vidya.client.datasource.types.WSCourseName_SOAPBuilder builder = null;
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
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_children_QNAME)) {
                member = ns4_myns4_list__CollectionInterfaceSerializer.deserialize(ns1_children_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new de.bea.services.vidya.client.datasource.types.WSCourseName_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myCHILDREN_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setChildren((java.util.List) member);
                }
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_lockCount_QNAME)) {
                member = ns3_myns3__long__long_Long_Serializer.deserialize(ns1_lockCount_QNAME, reader, context);
                instance.setLockCount(((Long) member).longValue());
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_lockTime_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_lockTime_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new de.bea.services.vidya.client.datasource.types.WSCourseName_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myLOCKTIME_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setLockTime((java.lang.String) member);
                }
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_lockUserID_QNAME)) {
                member = ns3_myns3__long__long_Long_Serializer.deserialize(ns1_lockUserID_QNAME, reader, context);
                instance.setLockUserID(((Long) member).longValue());
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_rolesX_QNAME)) {
                member = ns4_myns4_list__CollectionInterfaceSerializer.deserialize(ns1_rolesX_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new de.bea.services.vidya.client.datasource.types.WSCourseName_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myROLESX_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setRolesX((java.util.List) member);
                }
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_status_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_status_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new de.bea.services.vidya.client.datasource.types.WSCourseName_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, mySTATUS_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setStatus((java.lang.String) member);
                }
                reader.nextElementContent();
            }
        }
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (Object) instance : (Object) state);
    }

    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.WSCourseName instance = (de.bea.services.vidya.client.datasource.types.WSCourseName) obj;
    }

    public void doSerializeInstance(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        de.bea.services.vidya.client.datasource.types.WSCourseName instance = (de.bea.services.vidya.client.datasource.types.WSCourseName) obj;
        ns3_myns3__long__long_Long_Serializer.serialize(new Long(instance.getUid()), ns1_uid_QNAME, null, writer, context);
        ns4_myns4_list__CollectionInterfaceSerializer.serialize(instance.getChildren(), ns1_children_QNAME, null, writer, context);
        ns3_myns3__long__long_Long_Serializer.serialize(new Long(instance.getLockCount()), ns1_lockCount_QNAME, null, writer, context);
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getLockTime(), ns1_lockTime_QNAME, null, writer, context);
        ns3_myns3__long__long_Long_Serializer.serialize(new Long(instance.getLockUserID()), ns1_lockUserID_QNAME, null, writer, context);
        ns4_myns4_list__CollectionInterfaceSerializer.serialize(instance.getRolesX(), ns1_rolesX_QNAME, null, writer, context);
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getStatus(), ns1_status_QNAME, null, writer, context);
    }
}
