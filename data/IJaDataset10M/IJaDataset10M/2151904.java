package it.essepuntato.earmark.core;

/**
 * This interface defines all the methods that an EARMARK node have to implement.
 * We call EARMARK node an EARMARK item that takes part in the graph structure of
 * the document, i.e. all the markup items, the ranges and the document itself.
 * 
 * @author Silvio Peroni
 *
 */
public interface EARMARKNode extends EARMARKItem {

    /**
	 * An enumeration of all the kinds of EARMARK node.
	 * 
	 * @author Silvio Peroni
	 */
    public enum Type {

        /**
		 * If the EARMARK node is an element. 
		 */
        Element, /**
		 * If the EARMARK node is an attribute.
		 */
        Attribute, /**
		 * If the EARMARK node is a comment.
		 */
        Comment, /**
		 * If the EARMARK node is a pointer range.
		 */
        PointerRange, /**
		 * If the EARMARK node is a xpath pointer range.
		 */
        XPathPointerRange, /**
		 * If the EARMARK node is a EARMARK document.
		 */
        Document
    }

    /**
	 * <p>This method clones the node.</p>
	 * <p>The algorithm used for cloning may change depending on the particular
	 * type (EAMRARKNode.Type) of the calling node.</p>
	 * 
	 * @param deep if true, recursively clone the structure under the specified node; if 
	 * false, clone only the node itself without recursion.
	 *  
	 * @return the cloned node.
	 */
    public EARMARKNode cloneNode(boolean deep);

    /**
	 * A code representing the type of the underlying earmark node.
	 * 
	 * @return a code among those defined above.
	 */
    public EARMARKNode.Type getNodeType();

    /**
	 * The EARMARKDocument object associated with this node.
	 * 
	 * @return The EARMARKDocument object associated with this node.
	 */
    public EARMARKDocument getOwnerDocument();

    /**
	 * This method returns the text content of this node and its descendants.
	 * 
	 * @return the text content of this node, or null if it does not exist.
	 */
    public String getTextContent();

    /**
	 * Retrieves the object associated to a key on a this node. The object must 
	 * first have been set to this node by calling setUserData with the same key.
	 * 
	 * @param key the key the object is associated to. 
	 * @return the object associated to the given key on this node, 
	 * or null if there was none.
	 */
    public Object getUserData(String key);

    /**
	 * <p>This method checks if the node is equal to another.</p>
	 * <p>The algorithm used for checking the equality may change depending on the particular
	 * type (EAMRARKNode.Type) of the calling node.</p>
	 * 
	 * @param node the node to compare equality with.
	 * @return true if the nodes are equal, false otherwise.
	 */
    public boolean isEqualNode(EARMARKNode node);

    /**
	 * <p>This method checks if the node is structurally equivalent to another.</p>
	 * <p>The algorithm used for checking the equality may change depending on the particular
	 * type (EAMRARKNode.Type) of the calling node.</p>
	 * 
	 * @param node the node to compare equality with.
	 * @return true if the nodes are equal, false otherwise.
	 */
    public boolean isStructurallyEqualNode(EARMARKNode node);

    /**
	 * This methods check if two nodes are the same, i.e., they have the same identifier. 
	 * 
	 * @param node the node to test against. 
	 * @return true if the nodes are the same, false otherwise. 
	 */
    public boolean isSameNode(EARMARKNode node);

    /**
	 * Associate an object to a key on this node. The object can later be retrieved 
	 * from this node by calling getUserData with the same key.
	 * 
	 * @param key the key to associate the object to.
	 * @param data the object to associate to the given key, or null to remove any 
	 * existing association to that key.
	 */
    public void setUserData(String key, Object data);
}
