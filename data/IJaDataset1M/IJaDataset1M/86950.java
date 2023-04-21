package org.jmlspecs.jml4.ast;

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.impl.BooleanConstant;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.jmlspecs.jml4.esc.util.Utils;

public class JmlSubtypeExpression extends Expression {

    /** A constant for the "isAssignableFrom" method selector. */
    private static final char[] IS_ASSIGNABLE_FROM = "isAssignableFrom".toCharArray();

    /** The left expression. */
    public final Expression left;

    /** The right expression. */
    public final Expression right;

    /**
     * Constructs a JmlSubtypeExpression with the specified parameters.
     * 
     * @param left The left expression.
     * @param right The right expression
     */
    public JmlSubtypeExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.sourceStart = left.sourceStart();
        this.sourceEnd = right.sourceEnd();
        Utils.assertTrue(this.sourceStart <= this.sourceEnd, "sourceStart > sourceEnd (" + this.sourceStart + " > " + this.sourceEnd + ")");
        constant = Constant.NotAConstant;
        resolvedType = TypeBinding.BOOLEAN;
    }

    /** {@inheritDoc} */
    public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
        flowInfo = left.analyseCode(currentScope, flowContext, flowInfo);
        flowInfo = right.analyseCode(currentScope, flowContext, flowInfo);
        return flowInfo;
    }

    /** {@inheritDoc} */
    public StringBuffer printExpression(int indent, StringBuffer output) {
        left.printExpression(0, output);
        output.append(" <: ");
        right.printExpression(0, output);
        return output;
    }

    /** {@inheritDoc} */
    public TypeBinding resolveType(BlockScope upperScope) {
        TypeBinding javaLangClass = upperScope.getJavaLangClass();
        TypeBinding leftType = left.resolveType(upperScope);
        if (leftType == null || leftType == TypeBinding.NULL) {
            upperScope.problemReporter().typeMismatchError(TypeBinding.NULL, javaLangClass, left, null);
        } else if (!leftType.erasure().isCompatibleWith(javaLangClass)) {
            upperScope.problemReporter().typeMismatchError(leftType, javaLangClass, left, null);
        }
        TypeBinding rightType = right.resolveType(upperScope);
        if (rightType == null || rightType == TypeBinding.NULL) {
            upperScope.problemReporter().typeMismatchError(TypeBinding.NULL, javaLangClass, right, null);
        } else if (!rightType.erasure().isCompatibleWith(javaLangClass)) {
            upperScope.problemReporter().typeMismatchError(rightType, javaLangClass, right, null);
        }
        return resolvedType;
    }

    /** {@inheritDoc} */
    public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
        if (left.resolvedType instanceof ReferenceBinding) {
            ReferenceBinding rb = (ReferenceBinding) left.resolvedType;
            MethodBinding[] methods = rb.getMethods(IS_ASSIGNABLE_FROM);
            MethodBinding iafMethod = null;
            if (methods.length == 1) {
                iafMethod = methods[0];
            } else if (valueRequired) {
                codeStream.generateConstant(BooleanConstant.fromValue(false), 0);
                return;
            }
            right.generateCode(currentScope, codeStream, valueRequired);
            left.generateCode(currentScope, codeStream, valueRequired);
            if (valueRequired) {
                codeStream.invokevirtual(iafMethod);
            }
        } else if (valueRequired) {
            codeStream.generateConstant(BooleanConstant.fromValue(false), 0);
        }
    }
}
