package org.deved.antlride.core.model.dltk.ast;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.statements.Statement;

public class DASTOption extends Statement {

    private String text;

    private int kind;

    public DASTOption(String text, int kind, int sourceStart, int sourceEnd) {
        super(sourceStart, sourceEnd - 1);
        this.text = text;
        this.kind = kind;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getKind() {
        return kind;
    }

    @Override
    public void traverse(ASTVisitor visitor) throws Exception {
        if (visitor.visit(this)) {
            visitor.endvisit(this);
        }
    }
}
