package edu.uvm.cs.calendar.graph;

import java.lang.reflect.Constructor;

/**
 * @author Jeremy Gustie
 * @version 1.0
 */
public abstract class Edge implements Comparable {

    public int compareTo(Object o) {
        Edge other = (Edge) o;
        return head.compareTo(other.head);
    }

    /** The head node, or node that is being pointed to */
    private Node head = null;

    /** Parameters for this edge */
    protected long[] parameters;

    /** True if this edge is a shadow of another edge (the ith operand) */
    private boolean shadow = false;

    /** The tail node, or node that is doing the pointing */
    private Node tail = null;

    /**
     * Zero parameter constructor for Edge.
     */
    public Edge() {
        this.parameters = new long[0];
    }

    /**
     * One parameter (m) constructor for Edge.
     */
    public Edge(long m) {
        this.parameters = new long[] { m };
    }

    /**
     * Two parameter (l,k) constructor for Edge.
     */
    public Edge(long l, long k) {
        this.parameters = new long[] { l, k };
    }

    /**
     * Three parameter (m,l,k) constructor for Edge.
     */
    public Edge(long m, long l, long k) {
        this.parameters = new long[] { m, l, k };
    }

    /**
     * Create a shadow edge for this edge. A shadow edge has the same parameters
     * and type as it's original, but does not have any of the connections (tail
     * or head). Also, the shadow edge's isShadow method returns true. Generally
     * shadow edges should be ignored when generating output as the original
     * would have been outputted by the head nodes getPrev node.
     * 
     * @return The shadow edge.
     */
    public final Edge createShadow() {
        Edge e = null;
        try {
            Object[] args = new Object[parameters.length];
            Class[] argsClass = new Class[parameters.length];
            for (int i = 0; i < parameters.length; ++i) {
                args[i] = new Long(parameters[i]);
                argsClass[i] = Long.TYPE;
            }
            Constructor c = this.getClass().getConstructor(argsClass);
            e = (Edge) c.newInstance(args);
            e.setShadow(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

    public final Node getHead() {
        return head;
    }

    public abstract int getLayer();

    /**
     * Get the number of parameters for this edge.
     * 
     * @return the number of parameters
     */
    public final int getParamLength() {
        return parameters.length;
    }

    public final Node getTail() {
        return tail;
    }

    /**
     * Generate a hash code for this Edge. The hash code is computed by the
     * equation: <code>param[0] * 31 ^ (n - 1) + param[1] * 31 ^ (n - 2) + ... 
     * + param[n - 1] + HC(className)</code>
     * Where param[i] is the ith parameter, n is the number of parameters, ^
     * denotes exponentiation, className is the fully qualified name of the
     * class whose hash code is being computed and HC is the hash code method of
     * java.lang.String. This method is very similar to how hash codes are
     * computed for Strings.
     * 
     * @see java.lang.Object#hashCode()
     * @return a hash code value for this edge
     */
    public final int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < parameters.length; ++i) {
            hashCode += Math.pow(parameters[i] * 31, (parameters.length - (i + 1)));
        }
        hashCode += this.getClass().getName().hashCode();
        hashCode = 31 * hashCode + tail.hashCode();
        hashCode = 31 * hashCode + head.hashCode();
        return hashCode;
    }

    public final boolean isShadow() {
        return shadow;
    }

    /**
     * Sets the head node of this edge. This method can only be called with one
     * node object, attempts to call this method with multiple node objects
     * results in an EdgeReuseException.
     * 
     * @param head
     * @throws EdgeReuseException
     */
    public final void setHead(Node head) throws EdgeReuseException {
        if ((this.head != null) && (!this.head.equals(head))) {
            throw new EdgeReuseException();
        }
        this.head = head;
    }

    private final void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    /**
     * Sets the tail node of this edge. This method can only be called with one
     * node object, attempts to call this method with multiple node objects
     * results in an EdgeReuseException.
     * 
     * @param tail
     * @throws EdgeReuseException
     */
    public final void setTail(Node tail) throws EdgeReuseException {
        if ((this.tail != null) && (!this.tail.equals(tail))) {
            throw new EdgeReuseException();
        }
        this.tail = tail;
    }

    /**
     * Generates a displayable string representation of this edge. The string is
     * "->" for a no-parameter edge, "-{m}->" for a one parameter edge,
     * "-{l,k}->" for a two parameter edge and "-{m,l,k}->" for a three
     * parameter edge. Edges that implement this method should over-ride
     * <code>toString</code> to give a hint as to what type of edge is being
     * displayed.
     * 
     * @see java.lang.Object#toString()
     * @return a string representation of this edge
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (parameters.length > 0) {
            sb.append("-{");
            for (int i = 0; i < parameters.length; ++i) {
                sb.append(parameters[i]);
                if (i < parameters.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("}");
        }
        sb.append("->");
        return sb.toString();
    }

    public void connect(Node tail, Node head) {
        setTail(tail);
        setHead(head);
        head.setInboundEdge(this);
        tail.addOutboundEdge(this);
    }
}
