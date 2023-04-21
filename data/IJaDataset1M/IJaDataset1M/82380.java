package net.sf.orcc.ir.nodes;

import java.util.List;
import java.util.ListIterator;
import net.sf.orcc.ir.CFGNode;

/**
 * This abstract class defines a no-op node visitor.
 * 
 * @author Matthieu Wipliez
 * 
 */
public abstract class AbstractNodeVisitor implements NodeVisitor {

    @Override
    public void visit(BlockNode node, Object... args) {
    }

    @Override
    public void visit(IfNode node, Object... args) {
        visit(node.getThenNodes());
        visit(node.getElseNodes());
        visit(node.getJoinNode(), args);
    }

    /**
	 * Visits the nodes of the given node list.
	 * 
	 * @param nodes
	 *            a list of nodes that belong to a procedure
	 * @param args
	 *            arguments
	 */
    protected void visit(List<CFGNode> nodes, Object... args) {
        ListIterator<CFGNode> it = nodes.listIterator();
        while (it.hasNext()) {
            CFGNode node = it.next();
            node.accept(this, it);
        }
    }

    @Override
    public void visit(WhileNode node, Object... args) {
        visit(node.getNodes());
        visit(node.getJoinNode(), args);
    }
}
