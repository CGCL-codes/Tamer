package org.datanucleus.store.rdbms.sql.method;

import java.util.List;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.query.compiler.CompilationComponent;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.AggregateNumericExpression;
import org.datanucleus.store.rdbms.sql.expression.NumericSubqueryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringLiteral;

/**
 * Expression handler to invoke the SQL AVG aggregation function.
 * For use in evaluating AVG({expr}) where the RDBMS supports this function.
 * Returns a NumericExpression "AVG({numericExpr})".
 */
public class AvgFunction extends SimpleNumericAggregateMethod {

    protected String getFunctionName() {
        return "AVG";
    }

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (expr != null) {
            throw new NucleusException(LOCALISER.msg("060002", getFunctionName(), expr));
        }
        if (args == null || args.size() != 1) {
            throw new NucleusException(getFunctionName() + " is only supported with a single argument");
        }
        Class returnType = Double.class;
        if (stmt.getQueryGenerator().getCompilationComponent() == CompilationComponent.RESULT) {
            JavaTypeMapping m = getMappingForClass(returnType);
            return new AggregateNumericExpression(stmt, m, getFunctionName(), args);
        } else {
            SQLExpression argExpr = (SQLExpression) args.get(0);
            SQLStatement subStmt = new SQLStatement(stmt, stmt.getRDBMSManager(), argExpr.getSQLTable().getTable(), argExpr.getSQLTable().getAlias(), null);
            subStmt.setClassLoaderResolver(clr);
            JavaTypeMapping mapping = stmt.getRDBMSManager().getMappingManager().getMappingWithDatastoreMapping(String.class, false, false, clr);
            String aggregateString = getFunctionName() + "(" + argExpr.toSQLText() + ")";
            SQLExpression aggExpr = exprFactory.newLiteral(subStmt, mapping, aggregateString);
            ((StringLiteral) aggExpr).generateStatementWithoutQuotes();
            subStmt.select(aggExpr, null);
            JavaTypeMapping subqMapping = exprFactory.getMappingForType(returnType, false);
            SQLExpression subqExpr = new NumericSubqueryExpression(stmt, subStmt);
            subqExpr.setJavaTypeMapping(subqMapping);
            return subqExpr;
        }
    }

    @Override
    protected Class getClassForMapping() {
        return double.class;
    }
}
