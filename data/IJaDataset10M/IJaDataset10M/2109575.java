package org.swfparser.operation;

import java.util.List;
import java.util.Stack;
import org.swfparser.CodeUtil;
import org.swfparser.ExecutionContext;
import org.swfparser.Operation;
import org.swfparser.SkipOperation;
import org.swfparser.annotations.NewAnalyzer;
import org.swfparser.exception.StatementBlockException;
import com.jswiff.swfrecords.actions.With;

@NewAnalyzer
public class WithOperation extends AbstractCompoundOperation implements SkipOperation {

    private Operation object;

    private List<Operation> operations;

    private boolean skipEmptyWithBlock = false;

    public WithOperation(Stack<Operation> stack, ExecutionContext context, With with) throws StatementBlockException {
        super(context);
        object = stack.pop();
        this.operations = executeWithSameStack(context, with.getWithBlock().getActions(), this);
        if (this.operations.isEmpty()) {
            skipEmptyWithBlock = true;
        }
    }

    public String getStringValue(int level) {
        StringBuffer buf = new StringBuffer().append(CodeUtil.getIndent(level)).append("with (").append(object.getStringValue(level)).append(") {\n");
        for (Operation operation : operations) {
            buf.append(operation.getStringValue(level + 1)).append(";\n");
        }
        buf.append(CodeUtil.getIndent(level)).append("}");
        return buf.toString();
    }

    public boolean skip() {
        return skipEmptyWithBlock;
    }
}
