package net.jadoth.sqlengine.sql.types;

import static net.jadoth.Jadoth.coalesce;
import static net.jadoth.Jadoth.notNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.jadoth.collections.X;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.collections.types.XReferencing;
import net.jadoth.lang.functional.IndexProcedure;
import net.jadoth.lang.functional.Procedure;
import net.jadoth.sqlengine.SqlEngineException;
import net.jadoth.sqlengine.SqlEngineExecutionException;
import net.jadoth.sqlengine.SqlUtils;
import net.jadoth.sqlengine.dbms.DbmsAdaptor;
import net.jadoth.sqlengine.dbms.DbmsConnectionProvider;
import net.jadoth.sqlengine.dbms.DbmsSqlSyntax;
import net.jadoth.sqlengine.dbms.DbmsTableManager;
import net.jadoth.sqlengine.sql.definitions.BaseTableColumn;
import net.jadoth.sqlengine.sql.definitions.SqlBaseTable;
import net.jadoth.sqlengine.sql.definitions.SqlDescribedTable;
import net.jadoth.sqlengine.sql.definitions.SqlViewedTable;
import net.jadoth.sqlengine.sql.statements.SqlPlainString;
import net.jadoth.sqlengine.sql.statements.SqlStatement;
import net.jadoth.sqlengine.sql.statements.SqlStatementExecutingCall;
import net.jadoth.sqlengine.sql.statements.SqlStatementExecutingQuery;
import net.jadoth.sqlengine.sql.statements.SqlStatementExecutingQueryCollection;
import net.jadoth.sqlengine.sql.statements.SqlStatementExecutingQuerySingleValue;
import net.jadoth.sqlengine.sql.statements.SqlStatementExecutingUpdate;
import net.jadoth.sqlengine.sql.syntax.SQL;
import net.jadoth.util.ItemMatchResult;
import net.jadoth.util.ItemMatcher;
import net.jadoth.util.KeyValue;
import net.jadoth.util.jdbc.JdbcExecution;
import net.jadoth.util.jdbc.JdbcExecutor;

public interface SqlContext<A extends DbmsAdaptor<A>> extends DbmsTableManager.Configuration<A>, JdbcExecutor {

    public Configuration<A> configuration();

    public DbmsConnectionProvider<A> connectionProvider();

    public SqlCatalog<?> catalog();

    public SqlTransaction<A> createTransaction();

    public Integer execute(SqlStatementExecutingUpdate statement);

    public ResultSet execute(SqlStatementExecutingQuery statement);

    public <R> R execute(SqlStatementExecutingQuerySingleValue<R> statement);

    public <R> XGettingCollection<R> execute(SqlStatementExecutingQueryCollection<R> statement);

    public <R> R execute(SqlStatementExecutingCall<R> statement);

    public <R> R execute(SqlPlainString<R> statement);

    public Integer execute(SqlStatementExecutingUpdate statement, XGettingMap<Object, Object> assemblyVariables);

    public ResultSet execute(SqlStatementExecutingQuery statement, XGettingMap<Object, Object> assemblyVariables);

    public <R> R execute(SqlStatementExecutingQuerySingleValue<R> statement, XGettingMap<Object, Object> assemblyVariables);

    public <R> XGettingCollection<R> execute(SqlStatementExecutingQueryCollection<R> statement, XGettingMap<Object, Object> assemblyVariables);

    public <R> R execute(SqlStatementExecutingCall<R> statement, XGettingMap<Object, Object> assemblyVariables);

    public <R> R execute(SqlPlainString<R> statement, XGettingMap<Object, Object> assemblyVariables);

    public DbmsTableManager<A> tableManager();

    public Integer create(SqlDescribedTable table);

    public Integer create(SqlBaseTable table);

    public Integer create(SqlBaseTable table, boolean onlyLocalMembers);

    public Integer create(SqlViewedTable view);

    public Integer drop(SqlDescribedTable table);

