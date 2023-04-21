package org.swfparser.operation;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.swfparser.CodeUtil;
import org.swfparser.Operation;

public class CloneSpriteOperation extends AbstractOperation {

    private Operation source;

    private Operation target;

    private Operation depth;

    public CloneSpriteOperation(Stack<Operation> stack) {
        super(stack);
        depth = stack.pop();
        target = stack.pop();
        source = stack.pop();
    }

    public int getArgsNumber() {
        return 3;
    }

    public String getStringValue(int level) {
        return new StringBuffer().append(CodeUtil.getIndent(level)).append("duplicateMovieClip(").append(source.getStringValue(level)).append(",").append(target.getStringValue(level)).append(",").append(depth.getStringValue(level)).append(")").toString();
    }

    public List<Operation> getOperations() {
        return Arrays.asList(depth, target, source);
    }
}
