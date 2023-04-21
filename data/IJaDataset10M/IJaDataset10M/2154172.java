package org.tagbox.xpath.iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.tagbox.util.Log;

/**
 * abstracts a nodeset (such as the 'current nodeset' in xpath evaluation).
 * extends the NodeIterator interface with methods to determine nodeset size,
 * current node within the nodeset and position within the nodeset of the
 * current node, as well as nodeset cloning.
 */
public interface Nodeset extends NodeIterator, NodeList {

    /**
     * get the position of the current node within this nodeset
     */
    public int position();

    /**
     * get the number of nodes in this nodeset.
     * n.b. first time this is called, the nodeset has to iterate over
     * all nodes returned by the underlying node iterator.
     */
    public int size();

    /**
     * synonym for size() for org.w3c.dom.NodeList compatibility
     */
    public int getLength();

    /**
     * reset the iterator to point to the first node in the set
     */
    public void reset();

    /**
     * reset the iterator and return the first node in the Nodeset
     */
    public Node firstNode();

    /**
     * return the current (context) node of this nodeset.
     */
    public Node getCurrentNode();

    /**
     * advance or reverse to the node within this nodeset that has the 
     * given index.
     * after execution, the current node of this nodeset will be the node
     * with index
     */
    public Node getNode(int index);

    /**
     * synonym for getNode(int index) for org.w3c.dom.NodeList compatibility
     */
    public Node item(int index);

    public Nodeset cloneNodeset();
}
