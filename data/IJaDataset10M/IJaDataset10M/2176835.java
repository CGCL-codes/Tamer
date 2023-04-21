package org.swfparser.operation;

import java.util.Stack;
import org.swfparser.CodeUtil;
import org.swfparser.Operation;

public class TraceOperation extends UnaryOperation {

    public TraceOperation(Stack<Operation> stack) {
        super(stack);
    }

    @Override
    public String getStringValue(int level) {
        return CodeUtil.getIndent(level) + "trace(" + op.getStringValue(level) + ")";
    }
}
