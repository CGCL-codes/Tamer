package org.datanucleus.store.rdbms.sql.method;

import java.util.Date;
import java.util.List;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.TemporalExpression;

/**
 * Expression handler to invoke the SQL CURRENT_TIME function.
 * For use in evaluating CURRENT_TIME where the RDBMS supports this function.
 * Returns a TemporalExpression "CURRENT_TIME".
 */
public class CurrentTimeFunction extends AbstractSQLMethod {

    protected String getFunctionName() {
        return "CURRENT_TIME";
    }

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (expr == null) {
            SQLExpression dateExpr = new TemporalExpression(stmt, getMappingForClass(getClassForMapping()), getFunctionName(), args);
            dateExpr.toSQLText().clearStatement();
            dateExpr.toSQLText().append(getFunctionName());
            return dateExpr;
        } else {
            throw new NucleusException(LOCALISER.msg("060002", getFunctionName(), expr));
        }
    }

    protected Class getClassForMapping() {
        return Date.class;
    }
}
