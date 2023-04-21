package metamodel;

import org.w3c.dom.Node;

/**
 * Class that represents a Get operation of a MetaClass representation
 * @author Soraya Santana, Jose Manuel Martinez, Pedro J. Cabrera
 *
 */
public class MetaGet {

    private String name = null;

    private String type = null;

    private String code = null;

    /**
	 * Every MetaGet is constructed from a <get> node of the metamodel DOM
	 * @param getNode DOM <get> node 
	 */
    public MetaGet(Node getNode) {
        if (getNode.getNodeType() != Node.ELEMENT_NODE) return;
        unserialize(getNode);
    }

    /**
	 * Unserializes the <get> node into MetaGet attributes
	 * @param getNode DOM <get> node 
	 */
    public void unserialize(Node getNode) {
        name = getNode.getAttributes().getNamedItem("name").getNodeValue();
        code = getNode.getTextContent();
        if (getNode.getAttributes().getNamedItem("type") != null) type = getNode.getAttributes().getNamedItem("type").getNodeValue();
    }

    /**
	 * Gets the name of the get operation
	 * @return get name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Gets the get type
	 * @return get type
	 */
    public String getType() {
        return type;
    }

    /**
	 * Gets the code of the get operation
	 * @return get operation code
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Merges with get adding fields that are not present
	 * Used to compound a whole definition from redefinitions of gets (new code)
	 * @param get
	 */
    public void merge(MetaGet get) {
        if ((type == null) && (get.getType() != null)) type = get.getType();
        if ((code == null) && (get.getCode() != null)) code = get.getCode();
    }
}
