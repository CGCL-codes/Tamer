package it.uniroma1.dis.omega.upnpqos.structure;

import it.uniroma1.dis.omega.upnpqos.argument.ArgumentException;
import it.uniroma1.dis.omega.upnpqos.utils.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PerStreamConfiguration {

    /**
	 * This is an OPTIONAL field. This is the MAC address of an interface on a QosDevice Service 
	 * at which the Rotameter information is provided in the report. This parameter can be 
	 * configured by the Control Point. Refer to description of ROAddr field in section 2.2.12.3
	 * of QosDevice:3 specification.
	 */
    public String ROAddr;

    public boolean ROAddrFound = false;

    /**
	 * This is an OPTIONAL field. This identifies a unique UPnP traffic stream flowing through a 
	 * QosDevice Service. A Control Point optionally specifies this field if it is interested in 
	 * obtaining information for a specific UPnP traffic stream. 
	 */
    public String TrafficHandle;

    public boolean TrafficHandleFound = false;

    /**
	 * This is an OPTIONAL field. It uniquely identifies a network segment of a shared media 
	 * technolgy. A Control Point optionally specifies this field if it is interested in obtaining 
	 * information for a specific traffic stream identified by Layer2StreamIds on that QosSegmentId.  
	 */
    public String QosSegmentId;

    public boolean QosSegmentIdFound = false;

    /**
	 * This is an optional field. This identifies a unique traffic stream flowing through a QoS Segment
	 * identified by a QosSegmentId. A Control Point optionally specifies this field if it is interested 
	 * in obtaining information for a specific traffic stream identified by a Layer2StreamId on the 
	 * specified QosSegmentId. Layer2StreamId is defined in section 2.2.17.7 of QosDevice:3.
	 */
    public String Layer2StreamId;

    private boolean Layer2StreamIdFound = false;

    protected Node nextVersionNode = null;

    public PerStreamConfiguration() {
    }

    /**
	 * 
	 * @param n
	 * @throws ArgumentException
	 */
    public PerStreamConfiguration(Node n) throws ArgumentException {
        readNode(n);
    }

    protected void readNode(Node node) throws ArgumentException {
        Node n = null;
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            n = nl.item(i);
            if (n.getNodeName().endsWith("ROAddr")) {
                ROAddr = Util.getString(n);
                ROAddrFound = true;
            } else if (n.getNodeName().endsWith("TrafficHandle")) {
                TrafficHandle = Util.getString(n);
                TrafficHandleFound = true;
            } else if (n.getNodeName().endsWith("QosSegmentId")) {
                QosSegmentId = Util.getString(n);
                QosSegmentIdFound = true;
            } else if (n.getNodeName().endsWith("Layer2StreamId")) {
                Layer2StreamId = Util.getString(n);
                Layer2StreamIdFound = true;
            } else if (n.getNodeName().endsWith("v2")) {
                nextVersionNode = n;
            } else if (Util.ignoreTag(n.getNodeName())) {
            } else {
                throw new ArgumentException("PerStreamConfiguration structure contains the following unknown tag: " + n.getNodeName());
            }
        }
    }

    /**
	 * 
	 * @return the XML fragment with no XML header
	 */
    public String getXMLFragment() {
        return getXMLFragment("", "");
    }

    /**
	 * 
	 * @return the XML fragment with no XML header with a printed namespace comprehensive of : (e.g. td:)
	 */
    public String getXMLFragment(String namespace) {
        return getXMLFragment("", namespace);
    }

    protected String getXMLFragment(String extension, String namespace) {
        String xml = "<PerStreamConfiguration>";
        if (ROAddrFound) xml += "<" + namespace + "ROAddr>" + ROAddr + "</" + namespace + "ROAddr" + ">";
        if (TrafficHandleFound) xml += "<" + namespace + "TrafficHandle>" + TrafficHandle + "</" + namespace + "TrafficHandle" + ">";
        if (QosSegmentIdFound) xml += "<" + namespace + "QosSegmentId>" + QosSegmentId + "</" + namespace + "QosSegmentId" + ">";
        if (Layer2StreamIdFound) xml += "<" + namespace + "Layer2StreamId>" + Layer2StreamId + "</" + namespace + "Layer2StreamId" + ">";
        xml += extension + "</PerStreamConfiguration>";
        return xml;
    }
}
