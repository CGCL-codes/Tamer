package ibex.ast;

import polyglot.ast.Expr;

public interface RhsLit extends RhsExpr {

    Expr lit();

    RhsLit lit(Expr lit);
}
