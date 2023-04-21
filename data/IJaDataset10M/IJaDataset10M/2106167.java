package org.xteam.cs.lex.ast;

import org.xteam.cs.runtime.Span;

public class UpToExprAst extends UnaryExprAst {

    public UpToExprAst(Span span, ExprAst expr) {
        super(span, expr);
        this.expr = expr;
    }

    @Override
    public void visit(ILexerAstVisitor visitor) {
        visitor.visitUpToExpr(this);
    }
}
