package kinky.parsers.java.ast.body;

import java.util.List;
import kinky.parsers.java.ast.TypeParameter;
import kinky.parsers.java.ast.expr.AnnotationExpr;
import kinky.parsers.java.ast.expr.NameExpr;
import kinky.parsers.java.ast.stmt.BlockStmt;
import kinky.parsers.java.ast.type.Type;
import kinky.parsers.java.ast.visitor.GenericVisitor;
import kinky.parsers.java.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class MethodDeclaration extends BodyDeclaration {

    public final int modifiers;

    public final List<AnnotationExpr> annotations;

    public final List<TypeParameter> typeParameters;

    public final Type type;

    public final String name;

    public final List<Parameter> parameters;

    public final int arrayCount;

    public final List<NameExpr> throws_;

    public final BlockStmt block;

    public MethodDeclaration(int line, int column, int modifiers, List<AnnotationExpr> annotations, List<TypeParameter> typeParameters, Type type, String name, List<Parameter> parameters, int arrayCount, List<NameExpr> throws_, BlockStmt block) {
        super(line, column);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.typeParameters = typeParameters;
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.arrayCount = arrayCount;
        this.throws_ = throws_;
        this.block = block;
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
