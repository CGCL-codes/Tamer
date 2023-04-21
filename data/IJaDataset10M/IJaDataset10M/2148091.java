package org.datanucleus.store.rdbms.sql.operation;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.SQLText;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;

/**
 * Implementation of CONCAT, using SQL CONCAT operator.
 * Results in
 * <pre>
 * CAST(expr1 AS VARCHAR(4000)) || CAST(expr2 AS VARCHAR(4000))
 * </pre>
 */
public class Concat3Operation extends AbstractSQLOperation {

    public SQLExpression getExpression(SQLExpression expr, SQLExpression expr2) {
        JavaTypeMapping m = exprFactory.getMappingForType(String.class, false);
        List types = new ArrayList();
        types.add("VARCHAR(4000)");
        List argsOp1 = new ArrayList();
        argsOp1.add(expr);
        SQLExpression firstExpr = new StringExpression(expr.getSQLStatement(), m, "CAST", argsOp1, types).encloseInParentheses();
        List argsOp2 = new ArrayList();
        argsOp2.add(expr2);
        SQLExpression secondExpr = new StringExpression(expr.getSQLStatement(), m, "CAST", argsOp2, types).encloseInParentheses();
        StringExpression concatExpr = new StringExpression(expr.getSQLStatement(), null, null);
        SQLText sql = concatExpr.toSQLText();
        sql.clearStatement();
        sql.append(firstExpr);
        sql.append("||");
        sql.append(secondExpr);
        List args = new ArrayList();
        args.add(concatExpr);
        return new StringExpression(expr.getSQLStatement(), m, "CAST", args, types);
    }
}
