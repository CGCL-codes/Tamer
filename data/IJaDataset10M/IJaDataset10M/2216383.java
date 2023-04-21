package ast;

import org.antlr.runtime.Token;

public class Minus_Expression extends Exp_negacao {

    public Minus_Expression(Token payload) {
        super(payload);
    }

    public Exp_atomica getExp_atomica() {
        return (Exp_atomica) getChild(0);
    }

    @Override
    public Object accept(IVisitor visitor, Object o) {
        return null;
    }
}
