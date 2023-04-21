package xpath;

import java.util.ArrayList;

public abstract class LocationStep extends NodeSetExpression {

    public LocationStep() {
    }

    public Axis getAxis() {
        return null;
    }

    public void setAxis(Axis val) {
    }

    public NodeSetExpression getContext() {
        return null;
    }

    public void setContext(NodeSetExpression val) {
    }

    public NodeTest getNodeTest() {
        return null;
    }

    public ArrayList<Predicate> getPredicates() {
        return null;
    }

    public void setPredicates(ArrayList<Predicate> val) {
    }

    public void setNodeTest(NodeTest val) {
    }
}
