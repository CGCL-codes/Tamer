package javacc.parser.ast.stmt;

import javacc.parser.ast.visitor.GenericVisitor;
import javacc.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ContinueStmt extends Statement {

    public static final ContinueStmt SINGLETON = new ContinueStmt(null);

    public final String id;

    public ContinueStmt(String id) {
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
