package org.datanucleus.store.rdbms.sql.expression;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.identity.OID;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.query.expression.Expression;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.mapped.mapping.PersistableMapping;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * Representation of an Object literal in a query.
 */
public class ObjectLiteral extends ObjectExpression implements SQLLiteral {

    private Object value;

    /**
     * Constructor for an Object literal with a value.
     * @param stmt the SQL statement
     * @param mapping the mapping
     * @param value the Object value
     * @param parameterName Name of the parameter that this represents if any (as JDBC "?")
     */
    public ObjectLiteral(SQLStatement stmt, JavaTypeMapping mapping, Object value, String parameterName) {
        super(stmt, null, mapping);
        this.value = value;
        this.parameterName = parameterName;
        if (parameterName != null) {
            if (value != null) {
                this.subExprs = new ColumnExpressionList();
                addSubexpressionsForValue(this.value, mapping);
            }
            if (mapping.getNumberOfDatastoreMappings() == 1) {
                st.appendParameter(parameterName, mapping, this.value);
            }
        } else {
            this.subExprs = new ColumnExpressionList();
            if (value != null) {
                addSubexpressionsForValue(this.value, mapping);
            }
            st.append(subExprs.toString());
        }
    }

    /**
     * Method to add subExprs for the supplied mapping, consistent with the supplied value.
     * The value can be a persistent object, or an identity (datastore/application).
     * @param value The value
     * @param mapping The mapping
     */
    private void addSubexpressionsForValue(Object value, JavaTypeMapping mapping) {
        RDBMSStoreManager storeMgr = stmt.getRDBMSManager();
        ClassLoaderResolver clr = stmt.getClassLoaderResolver();
        String objClassName = value.getClass().getName();
        if (mapping instanceof PersistableMapping) {
            objClassName = mapping.getType();
        } else if (value instanceof OID) {
            objClassName = ((OID) value).getPcClass();
        }
        AbstractClassMetaData cmd = storeMgr.getNucleusContext().getMetaDataManager().getMetaDataForClass(objClassName, clr);
        if (cmd != null) {
            int numCols = mapping.getNumberOfDatastoreMappings();
            for (int i = 0; i < numCols; i++) {
                ColumnExpression colExpr = null;
                if (parameterName == null && mapping instanceof PersistableMapping) {
                    Object colValue = ((PersistableMapping) mapping).getValueForDatastoreMapping(stmt.getRDBMSManager().getNucleusContext(), i, value);
                    colExpr = new ColumnExpression(stmt, colValue);
                } else {
                    colExpr = new ColumnExpression(stmt, parameterName, mapping, value, i);
                }
                subExprs.addExpression(colExpr);
            }
        } else {
            NucleusLogger.GENERAL.error(">> ObjectLiteral doesn't yet cater for values of type " + StringUtils.toJVMIDString(value));
        }
    }

    public Object getValue() {
        return value;
    }

    /**
     * Method called when the query contains "object == value".
     * @param expr The expression
     * @return The resultant expression for this query relation
     */
    public BooleanExpression eq(SQLExpression expr) {
        addSubexpressionsToRelatedExpression(expr);
        if (isParameter() || expr.isParameter()) {
            return new BooleanExpression(this, Expression.OP_EQ, expr);
        } else if (value == null) {
            return new NullLiteral(stmt, null, null, null).eq(expr);
        } else if (expr instanceof ObjectExpression) {
            return ExpressionUtils.getEqualityExpressionForObjectExpressions(this, (ObjectExpression) expr, true);
        } else {
            return super.eq(expr);
        }
    }

    /**
     * Method called when the query contains "object NOTEQUALS value".
     * @param expr The expression
     * @return The resultant expression for this query relation
     */
    public BooleanExpression ne(SQLExpression expr) {
        addSubexpressionsToRelatedExpression(expr);
        if (isParameter() || expr.isParameter()) {
            return new BooleanExpression(this, Expression.OP_NOTEQ, expr);
        } else if (value == null) {
            return new NullLiteral(stmt, null, null, null).ne(expr);
        } else if (expr instanceof ObjectExpression) {
            return ExpressionUtils.getEqualityExpressionForObjectExpressions(this, (ObjectExpression) expr, false);
        } else {
            return super.ne(expr);
        }
    }

    public String toString() {
        if (value != null) {
            return super.toString() + " = " + value.toString();
        } else {
            return super.toString() + " = NULL";
        }
    }

    public void setNotParameter() {
        if (parameterName == null) {
            return;
        }
        parameterName = null;
        st.clearStatement();
        setStatement();
    }

    protected void setStatement() {
        if (parameterName == null) {
            this.subExprs = new ColumnExpressionList();
            if (value != null) {
                addSubexpressionsForValue(this.value, mapping);
            }
            st.append(subExprs.toString());
        } else {
            st.appendParameter(parameterName, mapping, this.value);
        }
    }
}
