package org.xteam.cs.grm.ast;

import org.xteam.cs.runtime.AstList;
import org.xteam.cs.runtime.Span;

public class LexerActionDeclarationAst extends DeclarationAst {

    protected AstList<IdentAst> actions;

    public LexerActionDeclarationAst(Span span, AstList<IdentAst> actions) {
        super(span);
        this.actions = actions;
    }

    public AstList<IdentAst> getActions() {
        return actions;
    }

    public void visit(IGrmVisitor visitor) {
        visitor.visitLexerActionDeclaration(this);
    }
}
