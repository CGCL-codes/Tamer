package org.htmlparser.util;

import org.htmlparser.Node;

/**
 * The HTMLSimpleEnumeration interface is similar to NodeIterator,
 * except that it does not throw exceptions. This interface is useful
 * when using HTMLVector, to enumerate through its elements in a simple
 * manner, without needing to do class casts for Node.
 * @author Somik Raha
 */
public interface SimpleNodeIterator extends NodeIterator {

    /**
     * Check if more nodes are available.
     * @return <code>true</code> if a call to <code>nextHTMLNode()</code> will
     * succeed.
     */
    public boolean hasMoreNodes();

    /**
     * Get the next node.
     * @return The next node in the HTML stream, or null if there are no more
     * nodes.
     */
    public Node nextNode();
}
