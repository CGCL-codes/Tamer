package org.datanucleus.store.rdbms.sql.method;

/**
 * Expression handler to invoke the SQL CEIL function.
 * For use in evaluating CEIL({expr}) where the RDBMS supports this function.
 * Returns a NumericExpression "CEIL({numericExpr})".
 */
public class CeilFunction extends SimpleNumericMethod {

    protected String getFunctionName() {
        return "CEIL";
    }

    @Override
    protected Class getClassForMapping() {
        return int.class;
    }
}
