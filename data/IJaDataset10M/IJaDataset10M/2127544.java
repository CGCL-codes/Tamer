package org.jazzteam.bpe.model.nodes.interfaces;

import org.jazzteam.bpe.model.nodes.Node;

/**
 * This interface must be implement all nodes that have reference to next node.
 * 
 * @author skars
 * @version $Rev: $
 */
public interface INextable {

    /**
	 * Gets next node from nodes hierarchy.
	 * 
	 * @return Returns instance of next node.
	 * @version 1
	 */
    Node getNextNode();

    /**
	 * Sets next node from nodes hierarchy.
	 * 
	 * @param nextNode
	 *            Next node to set.
	 * @version 1
	 */
    void setNextNode(Node nextNode);
}