    /**
	 * Issues a simple {@code DROP TABLE} SQL command to the database.
	 *
	 * @param table the table to be dropped
	 * @return the value returned by the execution.
	 */
    public Integer drop(SqlBaseTable table);

    public Integer drop(SqlViewedTable table);

    /**
	 * Issues a simple {@code TRUNCATE TABLE} SQL command to the database.
	 *
	 * @param table the table to be truncated.
	 * @return the value returned by the execution.
	 */
    public Integer truncate(SqlBaseTable table);

    /**
	 * Issues whatever DBMS-specific command(s) to delete a table as fast as possible, ignoring any constraints
	 * or dependencies if possible.
	 *
	 * @param table the table to be killed.
	 * @return
	 */
    public Object kill(SqlBaseTable table);

    /**
	 * Issues whatever DBMS-specific command(s) to delete the contents of a table as fast as , ignoring any constraints
	 * or dependencies if possible.
	 *
	 * @param table the table to be purged.
	 * @return
	 */
    public Object purge(SqlBaseTable table);

    public Procedure<SqlBaseTable> getTableDropper();

    public Procedure<SqlBaseTable> getTableCreator();

    @Override
    public <R> R executeJdbc(JdbcExecution<R> jdbcExecution);

    @Override
    public void close(Statement statement);

    @Override
    public void close(ResultSet resultSet);

    public interface Configuration<A extends DbmsAdaptor<A>> extends DbmsAdaptor.Member<A> {

        public SqlStatementExecutingUpdate.Executor getUpdateExecutor();

        public SqlStatementExecutingQuery.Executor getQueryExecutor();

        public <R> SqlStatementExecutingQuerySingleValue.Executor<R> getSingleValueExecutor(Class<R> returnValueType);

        public <R> SqlStatementExecutingQueryCollection.Executor<R> getCollectionExecutor(Class<R> returnValueType);

        public <R> SqlStatementExecutingCall.Executor<R> getCallExecutor(Class<R> returnValueType);

        public Integer getDefaultIsolationLevel();

        public class Implementation<A extends DbmsAdaptor<A>> extends DbmsAdaptor.Member.Implementation<A> implements Configuration<A> {

            public Implementation(final A dbms) {
                super(dbms);
            }

            @Override
            public SqlStatementExecutingUpdate.Executor getUpdateExecutor() {
                return SqlExecutor.simpleUpdate;
            }

            @Override
            public SqlStatementExecutingQuery.Executor getQueryExecutor() {
                return SqlExecutor.simpleQuery;
            }

            @Override
            public <R> SqlStatementExecutingQuerySingleValue.Executor<R> getSingleValueExecutor(final Class<R> returnValueType) {
                return SqlUtils.singleValueExecutor(returnValueType);
            }

            @Override
            public <R> SqlStatementExecutingQueryCollection.Executor<R> getCollectionExecutor(final Class<R> returnValueType) {
                return SqlUtils.collectionValueExecutor(returnValueType);
            }

            @Override
            public <R> SqlStatementExecutingCall.Executor<R> getCallExecutor(final Class<R> returnValueType) {
                return SqlUtils.callExecutor(returnValueType);
            }

            @Override
            public Integer getDefaultIsolationLevel() {
                return null;
            }
        }
    }

    public class Implementation<A extends DbmsAdaptor<A>> extends DbmsAdaptor.Member.Implementation<A> implements SqlContext<A> {

        public static <A extends DbmsAdaptor<A>> SqlContext.Implementation<A> New(final DbmsConnectionProvider<A> connectionProvider) {
            return new SqlContext.Implementation<A>(connectionProvider);
        }

        final Configuration<A> configuration;

        final DbmsConnectionProvider<A> connectionProvider;

        final XReferencing<SqlContext.Implementation<A>> selfRef = X.Constant(this);

        final Procedure<SqlBaseTable> tableDropper = new Procedure<SqlBaseTable>() {

            @Override
            public void apply(final SqlBaseTable e) {
                if (e == null) {
                    return;
                }
                SqlContext.Implementation.this.drop(e);
            }
        };

