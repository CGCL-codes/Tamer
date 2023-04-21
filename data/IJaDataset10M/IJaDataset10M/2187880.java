package org.avis.pubsub.ast.nodes;

import java.util.Map;
import org.avis.pubsub.ast.BoolParentNode;
import org.avis.pubsub.ast.Node;
import static org.avis.pubsub.ast.nodes.Const.CONST_FALSE;
import static org.avis.pubsub.ast.nodes.Const.CONST_TRUE;

public class And extends BoolParentNode {

    public And(Node<Boolean> node1) {
        super(node1);
    }

    public And(Node<Boolean> node1, Node<Boolean> node2) {
        super(node1, node2);
    }

    public And(Node<Boolean>... children) {
        super(children);
    }

    @Override
    public String expr() {
        return "&&";
    }

    @Override
    public Node<Boolean> inlineConstants() {
        for (int i = children.size() - 1; i >= 0; i--) {
            Node<Boolean> child = children.get(i);
            Node<Boolean> newChild = child.inlineConstants();
            Boolean result = newChild.evaluate(EMPTY_NOTIFICATION);
            if (result == FALSE) return CONST_FALSE; else if (result == TRUE) children.remove(i); else if (child != newChild) children.set(i, newChild);
        }
        if (children.isEmpty()) return CONST_TRUE; else if (children.size() == 1) return children.get(0); else return this;
    }

    @Override
    public Boolean evaluate(Map<String, Object> attrs) {
        Boolean value = TRUE;
        for (int i = 0; i < children.size(); i++) {
            Boolean result = children.get(i).evaluate(attrs);
            if (result == FALSE) return result; else if (result == BOTTOM) value = BOTTOM;
        }
        return value;
    }
}
