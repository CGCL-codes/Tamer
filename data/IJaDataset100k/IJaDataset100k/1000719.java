package com.kopiright.tanit.plsql;

import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.compiler.base.UnpositionedError;
import com.kopiright.xkopi.comp.sqlc.SearchCondition;
import com.kopiright.xkopi.comp.sqlc.SimpleSearchCondition;

/**
 * This class represents a binary search condition
 */
public class PLSQLSimpleSearchCondition extends PLSQLSearchCondition {

    /**
   * Constructor
   */
    public PLSQLSimpleSearchCondition(TokenReference ref, PLSQLPredicate expr) {
        super(ref);
        this.expr = expr;
    }

    /**
   * Accepts a visitor.
   *
   * @param	visitor			the visitor
   */
    public void accept(PLVisitor visitor) throws PositionedError {
        visitor.visitSQLSimpleSearchCondition(this, expr);
    }

    public void analyse(PLSQLContext context, Environment env) throws PositionedError {
        expr.analyse(context, env);
    }

    public SearchCondition toXKopi(PLSQLContext context, Environment env) throws PositionedError {
        return new SimpleSearchCondition(getTokenReference(), expr.toXKopi(context, env));
    }

    private PLSQLPredicate expr;
}
