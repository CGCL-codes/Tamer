package net.sourceforge.esw.graph;

import net.sourceforge.esw.graph.factory.INodeFactory;

/**
 * Defines the methods for creation of <code>INode</code> instances.
 * <p>
 *
 *
 * This is the default implementation of the <code>INodeFactory</code> interface
 * which simply creates and returns the default ESW implementation of the
 * <code>INode</code> interface.
 * <p>
 *
 * This is the <code>Abstract Factory</code> of the Abstract Factory design
 * pattern.
 * <p>
 *
 * "Pluggable factories" allow the application to add its own custom
 * implementations of the <code>INode</code> interface to the framework in a
 * consitent, simple way. Custom implementations of the <code>INode</code>
 * interface can thus be used as if they were a standard part of the framework.
 * <p>
 *
 * Examples:
 * <pre>
 *   MyCustomNodeFactory customFactory = new MyCustomNodeFactory();
 *   NodeFactory.setNodeFactory( customFactory );
 * </pre>
 * <p>
 *
 *
 * @stereotype factory
 */
public class DefaultNodeFactory implements INodeFactory {

    /****************************************************************************
   * Creates a new empty <code>INode</code> instance.
   *
   * @return an empty <code>INode</code> instance.
   */
    public INode createNode() {
        return new Node();
    }

    /****************************************************************************
   * Creates a new <code>INode</code> instance with the specified identifier.
   *
   * @param aID the identifier used by the <code>INode</code> instance.
   *
   * @return an <code>INode</code> instance with the specified identifier.
   */
    public INode createNode(Object aID) {
        INode node = new Node();
        node.setID(aID);
        return node;
    }

    /****************************************************************************
   * Creates a new <code>INode</code> instance with the specified identifier
   * and value.
   *
   * @param aID the identifier used by the <code>INode</code> instance.
   * @param aValue the value used by the <code>INode</code> instance.
   *
   * @return an <code>INode</code> instance with the specified identifier and
   *         value.
   */
    public INode createNode(Object aID, Object aValue) {
        INode node = new Node();
        node.setID(aID);
        node.setValue(aValue);
        return node;
    }
}
