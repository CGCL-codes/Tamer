package org.effdom.me;

/**
 * @author <a href="mailto:mattias@effcode.com">Mattias Jonsson</a>
 */
public interface Node {

    /**
     * Returns the node's ID specified as a <code>short</code>.
     * 
     * @return the node ID
     */
    public short id();

    /**
     * Returns the length in bytes that this node will have in when exported to
     * the EffDom data format.
     * 
     * @return
     */
    public int length() throws IllegalStateException;

    /**
     * Returns the parent to this node.
     * 
     * @return the node's parent
     */
    public Element parent() throws IllegalStateException;

    /**
     * Returns <code>true</code> if this node has a parent otherwise
     * <code>false</code> is returned.
     * 
     * @return <code>true</code> if the node has a parent.
     */
    public boolean hasParent();
}
