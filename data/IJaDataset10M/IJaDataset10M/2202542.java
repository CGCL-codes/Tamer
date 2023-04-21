package javacc.parser.ast.expr;

import javacc.parser.ast.Node;
import javacc.parser.ast.visitor.GenericVisitor;
import javacc.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class MemberValuePair extends Node {

    public final String name;

    public final Expression value;

    public MemberValuePair(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
