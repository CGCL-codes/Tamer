package jolie.runtime;

import jolie.process.TransformationReason;

public class IsRealExpression implements Expression {

    private final VariablePath path;

    public IsRealExpression(VariablePath path) {
        this.path = path;
    }

    public Expression cloneExpression(TransformationReason reason) {
        return new IsRealExpression(path);
    }

    public Value evaluate() {
        Value value = path.getValueOrNull();
        return Value.create((value != null && value.isDouble()) ? 1 : 0);
    }
}
