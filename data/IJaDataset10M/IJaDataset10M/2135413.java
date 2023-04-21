package edu.caece.langprocessor.syntax.tree.nodes;

import edu.caece.langprocessor.syntax.GrammarItem;
import edu.caece.langprocessor.syntax.tree.TreeNodeVisitor;

public class IdListTreeNode extends AbstractTreeNode {

    public IdListTreeNode(GrammarItem item) {
        super(item);
    }

    public void accept(TreeNodeVisitor visitor) {
        visitor.visitIdListTreeNode(this);
    }
}
