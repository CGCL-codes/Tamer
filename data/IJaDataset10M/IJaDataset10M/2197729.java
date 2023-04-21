package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.TemporalExpression;

/**
 * Method for evaluating {dateExpr}.getDay().
 * Returns a NumericExpression that equates to <pre>DAY(dateExpr)</pre>
 */
public class DateGetDayMethod extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (!(expr instanceof TemporalExpression)) {
            throw new NucleusException(LOCALISER.msg("060001", "getDay()", expr));
        }
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(expr);
        return new NumericExpression(stmt, getMappingForClass(int.class), "DAY", funcArgs);
    }
}
