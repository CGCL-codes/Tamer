package org.jmlspecs.jml6.core.ast;

import org.eclipse.jdt.core.dom.Expression;
import org.jmlspecs.annotation.Invariant;

public class JmlEnsuresClause extends JmlClause {

    @Invariant(header = "public", value = "this.expr != null")
    public JmlEnsuresClause(boolean isRedundant, Expression predOrKeyword) {
        super(JmlClauseKind.ENSURES, isRedundant, predOrKeyword);
    }

    public boolean subtreeMatch(JmlAstMatcher matcher, Object other) {
        return matcher.match(this, other);
    }

    public void accept(JmlAstVisitor visitor) {
        if (visitor.visit(this)) {
            visitExpressions(visitor);
        }
    }
}
