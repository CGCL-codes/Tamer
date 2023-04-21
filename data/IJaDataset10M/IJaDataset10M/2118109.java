package javacc.parser.ast.body;

import java.util.List;
import javacc.parser.ast.expr.AnnotationExpr;
import javacc.parser.ast.expr.Expression;
import javacc.parser.ast.type.Type;
import javacc.parser.ast.visitor.GenericVisitor;
import javacc.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class AnnotationMemberDeclaration extends BodyDeclaration {

    public final int modifiers;

    public final List<AnnotationExpr> annotations;

    public final Type type;

    public final String name;

    public final Expression defaultValue;

    public AnnotationMemberDeclaration(int modifiers, List<AnnotationExpr> annotations, Type type, String name, Expression defaultValue) {
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
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
