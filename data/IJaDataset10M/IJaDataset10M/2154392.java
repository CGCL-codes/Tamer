package org.datanucleus.store.rdbms.sql.method;

import java.util.List;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;

/**
 * Method for evaluating {strExpr1}.equals(strExpr2).
 * Returns a BooleanExpression that equates to <pre>{strExpr1} = {strExpr2}</pre>
 */
public class StringEqualsMethod extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (args == null || args.size() != 1) {
            throw new NucleusException(LOCALISER.msg("060003", "endsWith", "StringExpression", 0, "StringExpression/CharacterExpression/ParameterLiteral"));
        }
        StringExpression strExpr1 = (StringExpression) expr;
        StringExpression strExpr2 = (StringExpression) args.get(0);
        return strExpr1.eq(strExpr2);
    }
}
