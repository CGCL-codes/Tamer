package com.stateofflow.eclipse.tane.hidedelegate.model.chain;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;

class ASTTruncater extends ASTMatcher {

    private final Expression origin;

    public ASTTruncater(final Expression origin) {
        this.origin = origin;
    }

    @Override
    public boolean match(final FieldAccess original, final Object duplicate) {
        if (original == origin) {
            deleteNonNull(((FieldAccess) duplicate).getExpression());
            return false;
        }
        return super.match(original, duplicate);
    }

    @Override
    public boolean match(final MethodInvocation original, final Object duplicate) {
        if (original == origin) {
            deleteNonNull(((MethodInvocation) duplicate).getExpression());
            return false;
        }
        return super.match(original, duplicate);
    }

    private void deleteNonNull(final Expression expression) {
        if (expression != null) {
            expression.delete();
        }
    }
}
