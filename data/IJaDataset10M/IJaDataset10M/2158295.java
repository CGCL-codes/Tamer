package com.groovyj.jgprog;

/** The base class of all terminals and functions. A generic node consists of
 * zero or more children nodes and a return type. If a node has zero children
 * then it is a terminal. If a node has more than zero children then it is a
 * function.
 * <P>
 * Copyright (c) 2000 Robert Baruch. This code is released under
 * the <a href=http://www.gnu.org/copyleft/gpl.html>GNU General Public License</a> (GPL).<p>
 *
 * @author Robert Baruch (groovyjava@linuxstart.com)
 * @version 1.0
 */
public abstract class Node implements Cloneable {

    /**
   * The array of children of this node. If the array is null then there are
   * no children. Try not to create zero-length arrays.
   *
   * @since 1.0
   */
    protected Node[] children = null;

    /**
   * The return type of this node.
   *
   * @since 1.0
   */
    protected Class returnType;

    /**
   * The individual currently being evaluated. This is here for nodes (such as ADF)
   * whose execution depends on the individual. It is automatically set by the
   * {@link World World} when it evaluates an individual.
   *
   * @since 1.0
   */
    protected static Individual individual = null;

    /**
   * Create a node with the given arity (i.e. number of children) and the given type.
   *
   * @param arity the number of children
   * @param type the return type for this node
   *
   * @since 1.0
   */
    protected Node(int arity, Class type) {
        if (arity != 0) children = new Node[arity];
        returnType = type;
    }

    /**
   * Make a deep copy of this node. A deep copy of a terminal is this node itself --
   * the node is not cloned.
   *
   * @return the copy of the node
   *
   * @since 1.0
   */
    public Node deepCopy() {
        if (children == null) return this;
        Node n = (Node) clone();
        if (children != null) for (int i = 0; i < children.length; i++) n.children[i] = children[i].deepCopy();
        return n;
    }

