package jtq.implementation.postgreSql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import jtq.InsertResult;
import jtq.Transaction;
import jtq.column.IColumn;
import jtq.column.ISelectable;
import jtq.column.OrderBy;
import jtq.core.ATable;
import jtq.core.ITableDef;
import jtq.core.JoinTypeEnum;
import jtq.core.ParameterStatement;
import jtq.core.SqlError;
import jtq.core.QueryGenerator.IDeleteTemplate;
import jtq.core.QueryGenerator.IInsertSelectTemplate;
import jtq.core.QueryGenerator.IInsertTemplate;
import jtq.core.QueryGenerator.IJoin;
import jtq.core.QueryGenerator.IQueryGenerator;
import jtq.core.QueryGenerator.IQueryTemplate;
import jtq.core.QueryGenerator.ISetValue;
import jtq.core.QueryGenerator.IUpdateTemplate;
import jtq.core.QueryGenerator.UnionEnum;
import jtq.helper.CloseHelper;

/**
 * 
 * Query generator
 * 
 * This class generates sql queries from their provided template object.
 * 
 * Query generator is designed to be immutable so it is thread safe as long as
 * the provided templates are not being modified by another thread.
 * 
 */
public final class PGQueryGenerator implements IQueryGenerator {

    private static final PGQueryGenerator sInstance = new PGQueryGenerator();

    private PGQueryGenerator() {
    }

    /**
	 * @return Singleton instance of QueryGenerator
	 */
    public static final IQueryGenerator getInstance() {
        return sInstance;
    }

    public final String getSelectQuery(IQueryTemplate pQueryTemplate) {
        return PGQueryGenerator.buildSelectQuery(pQueryTemplate);
    }

    public final String getInsertQuery(IInsertTemplate pInsertTemplate) {
        return PGQueryGenerator.buildInsertQuery(pInsertTemplate);
    }

    public final String getUpdateQuery(IUpdateTemplate pUpdateTemplate) {
        return PGQueryGenerator.buildUpdateQuery(pUpdateTemplate);
    }

    public final String getDeleteQuery(IDeleteTemplate pDeleteTemplate) {
        return PGQueryGenerator.buildDeleteQuery(pDeleteTemplate);
    }

