package metamodel;

import org.w3c.dom.Node;

/**
 * Class that represents a enumeration item of a MetaEnumeration
 * @author Soraya Santana, Jose Manuel Martinez, Pedro J. Cabrera
 *
 */
public class MetaEnumerationItem {

    private String label = null;

    private String value = null;

    /**
	 * Every MetaEnumerationItem is constructed from a <item> node of the metamodel DOM
	 * @param itemNode DOM <item> node 
	 */
    public MetaEnumerationItem(Node itemNode) {
        if (itemNode.getNodeType() != Node.ELEMENT_NODE) return;
        unserialize(itemNode);
    }

    /**
	 * Unserializes the <item> node into MetaEnumerationItem attributes
	 * @param itemNode DOM <item> node 
	 */
    public void unserialize(Node itemNode) {
        value = itemNode.getAttributes().getNamedItem("value").getNodeValue();
        label = itemNode.getTextContent();
    }

    /**
	 * Gets the label
	 * @return item label
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * Gets item integer value
	 * @return value
	 */
    public String getValue() {
        return value;
    }
}
