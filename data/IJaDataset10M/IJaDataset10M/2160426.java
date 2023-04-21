package com.google.code.java2objc.code;

import japa.parser.ast.body.ModifierSet;
import com.google.code.java2objc.code.ObjcStatementBlock.Builder;
import com.googlecode.java2objc.objc.CompilationContext;
import com.googlecode.java2objc.objc.ObjcField;
import com.googlecode.java2objc.objc.ObjcVariableDeclarator;

/**
 * Method for implementing dealloc for an object
 * 
 * @author Inderjeet Singh
 */
public final class ObjcMethodDealloc extends ObjcMethod {

    public ObjcMethodDealloc(CompilationContext context, ObjcType parent) {
        super(context, "dealloc", context.getTypeRepo().getVoid(), null, ModifierSet.PRIVATE, getDeallocBody(context, parent), null);
    }

    private static ObjcStatementBlock getDeallocBody(CompilationContext context, ObjcType parent) {
        Builder builder = new ObjcStatementBlock.Builder();
        for (ObjcField field : parent.fields) {
            if (!ModifierSet.isStatic(field.getModifiers()) && field.getType().isPointerType()) {
                for (ObjcVariableDeclarator decl : field.getVars()) {
                    ObjcExpressionMethodCall release = new ObjcExpressionMethodCall(context, new ObjcExpressionSimple(context, decl.getName()), "release", null);
                    builder.addStatement(new ObjcStatementExpression(release));
                }
            }
        }
        builder.addStatement(new ObjcStatementSimple("[super dealloc];"));
        return builder.build();
    }
}
