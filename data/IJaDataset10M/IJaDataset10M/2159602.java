package javacc.parser.ast.expr;

import javacc.parser.ast.type.Type;
import javacc.parser.ast.visitor.GenericVisitor;
import javacc.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class InstanceOfExpr extends Expression {

    public final Expression expr;

    public final Type type;

    public InstanceOfExpr(Expression expr, Type type) {
        this.expr = expr;
        this.type = type;
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