    /**
   * Clone this node. This is used when we need to make a copy of a node and need
   * to retain the node's class. The resulting copy has the same arity and number of children,
   * but the children are blank (not set).
   * <p>
   * If you create a class extending from this class, you should be aware that the members
   * of that class will be shallow-copied on a cloning. Thus if there are members you do
   * not want shallow-copied (such as instance-specific arrays), you should override
   * the clone method to perform the cloning operation you want.
   *
   * @return the copy of the node
   *
   * @since 1.0
   */
    public Object clone() {
        Node o;
        try {
            o = (Node) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
        if (o.children != null) o.children = new Node[children.length];
        return o;
    }

    /**
   * Sets the individual currently being evaluated.
   *
   * @param individual the individual currently being evaluated
   *
   * @since 1.0
   */
    public static void setIndividual(Individual individual) {
        Node.individual = individual;
    }

    /**
   * Gets the given child node, between 0 and number of children-1
   *
   * @param i the child number
   *
   * @return the child node
   *
   * @since 1.0
   */
    public Node getChild(int i) {
        return children[i];
    }

    /**
   * Gets the arity (or number of children) of this node.
   *
   * @return the arity of this node
   *
   * @since 1.0
   */
    public int getArity() {
        return (children == null) ? 0 : children.length;
    }

    /**
   * Sets the given child to the given node.
   *
   * @param i the child number to set, between 0 and number of children-1
   * @param n the node to set the child to
   *
   * @since 1.0
   */
    public void setChild(int i, Node n) {
        children[i] = n;
    }

    /**
   * Gets the return type of this node
   *
   * @return the return type of this node
   *
   * @since 1.0
   */
    public Class getReturnType() {
        return returnType;
    }

    /**
   * Sets the return type of this node
   *
   * @param type the type to set the return type to
   *
   * @since 1.0
   */
    public void setReturnType(Class type) {
        returnType = type;
    }

    /**
   * Executes this node as a boolean.
   *
   * @return the boolean return value of this node
   * @throws UnsupportedOperationException if the type of this node is not boolean
   *
   * @since 1.0
   */
    public boolean execute_boolean() {
        throw new UnsupportedOperationException(getName() + " cannot return boolean");
    }

    /**
   * Executes this node, returning nothing.
   *
   * @throws UnsupportedOperationException if the type of this node is not void
   *
   * @since 1.0
   */
    public void execute_void() {
        throw new UnsupportedOperationException(getName() + " cannot return void");
    }

    /**
   * Executes this node as an integer.
   *
   * @return the integer return value of this node
   * @throws UnsupportedOperationException if the type of this node is not integer
   *
   * @since 1.0
   */
    public int execute_int() {
        throw new UnsupportedOperationException(getName() + " cannot return int");
    }

    /**
   * Executes this node as a long.
   *
   * @return the long return value of this node
   * @throws UnsupportedOperationException if the type of this node is not long
   *
   * @since 1.0
   */
    public long execute_long() {
        throw new UnsupportedOperationException(getName() + " cannot return long");
    }

    /**
   * Executes this node as a float.
   *
   * @return the float return value of this node
   * @throws UnsupportedOperationException if the type of this node is not float
   *
   * @since 1.0
   */
    public float execute_float() {
        throw new UnsupportedOperationException(getName() + " cannot return float");
    }

    /**
   * Executes this node as a double.
   *
   * @return the double return value of this node
   * @throws UnsupportedOperationException if the type of this node is not double
   *
   * @since 1.0
   */
    public double execute_double() {
        throw new UnsupportedOperationException(getName() + " cannot return double");
    }

    /**
   * Executes this node without knowing its return type.
   *
   * @return the Object which wraps the return value of this node, or null
   * if the return type is null or unknown.
   *
   * @since 1.0
   */
    public Object execute() {
        if (returnType == Boolean.class) return new Boolean(execute_boolean());
        if (returnType == Integer.class) return new Integer(execute_int());
        if (returnType == Long.class) return new Long(execute_long());
        if (returnType == Float.class) return new Float(execute_float());
        if (returnType == Double.class) return new Double(execute_double());
        if (returnType == Void.class) execute_void();
        return null;
    }

    /**
   * Gets the number of nodes in the tree rooted at this node.
   *
   * @return the number of nodes
   *
   * @since 1.0
   */
    public int getSize() {
        int size = 1;
        for (int i = 0; i < getArity(); i++) size += children[i].getSize();
        return size;
    }

    /**
   * Gets the number of nodes of the given type in the tree rooted at this node.
   *
   * @return the number of nodes
   *
   * @since 1.0
   */
    public int getSize(Class type) {
        int size = (type == returnType ? 1 : 0);
        for (int i = 0; i < getArity(); i++) size += children[i].getSize(type);
        return size;
    }

    /**
   * Gets the depth of the tree rooted at this node.
   *
   * @return the depth of the tree
   *
   * @since 1.0
   */
    public int getDepth() {
        int depth = 1;
        for (int i = 0; i < getArity(); i++) depth = Math.max(depth, 1 + children[i].getDepth());
        return depth;
    }

    /**
   * Returns the string representing the tree rooted at this node.
   *
   * @return the string representing the tree rooted at this node
   *
   * @since 1.0
   */
    public String toString() {
        if (getArity() == 0) return getName() + " ";
        String str = new String();
        str += getName() + " ( ";
        for (int i = 0; i < getArity(); i++) str += children[i];
        str += ") ";
        return str;
    }

    /**
   * Gets the name of this node. Must be overridden in subclasses.
   *
   * @return the name of this node.
   *
   * @since 1.0
   */
    public abstract String getName();

    /**
   * Gets the type of node allowed form the given child number. Must be overridden
   * in subclasses.
   *
   * @param i the child number
   * @return the type of node allowed for that child
   *
   * @since 1.0
   */
    public abstract Class getChildType(int i);
}
