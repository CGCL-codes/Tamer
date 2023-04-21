package com.volantis.mcs.wbdom;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.dom.NodeAnnotation;

/**
 * The base class for nodes which represent the common tree structure of a DOM
 * for WBDOM.
 * <p>
 * This uses a very simple representation which is optimised for read only
 * behaviour. If you want to modify existing elements, this class may need 
 * to be modified to look more like the MCS DOM version.
 * <p>
 * WBDOM nodes act as a tree structured facade over another level of objects 
 * which contain the actual WBSAX value objects which store the data content. 
 */
public abstract class WBDOMNode implements WBDOMVisitor.Acceptor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(WBDOMNode.class);

    /**
     * The parent element, is null if this is the root node.
     * <p>
     * This is package private so it can be accessed by other classes in this
     * package.
     * </p>
     */
    WBDOMElement parent;

    /**
     * The next node, is null if this is the last node in the list.
     * <p>
     * This is package private so it can be accessed by other classes in this
     * package.
     * </p>
     */
    WBDOMNode next;

    /**
     * Some user data associated with the node.
     */
    private NodeAnnotation annotation;

    /**
     * Create a new <code>Node</code>.
     */
    WBDOMNode() {
    }

    /**
     * Get the parent element.
     * @return The parent element.
     */
    public WBDOMElement getParent() {
        return parent;
    }

    /**
     * Get the next node.
     * @return The next node.
     */
    public WBDOMNode getNext() {
        return next;
    }

    /**
     * Set the object.
     * @param annotation The object.
     */
    public void setAnnotation(NodeAnnotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Get the object.
     * @return The object.
     */
    public NodeAnnotation getAnnotation() {
        return annotation;
    }
}
