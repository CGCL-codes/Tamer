package net.sf.jode.expr;

import net.sf.jode.type.Type;
import net.sf.jode.decompiler.TabbedPrintWriter;

public class CheckCastOperator extends Operator {

    Type castType;

    public CheckCastOperator(Type type) {
        super(type, 0);
        castType = type;
        initOperands(1);
    }

    public int getPriority() {
        return 700;
    }

    public void updateSubTypes() {
        subExpressions[0].setType(Type.tUObject);
    }

    public void updateType() {
    }

    public Expression simplify() {
        if (subExpressions[0].getType().getCanonic().isOfType(Type.tSubType(castType))) return subExpressions[0].simplify();
        return super.simplify();
    }

    public void dumpExpression(TabbedPrintWriter writer) throws java.io.IOException {
        writer.print("(");
        writer.printType(castType);
        writer.print(") ");
        writer.breakOp();
        Type superType = castType.getCastHelper(subExpressions[0].getType());
        if (superType != null) {
            writer.print("(");
            writer.printType(superType);
            writer.print(") ");
            writer.breakOp();
        }
        subExpressions[0].dumpExpression(writer, 700);
    }
}
