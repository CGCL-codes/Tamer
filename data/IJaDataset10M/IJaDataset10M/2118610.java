package tjger.lib;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This interface is used by the XmlUtil to read and write maps from/to xml.
 * 
 * @author hagru
 */
public interface XmlMapConverter {

    /**
     * Write the key and the value into the node (as attributes or sub-nodes).
     * 
     * @param itemNode The xml node for the item.
     * @param key The key to save.
     * @param value The value to save.
     */
    public void writeNode(Element itemNode, Object key, Object value);

    /**
     * Read the key for the map entry from the node.
     * 
     * @param itemNode The node to extract the key from.
     * @return The key object, if null this entry is ignored.
     */
    public Object readKey(Node itemNode);

    /**
     * Read the value for the map entry from the node.
     * 
     * @param itemNode The node to extract the value from.
     * @return The value object, can also be null to be used as value.
     */
    public Object readValue(Node itemNode);
}
