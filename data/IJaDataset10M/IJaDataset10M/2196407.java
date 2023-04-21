package net.lagerwey.xml;

/**
 * Listener class that will receive events when a complete child node is parsed by the XmlReader.
 * @author lagerweij
 */
public interface XmlReaderListener {

    /**
	 * When a child node is parsed. The getParent() method will result in the root node, but with only a single child node.
	 * @param node The node that is parsed.
	 */
    public void node(XmlNode node);
}
