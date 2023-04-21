package japa.parser.ast.stmt;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ForeachStmt extends Statement {

    private final VariableDeclarationExpr var;

    private final Expression iterable;

    private final Statement body;

    public ForeachStmt(int line, int column, VariableDeclarationExpr var, Expression iterable, Statement body) {
        super(line, column);
        this.var = var;
        this.iterable = iterable;
        this.body = body;
    }

    public VariableDeclarationExpr getVariable() {
        return var;
    }

    public Expression getIterable() {
        return iterable;
    }

    public Statement getBody() {
        return body;
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
