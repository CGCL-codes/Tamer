package javacc.parser.ast.expr;

import javacc.parser.ast.visitor.GenericVisitor;
import javacc.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class SingleMemberAnnotationExpr extends AnnotationExpr {

    public final NameExpr name;

    public final Expression memberValue;

    public SingleMemberAnnotationExpr(NameExpr name, Expression memberValue) {
        this.name = name;
        this.memberValue = memberValue;
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
