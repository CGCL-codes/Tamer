package net.sf.jode.expr;

import net.sf.jode.type.Type;
import net.sf.jode.decompiler.TabbedPrintWriter;

/**
 * A PrePostFixOperator has one subexpression, namely the StoreInstruction.
 */
public class PrePostFixOperator extends Operator {

    boolean postfix;

    public PrePostFixOperator(Type type, int operatorIndex, LValueExpression lvalue, boolean postfix) {
        super(type);
        this.postfix = postfix;
        setOperatorIndex(operatorIndex);
        initOperands(1);
        setSubExpressions(0, (Operator) lvalue);
    }

    public int getPriority() {
        return postfix ? 800 : 700;
    }

    public void updateSubTypes() {
        if (!isVoid()) subExpressions[0].setType(type);
    }

    public void updateType() {
        if (!isVoid()) updateParentType(subExpressions[0].getType());
    }

    public void dumpExpression(TabbedPrintWriter writer) throws java.io.IOException {
        boolean needBrace = false;
        int priority = 700;
        if (!postfix) {
            writer.print(getOperatorString());
            priority = 800;
        }
        subExpressions[0].dumpExpression(writer, priority);
        if (postfix) writer.print(getOperatorString());
    }
}