    /**
	 * Builds a select query with postgreSql syntax
	 */
    private static final String buildSelectQuery(IQueryTemplate pQueryTemplate) {
        if (pQueryTemplate == null) throw new IllegalArgumentException("pQueryTemplate can not be null");
        StringBuilder query = new StringBuilder("SELECT ");
        if (pQueryTemplate.getDistinct()) query.append("DISTINCT ");
        for (int columnIndex = 0; columnIndex < pQueryTemplate.getSelectColumns().size(); columnIndex++) {
            ISelectable<?, ?> column = pQueryTemplate.getSelectColumns().get(columnIndex);
            if (columnIndex > 0) query.append(',');
            query.append(column.getAlisedName());
        }
        query.append(" FROM ");
        boolean useOjSyntax = pQueryTemplate.getJoinList() != null && pQueryTemplate.getJoinList().size() > 0;
        if (useOjSyntax) query.append("{oj ");
        query.append(pQueryTemplate.getFromTable().getTableName()).append(' ');
        query.append(pQueryTemplate.getFromTable().getAlias()).append(' ');
        if (pQueryTemplate.getJoinList() != null) {
            for (IJoin join : pQueryTemplate.getJoinList()) {
                if (join.getJoinType() == JoinTypeEnum.Join) query.append("JOIN "); else if (join.getJoinType() == JoinTypeEnum.Left) query.append("LEFT JOIN "); else if (join.getJoinType() == JoinTypeEnum.Right) query.append("RIGHT JOIN "); else if (join.getJoinType() == JoinTypeEnum.Cross) query.append("CROSS JOIN "); else throw new Error("Unknown join type: " + join.getJoinType().toString());
                query.append(join.getTable().getTableName()).append(' ').append(join.getTable().getAlias());
                query.append(" ON ").append(join.getCondition().getConditionSql()).append(' ');
            }
        }
        if (useOjSyntax) query.append("}");
        if (pQueryTemplate.getWhereCondition() != null) query.append("WHERE ").append(pQueryTemplate.getWhereCondition().getConditionSql()).append(' ');
        if (pQueryTemplate.getGroupByColumns() != null && pQueryTemplate.getGroupByColumns().length > 0) {
            query.append("GROUP BY ");
            for (int groupByIndex = 0; groupByIndex < pQueryTemplate.getGroupByColumns().length; groupByIndex++) {
                IColumn<?, ?> column = pQueryTemplate.getGroupByColumns()[groupByIndex];
                if (groupByIndex > 0) query.append(',');
                query.append(column.getAlisedName()).append(' ');
            }
        }
        if (pQueryTemplate.getHavingCondition() != null) query.append("HAVING ").append(pQueryTemplate.getHavingCondition().getConditionSql()).append(' ');
        if (pQueryTemplate.getOrderByColumns() != null && pQueryTemplate.getOrderByColumns().size() > 0) {
            query.append("ORDER BY ");
            int orderByIndex = 0;
            boolean isUnionQuery = pQueryTemplate.getUnionType() != UnionEnum.None;
            for (OrderBy orderBy : pQueryTemplate.getOrderByColumns()) {
                if (orderByIndex > 0) query.append(',');
                if (isUnionQuery) query.append(orderBy.getSelectable().getNonAlisedName()); else query.append(orderBy.getSelectable().getAlisedName());
                switch(orderBy.getOrderBy()) {
                    case Default:
                        break;
                    case Ascending:
                        query.append(" ASC");
                        break;
                    case Descending:
                        query.append(" DESC");
                        break;
                    default:
                        throw new Error("Unknown order by type: " + orderBy.getOrderBy().toString());
                }
                orderByIndex++;
            }
            query.append(' ');
        }
        if (pQueryTemplate.getLimit() != null) query.append("LIMIT ").append(pQueryTemplate.getLimit().toString());
        List<IQueryTemplate> unionTemplates = pQueryTemplate.getUnionTemplates();
        if (unionTemplates != null) {
            for (IQueryTemplate unionTemplate : unionTemplates) {
                switch(unionTemplate.getUnionType()) {
                    case Union:
                        query.append("UNION ");
                        break;
                    case UnionAll:
                        query.append("UNION ALL ");
                        break;
                    case Intersect:
                        query.append("INTERSECT ");
                        break;
                    case Except:
                        query.append("EXCEPT ");
                        break;
                    case None:
                        throw new Error("Union type of 'None' is invalid");
                    default:
                        throw new Error("Unknown union type: " + unionTemplate.getUnionType().toString());
                }
                query.append(buildSelectQuery(unionTemplate));
            }
        }
        return query.toString();
    }

    /**
	 * Builds an insert query with postgreSql syntax
	 */
    private static final String buildInsertQuery(IInsertTemplate pInsertTemplate) {
        StringBuilder insertQuery = new StringBuilder("INSERT INTO ");
        insertQuery.append(pInsertTemplate.getTable().getTableName()).append('(');
        int index = 0;
        for (ISetValue setValue : pInsertTemplate.getValueList()) {
            if (index > 0) insertQuery.append(',');
            insertQuery.append(setValue.getColumn().getColumnName());
            index++;
        }
        insertQuery.append(") VALUES (");
        for (index = 0; index < pInsertTemplate.getValueList().size(); index++) {
            if (index > 0) insertQuery.append(',');
            ISetValue setValue = pInsertTemplate.getValueList().get(index);
            if (setValue.getFunction() != null) insertQuery.append(setValue.getFunction().getAlisedName()); else insertQuery.append("?");
        }
        insertQuery.append(')');
        return insertQuery.toString();
    }

    /**
	 * Builds an update query with postgreSql syntax
	 */
    private static final String buildUpdateQuery(IUpdateTemplate pUpdateTemplate) {
        if (pUpdateTemplate == null) throw new IllegalArgumentException("pUpdateTemplate can not be null");
        if (pUpdateTemplate.getSetValueList().size() == 0) throw new IllegalArgumentException("pUpdateTemplate setvalue list has not items");
        StringBuilder updateSql = new StringBuilder("UPDATE ");
        updateSql.append(pUpdateTemplate.getTable().getTableName()).append(" ");
        updateSql.append(pUpdateTemplate.getTable().getAlias()).append(" ");
        updateSql.append(" SET ");
        int index = 0;
        for (ISetValue setValue : pUpdateTemplate.getSetValueList()) {
            if (index++ > 0) updateSql.append(',');
            updateSql.append(setValue.getColumn().getColumnName()).append(" = ");
            if (setValue.getFunction() != null) updateSql.append(setValue.getFunction().getAlisedName()); else updateSql.append("?");
        }
        if (pUpdateTemplate.getWhereCondition() != null) updateSql.append(" WHERE ").append(pUpdateTemplate.getWhereCondition().getConditionSql());
        return updateSql.toString();
    }

