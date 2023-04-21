package net.sf.jode.expr;

import net.sf.jode.type.Type;
import net.sf.jode.type.ArrayType;
import net.sf.jode.decompiler.TabbedPrintWriter;

public class ConstantArrayOperator extends Operator {

    boolean isInitializer;

    ConstOperator empty;

    Type argType;

    public ConstantArrayOperator(Type type, int size) {
        super(type);
        argType = (type instanceof ArrayType) ? Type.tSubType(((ArrayType) type).getElementType()) : Type.tError;
        Object emptyVal;
        if (argType == type.tError || argType.isOfType(Type.tUObject)) emptyVal = null; else if (argType.isOfType(Type.tBoolUInt)) emptyVal = new Integer(0); else if (argType.isOfType(Type.tLong)) emptyVal = new Long(0); else if (argType.isOfType(Type.tFloat)) emptyVal = new Float(0); else if (argType.isOfType(Type.tDouble)) emptyVal = new Double(0); else throw new IllegalArgumentException("Illegal Type: " + argType);
        empty = new ConstOperator(emptyVal);
        empty.setType(argType);
        empty.makeInitializer(argType);
        initOperands(size);
        for (int i = 0; i < subExpressions.length; i++) setSubExpressions(i, empty);
    }

    public void updateSubTypes() {
        argType = (type instanceof ArrayType) ? Type.tSubType(((ArrayType) type).getElementType()) : Type.tError;
        for (int i = 0; i < subExpressions.length; i++) if (subExpressions[i] != null) subExpressions[i].setType(argType);
    }

    public void updateType() {
    }

    public boolean setValue(int index, Expression value) {
        if (index < 0 || index > subExpressions.length || subExpressions[index] != empty) return false;
        value.setType(argType);
        setType(Type.tSuperType(Type.tArray(value.getType())));
        subExpressions[index] = value;
        value.parent = this;
        value.makeInitializer(argType);
        return true;
    }

    public int getPriority() {
        return 200;
    }

    public void makeInitializer(Type type) {
        if (type.getHint().isOfType(getType())) isInitializer = true;
    }

    public Expression simplify() {
        for (int i = 0; i < subExpressions.length; i++) {
            if (subExpressions[i] != null) subExpressions[i] = subExpressions[i].simplify();
        }
        return this;
    }

    public void dumpExpression(TabbedPrintWriter writer) throws java.io.IOException {
        if (!isInitializer) {
            writer.print("new ");
            writer.printType(type.getHint());
            writer.breakOp();
            writer.print(" ");
        }
        writer.print("{ ");
        writer.startOp(writer.EXPL_PAREN, 0);
        for (int i = 0; i < subExpressions.length; i++) {
            if (i > 0) {
                writer.print(", ");
                writer.breakOp();
            }
            if (subExpressions[i] != null) subExpressions[i].dumpExpression(writer, 0); else empty.dumpExpression(writer, 0);
        }
        writer.endOp();
        writer.print(" }");
    }
}
