package edu.caece.langprocessor.syntax.tree.nodes;

import edu.caece.langprocessor.syntax.GrammarItem;
import edu.caece.langprocessor.syntax.tree.TreeNodeVisitor;

public class ExpressionTermTreeNode extends AbstractTreeNode {

    public ExpressionTermTreeNode(GrammarItem item) {
        super(item);
    }

    public void accept(TreeNodeVisitor visitor) {
        visitor.visitExpressionTermTreeNode(this);
    }
}
