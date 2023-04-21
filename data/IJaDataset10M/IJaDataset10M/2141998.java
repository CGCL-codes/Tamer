package edu.uvm.cs.calendar.graph.filter;

import edu.uvm.cs.calendar.graph.Node;

/**
 * This abstract node filter can be used to combine the results of two
 * <code>NodeFilter</code>s. The combination is achieved by overriding the
 * <code>accept(boolean, boolean)</code> method to return true based on the
 * two boolean values passed in.
 * 
 * @author Jeremy Gustie
 * @version 1.0
 */
public abstract class LogicNodeFilter implements NodeFilter {

    /**
	 * Return a new anonymous node filter that requires both of the supplied
	 * filters to accept a node.
	 * 
	 * @param filterA The first filter to consider.
	 * @param filterB The second filter to consider.
	 * @return A <code>NodeFilter</code> that will accept any node accepted by
	 *         both <code>filterA</code> AND <code>filterB</code>.
	 */
    public static LogicNodeFilter andFilter(NodeFilter filterA, NodeFilter filterB) {
        return new LogicNodeFilter(filterA, filterB) {

            public boolean accept(boolean a, boolean b) {
                return a && b;
            }
        };
    }

    /**
	 * Return a new anonymous node filter that requires either of the supplied
	 * filters to accept a node.
	 * 
	 * @param filterA The first filter to consider.
	 * @param filterB The second filter to consider.
	 * @return A <code>NodeFilter</code> that will accept any node accepted by
	 *         both <code>filterA</code> OR <code>filterB</code>.
	 */
    public static LogicNodeFilter orFilter(NodeFilter filterA, NodeFilter filterB) {
        return new LogicNodeFilter(filterA, filterB) {

            public boolean accept(boolean a, boolean b) {
                return a || b;
            }
        };
    }

    /** The first filter */
    private NodeFilter filterA;

    /** The second filter */
    private NodeFilter filterB;

    /**
	 * Create a new logic node filter that will use the
	 * <code>accept(boolean, boolean)</code> method to combine the result of
	 * two <code>NodeFilter</code>s.
	 * 
	 * @param filterA The first filter to consider.
	 * @param filterB the second filter to consider.
	 */
    public LogicNodeFilter(NodeFilter filterA, NodeFilter filterB) {
        this.filterA = filterA;
        this.filterB = filterB;
    }

    /**
	 * Combine the results of the two <code>NodeFilter</code>s.
	 * 
	 * @param a True if the first filter accepted the node.
	 * @param b True if the second filter accepted the node.
	 * @return The combination of <code>a</code> and <code>b</code>.
	 */
    protected abstract boolean accept(boolean a, boolean b);

    /**
	 * @see edu.uvm.cs.calendar.graph.filter.NodeFilter#accept(edu.uvm.cs.calendar.graph.Node)
	 */
    public final boolean accept(Node node) {
        return accept(filterA.accept(node), filterB.accept(node));
    }
}
