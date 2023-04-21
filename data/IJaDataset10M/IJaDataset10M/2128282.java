package org.jmlspecs.jml6.boogie.ast;

import org.eclipse.jdt.core.dom.ASTNode;
import org.jmlspecs.jml6.core.boogie.BoogieSource;

public class BooleanLiteral extends Expression {

    private boolean value;

    public BooleanLiteral(boolean value, ASTNode javaNode, Scope scope) {
        super(javaNode, scope);
        this.value = value;
    }

    public void toBuffer(BoogieSource out) {
        out.append(String.valueOf(value));
    }

    public void traverse(Visitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
