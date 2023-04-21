package it.uniroma1.dis.omega.upnpqos.structure;

import it.uniroma1.dis.omega.upnpqos.argument.ArgumentException;
import it.uniroma1.dis.omega.upnpqos.utils.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Interface {

    private static final String str_name = "Interface";

    public static final String Prioritized = "Prioritized";

    public static final String BestEffort = "BestEffort";

    /**
	 * MacAddress: This is an OPTIONAL field. If a given interface has an associated MAC address, the QosDevice
	 * MUST provide this information here. It provides the MAC address of the Interface and is of type
	 * MacAddressType (defined in the schema). 
	 */
    public String MacAddress;

    /**
	 * InterfaceId: This is a REQUIRED field. The value is of type string and MUST uniquely identify an interface
	 * within the QosDevice Service. Furthermore, the InterfaceId MUST remain the same for a given interface
	 * (L2 Technology) until the QosDevice Service reboots.
	 */
    public String InterfaceId;

    private boolean InterfaceIdFound;

    /**
	 * IanaTechnologyType: This is an OPTIONAL integer field. The IanaTechnologyType (IANA uses the
	 * designation IANAifType) is an integer assigned by IANA for any media type, such as a value of 6 for
	 * 802.3 media type or a value of 71 for 802.11 media type The allowed integer values for this parameter are
	 * specified in [IANA].
	 */
    public int IanaTechnologyType;

    /**
	 * AdmissionControlSupported: This is a REQUIRED enumeration field. This field is maintained for backward
	 * compatibility. This field can report only one of two values "Yes" or "No".. QosManager:3 ignores the
	 * value of this field.
	 */
    public boolean AdmissionControlSupported;

    private boolean AdmissionControlSupportedFound;

    /**
	 * PacketTaggingSupported: This is a REQUIRED enumeration field. PacketTaggingSupported field indicates
	 * whether the device is capable of tagging L2 priorities on the outgoing interface. This field can report only
	 * one of two values "Yes" or "No".
	 */
    public boolean PacketTaggingSupported;

    private boolean PacketTaggingSupportedFound;

    /**
	 * NativeQos: This is an OPTIONAL enumeration field. To ensure backward compatibility, this field MUST
	 * contain one of the values (Prioritized, BestEffort).
	 */
    public String NativeQos;

    /**
	 * MaxPhyRate: Indicates the maximum PHY rate of the interface and expressed as a value of type
	 * unsignedInt. This parameter is REQUIRED and indicates (Units) phy rate measured in bits/sec.
	 */
    public long MaxPhyRate;

    private boolean MaxPhyRateFound;

    protected Node nextVersionNode = null;

    public Interface() {
        init();
    }

    /**
	 * 
	 * @param n
	 * @throws ArgumentException
	 */
    public Interface(Node n) throws ArgumentException {
        init();
        readNode(n);
        checkMandatoryFields();
    }

    protected void init() {
        MacAddress = null;
        InterfaceId = null;
        InterfaceIdFound = false;
        IanaTechnologyType = -1;
        AdmissionControlSupported = false;
        AdmissionControlSupportedFound = false;
        PacketTaggingSupported = false;
        PacketTaggingSupportedFound = false;
        NativeQos = null;
        MaxPhyRate = -1;
        MaxPhyRateFound = false;
    }

    private void checkMandatoryFields() throws ArgumentException {
        checkMandatoryFields("");
    }

    protected void checkMandatoryFields(String msg) throws ArgumentException {
        int count = 0;
        if (!msg.equals("")) count++;
        if (!InterfaceIdFound) {
            if (count > 0) msg += "and ";
            msg += "InterfaceId ";
            count++;
        }
        if (!AdmissionControlSupportedFound) {
            if (count > 0) msg += "and ";
            msg += "AdmissionControlSupported ";
            count++;
        }
        if (!PacketTaggingSupportedFound) {
            if (count > 0) msg += "and ";
            msg += "PacketTaggingSupported ";
            count++;
        }
        if (!MaxPhyRateFound) {
            if (count > 0) msg += "and ";
            msg += "MaxPhyRate ";
            count++;
        }
        if (count > 0) {
            if (count > 1) msg += "are "; else msg += "is ";
            msg += "missing";
            throw new ArgumentException(msg);
        }
    }

    protected void readNode(Node node) throws ArgumentException {
        Node n = null;
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            n = nl.item(i);
            if (n.getNodeName().endsWith("InterfaceId")) {
                InterfaceId = Util.getString(n);
                InterfaceIdFound = true;
            } else if (n.getNodeName().endsWith("MacAddress")) {
                MacAddress = Util.getString(n);
            } else if (n.getNodeName().endsWith("IanaTechnologyType")) {
                IanaTechnologyType = Util.getInteger(n);
            } else if (n.getNodeName().endsWith("AdmissionControlSupported")) {
                AdmissionControlSupported = Util.getBoolean(n);
                AdmissionControlSupportedFound = true;
            } else if (n.getNodeName().endsWith("PacketTaggingSupported")) {
                PacketTaggingSupported = Util.getBoolean(n);
                PacketTaggingSupportedFound = true;
            } else if (n.getNodeName().endsWith("NativeQos")) {
                NativeQos = Util.getString(n);
            } else if (n.getNodeName().endsWith("MaxPhyRate")) {
                MaxPhyRate = Util.getLong(n);
                MaxPhyRateFound = true;
            } else if (n.getNodeName().endsWith("v2")) {
                nextVersionNode = n;
            } else if (Util.ignoreTag(n.getNodeName())) {
            } else {
                throw new ArgumentException(str_name + " structure contains the following unknown tag: " + n.getNodeName());
            }
        }
    }

    /**
	 * 
	 * @return the XML fragment with no XML header
	 */
    public String getXMLFragment() {
        return getXMLFragment("");
    }

    public String getXMLFragment(String namespace) {
        return getXMLFragment(namespace, str_name);
    }

    public String getXMLFragment(String namespace, String externalTag) {
        return getXMLFragment(namespace, externalTag, "");
    }

    /**
	 * 	  
	 * @param extension next version tags
	 * @param namespace should be comprehensive of ":"!
	 * @param externalTag rename the TrafficDescriptor tag with something else...
	 * @return
	 */
    protected String getXMLFragment(String namespace, String externalTag, String extension) {
        String xml = "<" + externalTag + ">";
        xml += "<" + namespace + "InterfaceId>" + InterfaceId + "</" + namespace + "InterfaceId>";
        if (MacAddress != null) xml += "<" + namespace + "MacAddress>" + MacAddress + "</" + namespace + "MacAddress>";
        if (IanaTechnologyType >= 0) xml += "<" + namespace + "IanaTechnologyType>" + IanaTechnologyType + "</" + namespace + "IanaTechnologyType>";
        xml += "<" + namespace + "AdmissionControlSupported>" + Util.getXMLBoolean_Yes_No(AdmissionControlSupported) + "</" + namespace + "AdmissionControlSupported>";
        xml += "<" + namespace + "PacketTaggingSupported>" + Util.getXMLBoolean_Yes_No(PacketTaggingSupported) + "</" + namespace + "PacketTaggingSupported>";
        if (NativeQos != null) xml += "<" + namespace + "NativeQos>" + NativeQos + "</" + namespace + "NativeQos>";
        xml += "<" + namespace + "MaxPhyRate>" + MaxPhyRate + "</" + namespace + "MaxPhyRate>";
        xml += extension + "</" + externalTag + ">";
        return xml;
    }
}
