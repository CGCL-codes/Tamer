package kinky.parsers.java.ast.stmt;

import kinky.parsers.java.ast.expr.Expression;
import kinky.parsers.java.ast.visitor.GenericVisitor;
import kinky.parsers.java.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ReturnStmt extends Statement {

    public final Expression expr;

    public ReturnStmt(int line, int column, Expression expr) {
        super(line, column);
        this.expr = expr;
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
