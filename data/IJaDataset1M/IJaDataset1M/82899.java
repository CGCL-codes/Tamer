package royere.cwi.framework;

public class GraphIteratorOp implements BooleanOperator {

    public boolean apply(boolean value1, boolean value2) {
        return value1 || value2;
    }
}
