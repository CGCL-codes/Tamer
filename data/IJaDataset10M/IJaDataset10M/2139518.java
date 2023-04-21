package net.jadoth.sqlengine.sql.statements;

import java.sql.ResultSet;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.sqlengine.SqlEngineException;
import net.jadoth.sqlengine.sql.types.SqlContext;
import net.jadoth.sqlengine.sql.types.SqlExecutor;

public interface SqlStatementExecutingQuery extends SqlStatement<ResultSet> {

    @Override
    public SqlStatementExecutingQuery.Executor getExecutor();

    public SqlStatementExecutingQuery setExecutor(SqlStatementExecutingQuery.Executor executor);

    public interface Executor extends SqlExecutor<SqlStatement<ResultSet>, ResultSet, ResultSet, ResultSet> {

        @Override
        public ResultSet execute(SqlContext<?> context, SqlStatement<ResultSet> statement, XGettingMap<Object, Object> assemblyVariables) throws SqlEngineException;
    }
}
