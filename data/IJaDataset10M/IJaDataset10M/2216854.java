package org.datanucleus.store.rdbms.sql.method;

/**
 * Expression handler to invoke the SQL TAN function.
 * For use in evaluating TAN({expr}) where the RDBMS supports this function.
 * Returns a NumericExpression "TAN({numericExpr})".
 */
public class TanFunction extends SimpleNumericMethod {

    protected String getFunctionName() {
        return "TAN";
    }

    @Override
    protected Class getClassForMapping() {
        return double.class;
    }
}
