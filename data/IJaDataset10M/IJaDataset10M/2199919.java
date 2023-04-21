package net.sf.orcc.network.serialize;

import org.w3c.dom.Node;

/**
 * This class defines a parse continuation, by storing the next node that shall
 * be parsed along with the result already computed.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class ParseContinuation<T> {

    private final Node node;

    private final T result;

    /**
	 * Creates a new parse continuation with the given DOM node and result. The
	 * constructor stores the next sibling of node.
	 * 
	 * @param node
	 *            a node that will be used to resume parsing after the result
	 *            has been stored
	 * @param result
	 *            the result
	 */
    public ParseContinuation(Node node, T result) {
        if (node == null) {
            this.node = null;
        } else {
            this.node = node.getNextSibling();
        }
        this.result = result;
    }

    /**
	 * Returns the node stored in this continuation.
	 * 
	 * @return the node stored in this continuation
	 */
    public Node getNode() {
        return node;
    }

    /**
	 * Returns the result stored in this continuation.
	 * 
	 * @return the result stored in this continuation
	 */
    public T getResult() {
        return result;
    }
}
