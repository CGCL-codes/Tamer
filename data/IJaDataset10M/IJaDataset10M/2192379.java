package org.jmlspecs.jml4.ast;

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.jmlspecs.jml4.nonnull.Nullity;

public class JmlCastExpressionWithoutType extends JmlCastExpression {

    public JmlCastExpressionWithoutType(Expression expression, boolean isExplicitNonNull) {
        super(expression, getType(expression), isExplicitNonNull);
    }

    private static Expression getType(Expression e) {
        final String type = "Object";
        return new JmlSingleTypeReference(type.toCharArray(), type.length(), Nullity.nullable, 0);
    }

    public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
        if (valueRequired) {
            expression.generateCode(currentScope, codeStream, valueRequired);
        }
        generateNullityTest(codeStream, currentScope);
    }

    public TypeBinding resolveType(BlockScope scope) {
        super.resolveType(scope);
        return this.expression.resolvedType;
    }

    public int nullStatus(FlowInfo flowInfo) {
        return (this.isExplicitNonNull) ? FlowInfo.NON_NULL : FlowInfo.UNKNOWN;
    }

    public boolean isDeclaredNonNull() {
        return this.isExplicitNonNull;
    }

    /**
	 * @see org.eclipse.jdt.internal.compiler.ast.Expression#tagAsUnnecessaryCast(Scope, TypeBinding)
	 */
    public void tagAsUnnecessaryCast(Scope scope, TypeBinding castType) {
        if (this.isExplicitNonNull && this.expression.isDeclaredNonNull()) {
            super.tagAsUnnecessaryCast(scope, castType);
        } else {
            this.tagAsNeedCheckCast();
        }
    }
}
