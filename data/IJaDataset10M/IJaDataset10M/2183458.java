package net.sf.xmlprocessor.xml.xsd.instances;

import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <code>XSAbstractNodeList</code> is the base class of the various
 * implementation of the <code>NodeList</code> interface. It provides the
 * required methods for the <code>NodeList</code> including a deep
 * <code>equals</code>.
 * 
 * @author Emond Papegaaij
 */
public abstract class XSAbstractNodeList implements NodeList {

    /**
	 * Collects the children of the parent node. The implementation of this
	 * method can choose to cache the results. Every access to the list will go
	 * through this method.
	 * @return The children of the parent node.
	 */
    protected abstract List<Node> collectSubNodes();

    /**
	 * Returns the length of the list of children.
	 * 
	 * @return The length of the list of children.
	 */
    public int getLength() {
        return collectSubNodes().size();
    }

    /**
	 * Returns the node at the given index.
	 * 
	 * @param index The index of the node to return.
	 * @return The node at the given index.
	 */
    public Node item(int index) {
        return collectSubNodes().get(index);
    }

    /**
	 * Checks of the given object is an instance of <code>NodeList</code> that
	 * contains the same nodes at the same indices.
	 * 
	 * @param o The object to compare this list to.
	 * @return True when the given object is equal to this list.
	 */
    public boolean equals(Object o) {
        if (o instanceof NodeList) {
            NodeList other = (NodeList) o;
            List<Node> nodes = collectSubNodes();
            if (other.getLength() != nodes.size()) {
                return false;
            }
            for (int count = 0; count < nodes.size(); count++) {
                if (!nodes.get(count).isEqualNode(other.item(count))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
