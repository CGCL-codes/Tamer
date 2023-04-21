package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;

/**
 * Expression handler to invoke an SQL String function that takes in an expression.
 * <ul>
 * <li>If the expression is a StringExpression will returns a StringExpression 
 *     <pre>{functionName}({stringExpr})</pre> and args aren't used</li>
 * <li>If the expression is null will return a StringExpression
 *     <pre>{functionName}({args})</pre> and expr isn't used</li>
 * </ul>
 */
public abstract class SimpleStringMethod extends AbstractSQLMethod {

    protected abstract String getFunctionName();

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (expr == null) {
            return new StringExpression(stmt, getMappingForClass(String.class), getFunctionName(), args);
        } else if (expr instanceof StringExpression) {
            ArrayList functionArgs = new ArrayList();
            functionArgs.add(expr);
            return new StringExpression(stmt, getMappingForClass(String.class), getFunctionName(), functionArgs);
        } else {
            throw new NucleusException(LOCALISER.msg("060002", getFunctionName(), expr));
        }
    }
}
