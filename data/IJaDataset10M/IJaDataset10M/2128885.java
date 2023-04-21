package org.avis.subscription.ast.nodes;

import java.util.Collection;
import java.util.Map;
import org.avis.subscription.ast.BoolParentNode;
import org.avis.subscription.ast.Node;

public class Xor extends BoolParentNode {

    public Xor(Node node1) {
        super(node1);
    }

    public Xor(Node node1, Node node2) {
        super(node1, node2);
    }

    public Xor(Collection<? extends Node> children) {
        super(children);
    }

    public Xor(Node... children) {
        super(children);
    }

    @Override
    public String expr() {
        return "^^";
    }

    @Override
    public Node inlineConstants() {
        for (int i = children.size() - 1; i >= 0; i--) {
            Node child = children.get(i);
            Node newChild = child.inlineConstants();
            if (child != newChild) children.set(i, newChild);
        }
        Boolean result = (Boolean) evaluate(EMPTY_NOTIFICATION);
        if (result != BOTTOM) return Const.bool(result); else return this;
    }

    @Override
    public Object evaluate(Map<String, Object> attrs) {
        Boolean value = FALSE;
        for (int i = 0; i < children.size(); i++) {
            Object result = children.get(i).evaluate(attrs);
            if (result == BOTTOM) return BOTTOM; else if (result == TRUE) value = value == TRUE ? FALSE : TRUE;
        }
        return value;
    }
}
