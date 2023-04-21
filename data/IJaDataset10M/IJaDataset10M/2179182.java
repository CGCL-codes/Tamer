package org.xteam.cs.ast.ast;

import org.xteam.cs.runtime.Span;

public class RepeatableTypeAst extends TypeAst {

    private TypeAst base;

    public RepeatableTypeAst(Span span, TypeAst base) {
        super(span);
        this.base = base;
    }

    public TypeAst getBase() {
        return base;
    }

    @Override
    public void visit(IAstVisitor visitor) {
        visitor.visitRepeatableType(this);
    }
}
