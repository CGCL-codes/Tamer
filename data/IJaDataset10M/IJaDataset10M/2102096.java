package jode.expr;

import jode.type.Type;
import jode.type.ArrayType;
import jode.decompiler.TabbedPrintWriter;

public class ArrayStoreOperator extends ArrayLoadOperator implements LValueExpression {

    public ArrayStoreOperator(Type type) {
        super(type);
    }

    public boolean matches(Operator loadop) {
        return loadop instanceof ArrayLoadOperator;
    }

    public void dumpExpression(TabbedPrintWriter writer) throws java.io.IOException {
        Type arrType = subExpressions[0].getType().getHint();
        if (arrType instanceof ArrayType) {
            Type elemType = ((ArrayType) arrType).getElementType();
            if (!elemType.isOfType(getType())) {
                writer.print("(");
                writer.startOp(writer.EXPL_PAREN, 1);
                writer.print("(");
                writer.printType(Type.tArray(getType().getHint()));
                writer.print(") ");
                writer.breakOp();
                subExpressions[0].dumpExpression(writer, 700);
                writer.print(")");
                writer.breakOp();
                writer.print("[");
                subExpressions[1].dumpExpression(writer, 0);
                writer.print("]");
                return;
            }
        }
        super.dumpExpression(writer);
    }
}