        final Procedure<SqlBaseTable> tableCreator = new Procedure<SqlBaseTable>() {

            @Override
            public void apply(final SqlBaseTable e) {
                if (e == null) {
                    return;
                }
                SqlContext.Implementation.this.create(e);
            }
        };

        final ItemMatcher<SqlBaseTable> tableMatcher;

        final ItemMatcher<BaseTableColumn.Descriptor> columnMatcher;

        public Implementation(final DbmsConnectionProvider<A> connectionProvider) {
            this(connectionProvider, new Configuration.Implementation<A>(connectionProvider.dbms()));
        }

        public Implementation(final DbmsConnectionProvider<A> connectionProvider, final Configuration<A> configuration) {
            super(connectionProvider.dbms());
            final A dbmsAdaptor = this.dbms();
            this.connectionProvider = connectionProvider;
            this.configuration = configuration;
            final DbmsSqlSyntax<A> syntax = dbmsAdaptor.syntax();
            this.tableMatcher = new ItemMatcher.Implementation<SqlBaseTable>().setEqualator(syntax.getSynchronizationTableEqualator()).setSimilator(syntax.getSynchronizationTableSimilator());
            this.columnMatcher = new ItemMatcher.Implementation<BaseTableColumn.Descriptor>().setEqualator(syntax.getSynchronizationColumnEqualator()).setSimilator(syntax.getSynchronizationColumnSimilator());
        }

        @Override
        public DbmsConnectionProvider<A> connectionProvider() {
            return this.connectionProvider;
        }

        @Override
        public SqlCatalog<?> catalog() {
            return this.connectionProvider.getCatalog();
        }

        @Override
        public SqlContext.Configuration<A> configuration() {
            return this.configuration;
        }

        @Override
        public Integer execute(final SqlStatementExecutingUpdate statement, final XGettingMap<Object, Object> assemblyVariables) {
            SqlStatementExecutingUpdate.Executor executor;
            if ((executor = statement.getExecutor()) == null) {
                executor = this.configuration.getUpdateExecutor();
            }
            return executor.execute(coalesce(statement.getSqlContext(), this), statement, assemblyVariables);
        }

        @Override
        public ResultSet execute(final SqlStatementExecutingQuery statement, final XGettingMap<Object, Object> assemblyVariables) {
            SqlStatementExecutingQuery.Executor executor;
            if ((executor = statement.getExecutor()) == null) {
                executor = this.configuration.getQueryExecutor();
            }
            return executor.execute(coalesce(statement.getSqlContext(), this), statement, assemblyVariables);
        }

        @Override
        public <R> R execute(final SqlStatementExecutingQuerySingleValue<R> statement, final XGettingMap<Object, Object> assemblyVariables) {
            SqlStatementExecutingQuerySingleValue.Executor<R> executor;
            if ((executor = statement.getExecutor()) == null) {
                executor = this.configuration.getSingleValueExecutor(statement.getReturnValueType());
            }
            return executor.execute(coalesce(statement.getSqlContext(), this), statement, assemblyVariables);
        }

        @Override
        public <R> XGettingCollection<R> execute(final SqlStatementExecutingQueryCollection<R> statement, final XGettingMap<Object, Object> assemblyVariables) {
            SqlStatementExecutingQueryCollection.Executor<R> executor;
            if ((executor = statement.getExecutor()) == null) {
                executor = this.configuration.getCollectionExecutor(statement.getReturnValueType());
            }
            return executor.execute(coalesce(statement.getSqlContext(), this), statement, assemblyVariables);
        }

        @Override
        public <R> R execute(final SqlStatementExecutingCall<R> statement, final XGettingMap<Object, Object> assemblyVariables) {
            SqlStatementExecutingCall.Executor<R> executor;
            if ((executor = statement.getExecutor()) == null) {
                executor = this.configuration.getCallExecutor(statement.getReturnType());
            }
            return executor.execute(coalesce(statement.getSqlContext(), this), statement, assemblyVariables);
        }

