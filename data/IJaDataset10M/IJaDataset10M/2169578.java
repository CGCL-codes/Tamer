package org.translator.java.code;

import org.translator.java.code.util.CodeUtil;

/**
 *
 * @author Joshua
 */
public class IfSegment extends CodeSegment {

    private Value condition;

    private CodeSegment elseThen = null;

    public IfSegment(Value condition) {
        this.condition = condition;
    }

    public IfSegment(Value condition, CodeSegment elseThen) {
        this.condition = condition;
        this.elseThen = elseThen;
    }

    public void setElse(CodeSegment elseThen) {
        this.elseThen = elseThen;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("if( %s )\n{", condition.toString()));
        for (int i = 0; i < blocks.size(); i++) {
            builder.append("\n");
            if (blocks.get(i) instanceof FunctionSegment && i != 0) builder.append("\n");
            builder.append(CodeUtil.indent(blocks.get(i).toString()));
        }
        builder.append("\n}");
        if (elseThen != null) {
            builder.append(" else {\n");
            builder.append(CodeUtil.indent(elseThen.toString()));
            builder.append("\n}");
        }
        return builder.toString();
    }
}
