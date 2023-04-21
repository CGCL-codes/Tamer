package edu.caece.langprocessor.syntax.tree.nodes;

import edu.caece.langprocessor.syntax.GrammarItem;
import edu.caece.langprocessor.syntax.tree.TreeNodeVisitor;

public class LeftParenthesisTreeNode extends AbstractTreeNode {

    public LeftParenthesisTreeNode(GrammarItem item) {
        super(item);
    }

    public void accept(TreeNodeVisitor visitor) {
        visitor.visitLeftParenthesisTreeNode(this);
    }
}
