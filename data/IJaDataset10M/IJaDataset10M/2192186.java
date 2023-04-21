package it.uniroma1.dis.omega.upnpqos.structure;

import java.util.LinkedList;
import java.util.List;
import it.uniroma1.dis.omega.upnpqos.argument.ArgumentException;
import it.uniroma1.dis.omega.upnpqos.utils.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LinkReachableMacs {

    protected static final String str_name = "LinkReachableMacs";

    /**
	 * LinkId: This is a REQUIRED field. Its value is of type string and contains the InterfaceId as defined in
	 * section 2.2.6.2. Note: this field was named LinkId in v2 although this field actually contains the
	 * InterfaceId; this name MUST be retained for backward compatibility,
	 */
    public String LinkId;

    private boolean LinkIdFound;

    /**
	 * BridgeId: This is an OPTIONAL field. It is of type string. Interfaces (links) that are interconnected (bridged)
	 * within the device at L2 are identified with the same value for BridgeId.
	 */
    public String BridgeId;

    /**
	 * MacAddress: This is an OPTIONAL field. It MUST be provided when supported by the technology. It
	 * provides the MAC address of the interface for an end point device.
	 */
    public String MacAddress;

    /**
	 * ReachableMac: This is an OPTIONAL structure. It MUST be provided when MAC addresses are supported
	 * by the technology. It provides the MAC address(es) of end point devices that are reachable through the
	 * link, if any. The device MUST list all MAC address that it currently knows for the link.
	 */
    public List<String> ReachableMac;

    /**
	 * This is a repeating field. Each instance contains information describing a single
	 * rotameter observation.
	 */
    public List<RotameterObservation> RotameterObservation;

    protected Node nextVersionNode;

    public LinkReachableMacs() {
        init();
    }

    /**
	 * 
	 * @param n
	 * @throws ArgumentException
	 */
    public LinkReachableMacs(Node n) throws ArgumentException {
        init();
        readNode(n);
        checkMandatoryFields();
    }

    protected void init() {
        nextVersionNode = null;
        LinkId = null;
        LinkIdFound = false;
        BridgeId = null;
        MacAddress = null;
        ReachableMac = new LinkedList<String>();
        RotameterObservation = new LinkedList<RotameterObservation>();
    }

    private void checkMandatoryFields() throws ArgumentException {
        checkMandatoryFields("");
    }

    protected void checkMandatoryFields(String msg) throws ArgumentException {
        int count = 0;
        if (!msg.equals("")) count++;
        if (!LinkIdFound) {
            if (count > 0) msg += "and ";
            msg += "LinkId ";
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
            if (n.getNodeName().endsWith("LinkId")) {
                LinkId = Util.getString(n);
                LinkIdFound = true;
            } else if (n.getNodeName().endsWith("BridgeId")) {
                BridgeId = Util.getString(n);
            } else if (n.getNodeName().endsWith("MacAddress")) {
                MacAddress = Util.getString(n);
            } else if (n.getNodeName().endsWith("ReachableMac")) {
                String mac = Util.getString(n);
                ReachableMac.add(mac);
            } else if (n.getNodeName().endsWith("RotameterObservation")) {
                RotameterObservation.add(new v3RotameterObservation(n));
            } else if (n.getNodeName().endsWith("v2")) {
                nextVersionNode = n;
            } else if (n.getNodeName().endsWith("v3")) {
                nextVersionNode = n;
            } else if (Util.ignoreTag(n.getNodeName())) {
            } else {
                throw new ArgumentException(str_name + " structure contains the following unknown tag: " + n.getNodeName());
            }
        }
    }

    /**
	 * 
	 * @return the XML fragment representing a full XML file
	 */
    public String getXMLFile() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + getXMLFragment();
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
        xml += "<" + namespace + "LinkId>" + LinkId + "</" + namespace + "LinkId>";
        if (BridgeId != null) xml += "<" + namespace + "BridgeId>" + BridgeId + "</" + namespace + "BridgeId>";
        if (MacAddress != null) xml += "<" + namespace + "MacAddress>" + MacAddress + "</" + namespace + "MacAddress>";
        for (int i = 0; i < ReachableMac.size(); i++) {
            xml += "<" + namespace + "ReachableMac>" + ReachableMac.get(i) + "</" + namespace + "ReachableMac>";
        }
        xml += extension;
        for (int i = 0; i < RotameterObservation.size(); i++) {
            xml += RotameterObservation.get(i).getXMLFragment(namespace);
        }
        xml += "</" + externalTag + ">";
        return xml;
    }
}
