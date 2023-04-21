package progranet.omg.ocl.expression;

import progranet.omg.core.types.Type;
import progranet.omg.ocl.Model;

public final class IntegerLiteralExp extends NumericLiteralExp {

    private static final long serialVersionUID = 1647211120512982643L;

    private Integer value;

    public IntegerLiteralExp(Integer value) {
        super("IntegerLiteralExp", Model.INTEGER);
        this.value = value;
    }

    public static IntegerLiteralExp compile(Integer value) {
        return new IntegerLiteralExp(value);
    }

    public Object execute(ExecutionContext executionContext) throws OclException {
        return this.value;
    }

    public Type getClassifier() {
        return Model.INTEGER_LITERAL_EXP;
    }
}
