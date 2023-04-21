package net.sf.jode.expr;

import net.sf.jode.type.Type;
import net.sf.jode.decompiler.TabbedPrintWriter;

public class CompareBinaryOperator extends Operator {

    boolean allowsNaN = false;

    Type compareType;

    public CompareBinaryOperator(Type type, int op) {
        super(Type.tBoolean, op);
        compareType = type;
        initOperands(2);
    }

    public CompareBinaryOperator(Type type, int op, boolean allowsNaN) {
        super(Type.tBoolean, op);
        compareType = type;
        this.allowsNaN = allowsNaN;
        initOperands(2);
    }

    public int getPriority() {
        switch(getOperatorIndex()) {
            case 26:
            case 27:
                return 500;
            case 28:
            case 29:
            case 30:
            case 31:
                return 550;
        }
        throw new RuntimeException("Illegal operator");
    }

    public Type getCompareType() {
        return compareType;
    }

    public void updateSubTypes() {
        subExpressions[0].setType(Type.tSubType(compareType));
        subExpressions[1].setType(Type.tSubType(compareType));
    }

    public void updateType() {
        Type leftType = Type.tSuperType(subExpressions[0].getType());
        Type rightType = Type.tSuperType(subExpressions[1].getType());
        compareType = compareType.intersection(leftType).intersection(rightType);
        subExpressions[0].setType(Type.tSubType(rightType));
        subExpressions[1].setType(Type.tSubType(leftType));
    }

    public Expression negate() {
        if (!allowsNaN || getOperatorIndex() <= NOTEQUALS_OP) {
            setOperatorIndex(getOperatorIndex() ^ 1);
            return this;
        }
        return super.negate();
    }

    public boolean opEquals(Operator o) {
        return (o instanceof CompareBinaryOperator) && o.operatorIndex == operatorIndex;
    }

    public void dumpExpression(TabbedPrintWriter writer) throws java.io.IOException {
        subExpressions[0].dumpExpression(writer, getPriority() + 1);
        writer.breakOp();
        writer.print(getOperatorString());
        subExpressions[1].dumpExpression(writer, getPriority() + 1);
    }
}
