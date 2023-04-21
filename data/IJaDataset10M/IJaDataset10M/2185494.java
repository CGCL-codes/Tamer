package org.datanucleus.store.rdbms.sql.method;

import java.util.List;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Expression handler to evaluate {objectExpression}.getClass().
 */
public class ObjectGetClassMethod extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        throw new NucleusException("getClass is not yet supported for " + expr);
    }
}
