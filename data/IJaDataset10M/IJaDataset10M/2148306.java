package org.swfparser.operation;

import java.util.Stack;
import org.swfparser.Operation;

public class OrdOperation extends ConvertOperation {

    public OrdOperation(Stack<Operation> stack) {
        super(stack);
    }

    @Override
    protected String getFunctionName() {
        return "ord";
    }
}
