package org.datanucleus.store.rdbms.sql.method;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.ByteLiteral;
import org.datanucleus.store.rdbms.sql.expression.FloatingPointLiteral;
import org.datanucleus.store.rdbms.sql.expression.IllegalExpressionOperationException;
import org.datanucleus.store.rdbms.sql.expression.IntegerLiteral;
import org.datanucleus.store.rdbms.sql.expression.NullLiteral;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLLiteral;

/**
 * Expression handler to evaluate Math.sqrt({expression}).
 * Returns a NumericExpression.
 */
public class MathSqrtMethod extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression ignore, List args) {
        if (args == null || args.size() == 0) {
            throw new NucleusUserException("Cannot invoke Math.sqrt without an argument");
        }
        SQLExpression expr = (SQLExpression) args.get(0);
        if (expr == null) {
            return new NullLiteral(stmt, null, null, null);
        } else if (expr instanceof SQLLiteral) {
            if (expr instanceof ByteLiteral) {
                int originalValue = ((BigInteger) ((ByteLiteral) expr).getValue()).intValue();
                BigInteger absValue = new BigInteger(String.valueOf(Math.sqrt(originalValue)));
                return new ByteLiteral(stmt, expr.getJavaTypeMapping(), absValue, null);
            } else if (expr instanceof IntegerLiteral) {
                int originalValue = ((Number) ((IntegerLiteral) expr).getValue()).intValue();
                Double absValue = new Double(Math.sqrt(originalValue));
                return new FloatingPointLiteral(stmt, expr.getJavaTypeMapping(), absValue, null);
            } else if (expr instanceof FloatingPointLiteral) {
                double originalValue = ((BigDecimal) ((FloatingPointLiteral) expr).getValue()).doubleValue();
                Double absValue = new Double(Math.sqrt(originalValue));
                return new FloatingPointLiteral(stmt, expr.getJavaTypeMapping(), absValue, null);
            }
            throw new IllegalExpressionOperationException("Math.sqrt()", expr);
        } else {
            return exprFactory.invokeMethod(stmt, null, "sqrt", null, args);
        }
    }
}
