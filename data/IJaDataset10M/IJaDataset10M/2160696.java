package javacc.parser.ast.body;

import java.util.List;
import javacc.parser.ast.Node;
import javacc.parser.ast.expr.AnnotationExpr;
import javacc.parser.ast.type.Type;
import javacc.parser.ast.visitor.GenericVisitor;
import javacc.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class Parameter extends Node {

    public final int modifiers;

    public final List<AnnotationExpr> annotations;

    public final Type type;

    public final boolean isVarArgs;

    public final VariableDeclaratorId id;

    public Parameter(int modifiers, List<AnnotationExpr> annotations, Type type, boolean isVarArgs, VariableDeclaratorId id) {
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.isVarArgs = isVarArgs;
        this.id = id;
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