        @Override
        public Integer execute(final SqlStatementExecutingUpdate statement) {
            return this.execute(statement, null);
        }

        @Override
        public ResultSet execute(final SqlStatementExecutingQuery statement) {
            return this.execute(statement, null);
        }

        @Override
        public <R> R execute(final SqlStatementExecutingQuerySingleValue<R> statement) {
            return this.execute(statement, null);
        }

        @Override
        public <R> XGettingCollection<R> execute(final SqlStatementExecutingQueryCollection<R> statement) {
            return this.execute(statement, null);
        }

        @Override
        public <R> R execute(final SqlStatementExecutingCall<R> statement) {
            return this.execute(statement, null);
        }

        @Override
        public <R> R execute(final SqlPlainString<R> statement) {
            return this.execute(statement, null);
        }

        @Override
        public <R> R execute(final SqlPlainString<R> statement, final XGettingMap<Object, Object> assemblyVariables) {
            SqlExecutor<SqlStatement<R>, ?, R, ?> executor;
            if ((executor = statement.getExecutor()) == null) {
                throw new SqlEngineExecutionException("No executor given.", new NullPointerException());
            }
            return executor.execute(coalesce(statement.getSqlContext(), this), statement, assemblyVariables);
        }

        @Override
        public void close(final Statement statement) {
            if (statement == null) {
                return;
            }
            Connection connection = null;
            try {
                connection = statement.getConnection();
                statement.close();
            } catch (final SQLException e) {
                throw new SqlEngineException(e);
            } finally {
                if (connection != null) {
                    this.connectionProvider.takeBack(connection);
                }
            }
        }

        @Override
        public void close(final ResultSet resultSet) {
            if (resultSet == null) {
                return;
            }
            Statement statement = null;
            try {
                statement = resultSet.getStatement();
                resultSet.close();
            } catch (final SQLException e) {
                throw new SqlEngineException(e);
            } finally {
                if (statement != null) {
                    this.close(statement);
                }
            }
        }

        @Override
        public <R> R executeJdbc(final JdbcExecution<R> jdbcExecution) {
            R result = null;
            final Connection connection = this.connectionProvider.get();
            try {
                return result = jdbcExecution.execute(connection);
            } catch (final SqlEngineExecutionException e) {
                throw e;
            } catch (final Throwable t) {
                throw new SqlEngineExecutionException(t);
            } finally {
                if (!(result instanceof ResultSet)) {
                    this.connectionProvider.takeBack(connection);
                }
            }
        }

        @Override
        public SqlTransaction<A> createTransaction() {
            return new SqlTransaction.Implementation<A>(this.connectionProvider, this.configuration);
        }

        @Override
        public DbmsTableManager<A> tableManager() {
            return this.dbms().tableManager(this);
        }

        @Override
        public Integer create(final SqlDescribedTable table) {
            if (table instanceof SqlBaseTable) {
                return this.create((SqlBaseTable) table);
            } else if (table instanceof SqlViewedTable) {
                return this.create((SqlViewedTable) table);
            }
            notNull(table);
            throw new UnsupportedOperationException("Unhandled table type: " + table);
        }

        @Override
        public Integer create(final SqlBaseTable table) {
            return create(table, false);
        }

        @Override
        public Integer create(final SqlViewedTable view) {
            return SQL.CREATE_VIEW(view).setSqlContext(this.selfRef).execute();
        }

        @Override
        public Integer create(final SqlBaseTable table, final boolean onlyLocalMembers) {
            return SQL.CREATE_TABLE(table).setOnlyLocalMembers(onlyLocalMembers).setSqlContext(this.selfRef).execute();
        }

        @Override
        public Integer drop(final SqlDescribedTable table) {
            if (table instanceof SqlBaseTable) {
                return this.drop((SqlBaseTable) table);
            } else if (table instanceof SqlViewedTable) {
                return this.drop((SqlViewedTable) table);
            }
            notNull(table);
            throw new UnsupportedOperationException("Unhandled table type: " + table);
        }

