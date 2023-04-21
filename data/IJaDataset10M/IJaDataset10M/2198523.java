package networkcontroller.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * class describing data of alive packet
 * 
 * @author maciek
 * 
 */
public class XMLAlive extends XML {

    /**
	 * creates xml alive
	 * 
	 * @param host
	 *            source host
	 * @param port
	 *            source port
	 */
    public XMLAlive(String host, int port) {
        super();
        createNewDocument();
        Node aliveNode = document.createElement("alive");
        document.appendChild(aliveNode);
        Node senderNode = document.createElement("sender");
        aliveNode.appendChild(senderNode);
        Node hostNode = document.createElement("host");
        hostNode.setTextContent(host);
        Node portNode = document.createElement("port");
        portNode.setTextContent(String.valueOf(port));
        senderNode.appendChild(hostNode);
        senderNode.appendChild(portNode);
    }

    /**
	 * creates xml alive from data
	 * 
	 * @param XMLData
	 *            byte array of data
	 */
    public XMLAlive(byte[] XMLData) {
        super(XMLData);
    }

    /**
	 * gets source host
	 * 
	 * @return source host
	 */
    public String getHost() {
        NodeList hosts = document.getElementsByTagName("host");
        return hosts.item(0).getTextContent();
    }

    /**
	 * gets source host port
	 * 
	 * @return source host port
	 */
    public int getPort() {
        NodeList ports = document.getElementsByTagName("port");
        return Integer.parseInt(ports.item(0).getTextContent());
    }
}
