package com.kopiright.tanit.plsql;

import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.compiler.base.UnpositionedError;
import com.kopiright.xkopi.comp.sqlc.Predicate;
import com.kopiright.xkopi.comp.sqlc.LikePredicate;

public class PLSQLLikePredicate extends PLSQLPredicate {

    /**
   * Constructor
   * @param	ref		the token reference for this statement
   * @param	sens
   * @param	expr
   * @param	escape
   */
    public PLSQLLikePredicate(TokenReference ref, PLSQLExpression expr) {
        super(ref);
        this.expr = expr;
    }

    /**
   * Accepts a visitor.
   *
   * @param	visitor			the visitor
   */
    public void accept(PLVisitor visitor) throws PositionedError {
        visitor.visitSQLLikePredicate(this, expr);
    }

    public void analyse(PLSQLContext context, Environment env) throws PositionedError {
        expr.analyse(context, env);
    }

    public Predicate toXKopi(PLSQLContext context, Environment env) throws PositionedError {
        return new LikePredicate(getTokenReference(), null, expr.toXKopi(context, env), null);
    }

    private PLSQLExpression expr;
}
