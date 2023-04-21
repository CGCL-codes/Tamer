package ast;

import org.antlr.runtime.Token;
import ast.terminais.Identifier;

public class Id_array extends Node {

    public Id_array(Token payload) {
        super(payload);
    }

    public Identifier getId() {
        return (Identifier) this.getChild(0);
    }

    public Aritmetic_Expression getExpression() {
        return (Aritmetic_Expression) this.getChild(1);
    }

    @Override
    public Object accept(IVisitor visitor, Object o) {
        return visitor.visit(this, o);
    }
}
