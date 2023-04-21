package dryven.view.engine.expression.nodelisters;

import java.util.ArrayList;
import dryven.view.engine.expression.ExpressionNode;

public class PrimitiveNodeLister implements NodeLister {

    @Override
    public boolean allowsWildChar() {
        return false;
    }

    @Override
    public Iterable<ExpressionNode> getChildNodes() {
        return new ArrayList<ExpressionNode>(0);
    }

    @Override
    public ExpressionNode createWildCharNode(String name) {
        return null;
    }
}
