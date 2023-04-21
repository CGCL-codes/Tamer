package edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom;

import org.w3c.dom.Document;

/**
 *  Extends the W3C Document class with utility methods
 * 
 *  @author Andrew Hogue
 */
public interface ITree extends Document {

    /**
     *  Returns the size (number of nodes) of the tree.
     */
    public int getSize();

    /**
     *  Returns the nodes of this tree, in postorder 
     */
    public INode[] getNodes();
}