        @Override
        public Integer drop(final SqlViewedTable view) {
            return SQL.DROP_VIEW(view).setSqlContext(this.selfRef).execute();
        }

        @Override
        public Integer drop(final SqlBaseTable table) {
            return SQL.DROP_TABLE(table).setSqlContext(this.selfRef).execute();
        }

        @Override
        public Integer truncate(final SqlBaseTable table) {
            return SQL.TRUNCATE_TABLE(table).setSqlContext(this.selfRef).execute();
        }

        @Override
        public Object kill(final SqlBaseTable table) {
            return this.dbms().logic().killTable(table, this);
        }

        @Override
        public Object purge(final SqlBaseTable table) {
            return this.dbms().logic().purgeTable(table, this);
        }

        @Override
        public Procedure<SqlBaseTable> getTableDropper() {
            return this.tableDropper;
        }

        @Override
        public Procedure<SqlBaseTable> getTableCreator() {
            return this.tableCreator;
        }

        @Override
        public SqlContext<A> getSqlContext() {
            return this;
        }

        @Override
        public ItemMatcher<SqlBaseTable> getTableMatcher() {
            return this.tableMatcher;
        }

        @Override
        public ItemMatcher<BaseTableColumn.Descriptor> getColumnMatcher() {
            return this.columnMatcher;
        }

        @Override
        public IndexProcedure<SqlBaseTable> getUndefinedExistingTableHandler(final DbmsTableManager<A> tableManager, final ItemMatchResult<SqlBaseTable> tableMatch) {
            return new IndexProcedure<SqlBaseTable>() {

                @Override
                public void apply(final SqlBaseTable table, final int index) {
                    SqlContext.Implementation.this.kill(table);
                }
            };
        }

        @Override
        public IndexProcedure<SqlBaseTable> getDefinedNonExistingTableHandler(final DbmsTableManager<A> tableManager, final ItemMatchResult<SqlBaseTable> tableMatch) {
            return new IndexProcedure<SqlBaseTable>() {

                @Override
                public void apply(final SqlBaseTable table, final int index) {
                    SqlContext.Implementation.this.create(table, true);
                }
            };
        }

        @Override
        public IndexProcedure<SqlBaseTable> getDefinedNonExistingTableFinalizer(final DbmsTableManager<A> tableManager, final ItemMatchResult<SqlBaseTable> tableMatch) {
            return new IndexProcedure<SqlBaseTable>() {

                @Override
                public void apply(final SqlBaseTable table, final int index) {
                    if (!tableManager.dbms().syntax().supportsTableCreationIndexDefinition()) {
                        tableManager.createIndices(table);
                    }
                    tableManager.createForeignKeys(table);
                    tableManager.createCheckConstraints(table);
                }
            };
        }

        @Override
        public IndexProcedure<KeyValue<SqlBaseTable, SqlBaseTable>> getMatchedTableSynchronizer(final DbmsTableManager<A> tableManager, final ItemMatchResult<SqlBaseTable> tableMatch) {
            return new IndexProcedure<KeyValue<SqlBaseTable, SqlBaseTable>>() {

                @Override
                public void apply(final KeyValue<SqlBaseTable, SqlBaseTable> e, final int index) {
                    tableManager.synchronizeTable(e.key().sqlDescriptor(), e.value().sqlDescriptor());
                }
            };
        }

        @Override
        public IndexProcedure<KeyValue<SqlBaseTable, SqlBaseTable>> getMatchedTableFinalizer(final DbmsTableManager<A> tableManager, final ItemMatchResult<SqlBaseTable> tableMatch) {
            return new IndexProcedure<KeyValue<SqlBaseTable, SqlBaseTable>>() {

                @Override
                public void apply(final KeyValue<SqlBaseTable, SqlBaseTable> e, final int index) {
                    tableManager.synchronizeForeignKeys(e.key().sqlDescriptor(), e.value().sqlDescriptor());
                }
            };
        }
    }
}
