package kpython.backend.ir.tree;

import java.util.ArrayList;
import java.util.List;

public class SimpleBlock extends Block {

    private List<IRNode> content = new ArrayList<IRNode>();

    public void addContent(IRNode node) {
        content.add(node);
    }

    public List<IRNode> getContent() {
        return content;
    }

    public Object accept(IRTreeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public void printTree(String tab) {
        System.out.println(tab + this.getClass().getSimpleName());
        for (IRNode node : content) node.printTree(tab + "\t");
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
