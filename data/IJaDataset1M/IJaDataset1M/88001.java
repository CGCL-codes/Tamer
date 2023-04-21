package de.odysseus.el.tree.impl.ast;

import javax.el.ELContext;
import de.odysseus.el.tree.Bindings;

public final class AstNested extends AstRightValue {

    private final AstNode child;

    public AstNested(AstNode child) {
        this.child = child;
    }

    @Override
    public Object eval(Bindings bindings, ELContext context) {
        return child.eval(bindings, context);
    }

    @Override
    public String toString() {
        return "(...)";
    }

    @Override
    public void appendStructure(StringBuilder b, Bindings bindings) {
        b.append("(");
        child.appendStructure(b, bindings);
        b.append(")");
    }

    public int getCardinality() {
        return 1;
    }

    public AstNode getChild(int i) {
        return i == 0 ? child : null;
    }
}
