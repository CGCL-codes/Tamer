package com.google.code.java2objc.code;

import com.googlecode.java2objc.objc.CompilationContext;
import japa.parser.ast.expr.Expression;

/**
 * A boolean literal in Objective C
 * 
 * @author David Gileadi
 */
public final class ObjcExpressionBooleanLiteral extends ObjcExpressionSimple {

    private ObjcExpressionBooleanLiteral(CompilationContext context, boolean value) {
        super(value ? "YES" : "NO", context.getTypeRepo().get("BOOL"));
    }

    /**
   * @param context TODO
   * @param expr the Java expression
   */
    public ObjcExpressionBooleanLiteral(CompilationContext context, Expression expr) {
        this(context, Boolean.parseBoolean(expr.toString()));
    }
}
