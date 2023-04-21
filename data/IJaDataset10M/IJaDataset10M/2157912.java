package net.sourceforge.combean.graph.alg.partition;

import net.sourceforge.combean.graph.alg.traversal.IdleTraversalVisitor;
import net.sourceforge.combean.interfaces.graph.Graph;
import net.sourceforge.combean.interfaces.graph.alg.partition.NodePartitionVisitor;

/**
 * This class wraps a NodePartitionsVisitor inside a traversal visitor.
 * Every components that is encountered during the traversal is reported
 * from the traversal visitor to the node partition visitor.
 * 
 * @see PartitionsTraversalVisitor
 * @see net.sourceforge.combean.interfaces.graph.alg.traverse.TraversalVisitor
 * 
 * @author schickin
 *
 */
public class PartitionsTraversalVisitor<Node, Edge> extends IdleTraversalVisitor<Node, Edge> {

    private NodePartitionVisitor<Node> partitionVis = null;

    /**
     * 
     */
    public PartitionsTraversalVisitor(NodePartitionVisitor<Node> partitionVis) {
        super();
        this.partitionVis = partitionVis;
    }

    public void init(Graph<Node, Edge> g) {
        this.partitionVis.init(g);
    }

    public void leaveComponent(Node v) {
        this.partitionVis.endPartition();
    }

    public void visitComponent(Node v) {
        this.partitionVis.startPartition();
    }

    public void visitNode(Node v) {
        this.partitionVis.addNode(v);
    }

    public void finish() {
        this.partitionVis.finish();
    }
}
