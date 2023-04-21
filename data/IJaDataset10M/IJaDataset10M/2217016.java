package net.sourceforge.combean.graph.alg.partition;

import net.sourceforge.combean.graph.alg.AbstractGraphAlg;
import net.sourceforge.combean.graph.containers.NodeNumberingAdvisor;
import net.sourceforge.combean.interfaces.graph.alg.partition.NodePartitionVisitor;
import net.sourceforge.combean.interfaces.graph.alg.partition.StronglyConnectedComponentsAlg;
import net.sourceforge.combean.interfaces.graph.containers.NodeNumbering;

/**
 * A convenience class for implementing SCC algorithms.
 * 
 * @author schickin
 *
 */
public abstract class AbstractSCCImpl<Node, Edge> extends AbstractGraphAlg<Node, Edge> implements StronglyConnectedComponentsAlg<Node, Edge> {

    private NodePartitionVisitor<Node> partitionVisitor = null;

    /**
     * constructor
     */
    public AbstractSCCImpl() {
        super();
    }

    public void setVisitor(NodePartitionVisitor<Node> partitionVisitor) {
        this.partitionVisitor = partitionVisitor;
    }

    public NodePartitionVisitor<Node> getVisitor() {
        return this.partitionVisitor;
    }

    /**
     * Set a NodeNumbering as Visitor for the strongly connected components.
     * The NodeNumbering will be filled with the number of the SCCs,
     * starting with 0.
     * 
     * @return the NodeNumbering to be used as Visitor
     */
    public final NodeNumbering<Node> setNodeNumberingAsVisitor() {
        NodeNumbering<Node> numbering = NodeNumberingAdvisor.getFastestNodeNumbering(getGraph());
        setVisitor(new NodeNumberingAsPartitionVisitor<Node>(numbering));
        return numbering;
    }
}
