package org.swfparser.operation;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.swfparser.CodeUtil;
import org.swfparser.Operation;
import com.jswiff.swfrecords.actions.StackValue;

public class SetPropertyOperation extends AbstractOperation {

    protected Operation target;

    protected Operation index;

    protected Operation value;

    public SetPropertyOperation(Stack<Operation> stack) {
        super(stack);
        value = stack.pop();
        index = stack.pop();
        target = stack.pop();
    }

    public int getArgsNumber() {
        return 3;
    }

    public String getStringValue(int level) {
        String indexValue = index.getStringValue(level);
        if (index instanceof StackValue) {
            indexValue = GetPropertyOperation.properties[((StackValue) index).getIntValue()];
        }
        return new StringBuffer().append(CodeUtil.getIndent(level)).append("setProperty(").append(target.getStringValue(level)).append(",").append(indexValue).append(",").append(value.getStringValue(level)).append(")").toString();
    }

    public List<Operation> getOperations() {
        return Arrays.asList(value, index, target);
    }
}
