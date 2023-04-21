package com.google.code.java2objc.code;

import com.googlecode.java2objc.objc.CompilationContext;
import com.googlecode.java2objc.objc.SourceCodeWriter;

/**
 * Base class for all Objective C expressions
 * 
 * @author Inderjeet Singh
 */
public class ObjcExpressionSimple extends ObjcExpression {

    private final String expression;

    public ObjcExpressionSimple(CompilationContext context, String expression) {
        super(context.typeOf(expression));
        this.expression = expression;
    }

    public ObjcExpressionSimple(String expression, ObjcType type) {
        super(type);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public void append(SourceCodeWriter writer) {
        if (expression != null) {
            writer.append(expression);
        }
    }
}
