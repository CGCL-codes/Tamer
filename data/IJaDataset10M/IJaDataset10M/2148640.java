package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;
import org.datanucleus.store.rdbms.sql.expression.TemporalExpression;

/**
 * Method for evaluating {dateExpr}.getSecond().
 * Returns a NumericExpression that equates to <pre>TO_NUMBER(TO_CHAR(dateExpr, "SS"))</pre>
 */
public class DateGetSecond2Method extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (!(expr instanceof TemporalExpression)) {
            throw new NucleusException(LOCALISER.msg("060001", "getSecond()", expr));
        }
        RDBMSStoreManager storeMgr = stmt.getRDBMSManager();
        JavaTypeMapping mapping = storeMgr.getMappingManager().getMapping(String.class);
        SQLExpression hh = exprFactory.newLiteral(stmt, mapping, "SS");
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(expr);
        funcArgs.add(hh);
        ArrayList funcArgs2 = new ArrayList();
        funcArgs2.add(new StringExpression(stmt, mapping, "TO_CHAR", funcArgs));
        return new NumericExpression(stmt, getMappingForClass(int.class), "TO_NUMBER", funcArgs2);
    }
}
