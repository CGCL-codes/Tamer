package org.jacex.compiler.types;

import java.util.Iterator;
import org.jacex.compiler.conversions.ConversionBuilder;
import org.jacex.compiler.operations.Operations;
import org.jacex.compiler.operations.OperationAppender;
import org.jacex.expressions.AndExpressionAppender;
import org.jacex.expressions.BinaryOperationExpressionAppender;
import org.jacex.expressions.ExpressionAppender;
import org.jacex.expressions.OrExpressionAppender;
import org.jacex.operations.Operation;
import org.jacex.operations.OperationGroup;

public class BooleanTypeInfo extends AbstractTypeInfo {

    public static OperationAppender selectBooleanOp(Operation op) {
        switch(op) {
            case MORE:
                return Operations.IGT;
            case NOTMORE:
                return Operations.ILE;
            case LESS:
                return Operations.ILT;
            case NOTLESS:
                return Operations.IGE;
            case EQUALS:
                return Operations.IEQ;
            case NOTEQUALS:
                return Operations.INE;
            default:
                throw new RuntimeException("Unsupported double operation: " + op);
        }
    }

    @Override
    public ExpressionAppender appendOperation(ExpressionAppender base, Operation op, ExpressionAppender arg) {
        if (!arg.type.equals(Boolean.TYPE)) throw new RuntimeException("Unknown argument type: " + arg.type.getName());
        return new BinaryOperationExpressionAppender(BooleanTypeInfo.selectBooleanOp(op), base, ConversionBuilder.EMPTY, arg, ConversionBuilder.EMPTY);
    }

    @Override
    public ExpressionAppender appendOperationList(ExpressionAppender base, OperationGroup group, TypeInfoProvider provider, Iterator<Operation> operationsIterator, Iterator<ExpressionAppender> argsIterator, int count) {
        ExpressionAppender[] operands = createOperands(base, operationsIterator, argsIterator, count);
        switch(group) {
            case LOGICAND:
                return new AndExpressionAppender(operands);
            case LOGICOR:
                return new OrExpressionAppender(operands);
            default:
                throw new RuntimeException("Upsupported bool op");
        }
    }

    @Override
    public boolean isLdc() {
        return true;
    }

    @Override
    public Class<?> getType() {
        return Double.TYPE;
    }

    private ExpressionAppender[] createOperands(ExpressionAppender base, Iterator<Operation> operationsIterator, Iterator<ExpressionAppender> argsIterator, int count) {
        ExpressionAppender[] operands = new ExpressionAppender[count + 1];
        operands[0] = base;
        for (int i = 0; i < count; ++i) {
            operands[i + 1] = argsIterator.next();
            operationsIterator.next();
        }
        return operands;
    }
}
