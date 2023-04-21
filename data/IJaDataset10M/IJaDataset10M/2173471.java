package kinky.parsers.java.ast.stmt;

import kinky.parsers.java.ast.visitor.GenericVisitor;
import kinky.parsers.java.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class BreakStmt extends Statement {

    public final String id;

    public BreakStmt(int line, int column, String id) {
        super(line, column);
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