    /**
	 * Builds a delete query with postgreSql syntax
	 */
    private static final String buildDeleteQuery(IDeleteTemplate pDeleteTemplate) {
        StringBuilder deleteSql = new StringBuilder("DELETE FROM ");
        deleteSql.append(pDeleteTemplate.getTable().getTableName());
        deleteSql.append(" ");
        deleteSql.append(pDeleteTemplate.getTable().getAlias());
        if (pDeleteTemplate.getWhereCondition() != null) {
            deleteSql.append(" WHERE ");
            deleteSql.append(pDeleteTemplate.getWhereCondition().getConditionSql());
        }
        return deleteSql.toString();
    }

    @Override
    public String getInsertSelectQuery(IInsertSelectTemplate pTemplate) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(pTemplate.getTable().getTableName()).append(" (");
        int counter = 0;
        for (IColumn<?, ?> column : pTemplate.getColumns()) {
            if (counter++ > 0) query.append(",");
            query.append(column.getColumnName());
        }
        query.append(") ").append(getSelectQuery(pTemplate.getQueryTemplate()));
        return query.toString();
    }

    public InsertResult insertGetGeneratedKeys(IInsertTemplate pTemplate, Transaction pTransaction) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append(buildInsertQuery(pTemplate));
        if (pTemplate.getTable().hasGeneratedColumns()) {
            sql.append(" RETURNING ");
            int columnCount = 0;
            for (IColumn<?, ?> column : pTemplate.getTable().getColumns()) {
                if (column.isGenerated()) {
                    if (columnCount++ > 0) sql.append(",");
                    sql.append(column.getColumnName());
                }
            }
        }
        PreparedStatement statement = null;
        try {
            statement = pTransaction.prepareStatement(pTemplate.getTable().getDatabase(), sql.toString());
            ParameterStatement parameterStatement = new ParameterStatement(statement, sql.toString());
            pTemplate.setParameters(parameterStatement);
            sql = new StringBuilder(parameterStatement.getSql());
            InsertResult insertResult;
            if (pTemplate.getTable().hasGeneratedColumns()) {
                ResultSet result = statement.executeQuery();
                result.next();
                Hashtable<IColumn<?, ?>, Object> keysHashtable = null;
                keysHashtable = new Hashtable<IColumn<?, ?>, Object>();
                int columnCount = 1;
                for (IColumn<?, ?> column : pTemplate.getTable().getColumns()) {
                    if (column.isGenerated()) {
                        keysHashtable.put(column, column.getValue(result, columnCount));
                        columnCount++;
                    }
                }
                insertResult = new InsertResultImp(sql.toString(), Integer.MIN_VALUE, keysHashtable);
            } else {
                statement.executeUpdate();
                insertResult = new InsertResultImp(sql.toString(), Integer.MIN_VALUE, null);
            }
            return insertResult;
        } catch (SQLException e) {
            pTransaction.setRollbackOnly();
            throw e;
        } finally {
            CloseHelper.close(statement);
        }
    }

    private class InsertResultImp implements InsertResult {

        private final String mSql;

        private final int mEffectedRows;

        private final Hashtable<IColumn<?, ?>, Object> mGeneratedKeysHash;

        public InsertResultImp(String pSql, int pEffectedRows, Hashtable<IColumn<?, ?>, Object> pGeneratedKeysHash) {
            mSql = pSql;
            mGeneratedKeysHash = pGeneratedKeysHash;
            mEffectedRows = pEffectedRows;
        }

        @Override
        public <TYPE, SUPER_TYPE> TYPE getGeneratedKeyValue(IColumn<TYPE, SUPER_TYPE> pColumn) {
            if (pColumn == null) throw new NullPointerException("pColumn can not be null");
            @SuppressWarnings("unchecked") TYPE value = (TYPE) mGeneratedKeysHash.get(pColumn);
            return value;
        }

        @Override
        public String getSql() {
            return mSql;
        }

        @Override
        public int getEffectedRows() {
            return mEffectedRows;
        }
    }

    @Override
    public ITableDef getTableDefinition(ATable<?> pTable) {
        try {
            List<ITableDef> tableList = TableDefinition.loadTables(pTable.getDatabase(), pTable);
            return tableList.size() == 1 ? tableList.get(0) : null;
        } catch (SQLException e) {
            throw new SqlError(e);
        }
    }
}
