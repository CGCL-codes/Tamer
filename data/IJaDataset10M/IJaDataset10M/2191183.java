package it.uniroma1.dis.omega.upnpqos.structure;

import it.uniroma1.dis.omega.upnpqos.argument.ArgumentException;
import it.uniroma1.dis.omega.upnpqos.utils.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class v2TrafficSpecification extends Tspec {

    public v2TrafficSpecification() {
        super();
    }

    /**
	 * 
	 * @param n
	 * @throws ArgumentException
	 */
    public v2TrafficSpecification(Node node) throws ArgumentException {
        super(node);
    }

    protected void init() {
        super.init();
    }

    protected void readNode(Node node) throws ArgumentException {
        super.readNode(node);
        if (nextVersionNode != null) {
            NodeList nl = nextVersionNode.getChildNodes();
            nextVersionNode = null;
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n.getNodeName().endsWith("v3TrafficSpecification")) {
                    nextVersionNode = n;
                } else if (Util.ignoreTag(n.getNodeName())) {
                } else {
                    throw new ArgumentException("v2TrafficSpecification structure contains the following unknown tag: " + n.getNodeName());
                }
            }
        }
    }

    /**	 
	 * @return the XML fragment representing the Tspec
	 */
    public String getXMLFragment() {
        return this.getXMLFragment("", "", "Tspec");
    }

    /**	 
	 * @return the XML fragment representing the Tspec with a namespace
	 */
    public String getXMLFragment(String namespace) {
        return this.getXMLFragment("", namespace, "Tspec");
    }

    /**	 
	 * @return the XML fragment representing the Tspec with a namespace and a customized external tag name
	 */
    public String getXMLFragment(String namespace, String externalTag) {
        return this.getXMLFragment("", namespace, externalTag);
    }

    protected String getXMLFragment(String extension, String namespace, String externalTag) {
        String xml = "<v2TrafficSpecification>";
        xml += extension + "</v2TrafficSpecification>";
        return super.getXMLFragment(xml, namespace, externalTag);
    }
}
