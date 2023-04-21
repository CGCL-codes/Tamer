package progranet.omg.ocl.expression;

import progranet.omg.core.types.Type;
import progranet.omg.ocl.Model;

public class TypeCastExp extends CallExp {

    public TypeCastExp(Type type, OclExpression source) {
        super("OperationCallExp", type, source);
    }

    private static final long serialVersionUID = -6446581340405713343L;

    public static TypeCastExp compile(OclExpression contextExpression, OclExpression[] params) throws OclException {
        if (params.length != 1) throw new OclException("Invalid number of type cast parameters operation");
        if (params[0].getClassifier() != Model.TYPE_EXP) throw new OclException("Invalid parameter type of cast operation");
        logger.debug("context.type = " + contextExpression.getType().getName());
        Type type = (Type) params[0].execute(null);
        logger.debug("result.type = " + type.getName());
        return new TypeCastExp(type, contextExpression);
    }

    public Object execute(ExecutionContext executionContext) throws OclException {
        Object result = this.getSource().execute(executionContext);
        return result;
    }

    public Type getClassifier() {
        return Model.OPERATION_CALL_EXP;
    }
}
