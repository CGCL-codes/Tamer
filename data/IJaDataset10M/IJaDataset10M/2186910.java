package de.beas.explicanto.client.model;

import org.eclipse.gef.requests.CreationFactory;

/**
 * CloneFactory
 * A factory that clones nodes, for the palette creation tools.
 *
 * @author Lucian Brancovean
 * @version 1.0
 *
 */
public class CloneFactory implements CreationFactory {

    /** the node that will be cloned and returned by this factory */
    private Node prototype;

    /**
     * Creates a factory that makes clones of a node.
     * @param prototype the node to be cloned.
     * @throws CloneNotSupportedException
     */
    public CloneFactory(Node prototype) throws CloneNotSupportedException {
        this.prototype = (Node) prototype.clone();
    }

    /**
     * Returns a new cloned node.
     * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
     * @return a clone of the supplied prototype.
     */
    public Object getNewObject() {
        try {
            return prototype.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
     * @return the type of the cloned node.
     */
    public Object getObjectType() {
        return prototype.getClass();
    }
}
