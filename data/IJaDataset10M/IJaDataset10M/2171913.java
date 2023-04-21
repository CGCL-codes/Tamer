package de.mguennewig.pobjects;

import java.util.Iterator;
import de.mguennewig.pobjects.metadata.*;

/** Interface for database connections.
 *
 * @author Michael G�nnewig
 */
public interface Container {

    /**
   * Capability flag, which states that
   * <code>[LEFT | RIGHT | FULL] OUTER JOIN</code> in the <code>FROM</code>
   * part are supported.
   *
   * <p>This join variation will be preferred if also the Oracle version is
   * supported.</p>
   */
    int SUPPORTS_SQL99_JOIN = 1;

    /**
   * Capability flag, which states that for outer joins a <code>(+)</code>
   * as used by Oracle must be appended to the expression in the
   * <code>WHERE</code> clause, which should be used for the join.
   *
   * @see #SUPPORTS_ORACLE_JOIN
   * @see "Oracle Documentation"
   */
    int SUPPORTS_ORACLE_JOIN = 2;

    /**
   * Capability flag, which states that both outer join syntaxes are
   * supported.
   *
   * @see #SUPPORTS_SQL99_JOIN
   * @see #SUPPORTS_ORACLE_JOIN
   */
    int SUPPORTS_JOIN = (SUPPORTS_SQL99_JOIN | SUPPORTS_ORACLE_JOIN);

    /**
   * The keywords <code>LIMIT</code> and <code>OFFSET</code> are supported to
   * limit result set of the query.
   *
   * @see "PostgreSQL Documentation"
   */
    int SUPPORTS_LIMIT_OFFSET = 4;

    /**
   * The <code>INHERITS</code> concept is supported by the RDBMS as well as
   * supported and used by the <code>Container</code>.
   *
   * <p>If this flag is set, then the RDBMS has to support queries as well as
   * data manipulation statements to affect also the inherited classes.</p>
   *
   * <p>If this flag is not set, then a special column <code>pclass_</code> is
   * added to the base class, which will specify the instance type.  The rest
   * has then to be done by the <code>Container</code>, {@link Query} and
   * {@link EvalContext}.</p>
   *
   * @see "PostgreSQL Documentation"
   */
    int SUPPORTS_INHERITS = 8;

    /** The database supports the data type <code>BOOLEAN</code> natively. */
    int SUPPORTS_BOOLEAN = 16;

    /** Use a stream/reader to insert large objects into the database.
   *
   * <p>The JDBC drivers of Oracle expect to get their own
   * {@link java.sql.Blob} and {@link java.sql.Clob} implementations for calls
   * to the {@link java.sql.PreparedStatement} API.  If they get anything else
   * you just run into a {@link ClassCastException}.</p>
   */
    int USE_STREAM_TO_INSERT_LOB = 32;

    /** Use an array of bytes/characters for large objects.
   *
   * <p>Most JDBC drivers for SQLite do not support LOBs, but arrays of bytes
   * or characters.</p>
   */
    int USE_ARRAY_FOR_LOB = 64;

    /** Returns the capabilities of the DBMS.
   *
   * @see "The <code>SUPPORTS_*</code> constants."
   */
    int getCapabilities();

    /** Tests whether the database supports booleans natively.
   *
   * @see #SUPPORTS_BOOLEAN
   */
    boolean supportsBoolean();

    /** Tests whether the database supports the inheritance concept natively.
   *
   * @see #SUPPORTS_INHERITS
   */
    boolean supportsInherits();

    /** Tests whether the database supports limiting the result set.
   *
   * @see #SUPPORTS_LIMIT_OFFSET
   */
    boolean supportsLimitOffset();

    /** Tests whether the database supports Oracle style joins.
   *
   * @see #SUPPORTS_ORACLE_JOIN
   */
    boolean supportsOracleJoin();

    /** Tests whether the database supports SQL99 style joins.
   *
   * @see #SUPPORTS_SQL99_JOIN
   */
    boolean supportsSQL99Join();

    /**
   * Tests whether the database requires to use an {@link java.io.InputStream}
   * or {@link java.io.Reader} instead of a large object.
   *
   * @see #USE_STREAM_TO_INSERT_LOB
   */
    boolean useStreamToInsertLob();

    /**
   * Tests whether the database requires to use an {@link byte[]} or
   * {@link char[]} instead of a large object.
   *
   * @see #USE_ARRAY_FOR_LOB
   */
    boolean useArrayForLob();

    /** Returns the used meta data dictionary. */
    PObjDictionary getDictionary();

    /**
   * Returns the database schema prefix that is used to access the SQL
   * entities.
   *
   * @see #getQualifiedName(SqlEntity)
   */
    String getSchema();

    /** Returns the qualified name for the given SQL entity.
   *
   * @see Module#getSchemaPrefix()
   * @see #getSchema()
   */
    String getQualifiedName(SqlEntity entity);

    /**
   * @throws IllegalArgumentException if {@code tableClass} is {@code null}.
   */
    TableExpr getTableExpr(Class<? extends Record> tableClass);

    /**
   * Returns the meta data definition for the table with the given schema
   * name.
   *
   * @see #getClassDecls()
   */
    ClassDecl getClassDecl(String schemaName);

    /**
   * Returns an {@link Iterator} over all known
   * {@link ClassDecl class declarations}.
   */
    Iterator<ClassDecl> getClassDecls();

    /**
   * Maps the given <em>pclass</em> id as used by the DBMS to the correct
   * {@link ClassDecl}.
   *
   * @return <code>null</code> if the <code>id</code> is unknown.
   * @throws UnsupportedOperationException if DB supports INHERITS.
   * @see #mapClassDeclToPClassId(ClassDecl)
   */
    ClassDecl mapPClassIdToClassDecl(String id);

    /**
   * Maps the given {@link ClassDecl} to the <em>pclass</em> id used in the
   * DBMS.
   *
   * @return <code>null</code> if the <code>table</code> is unknown.
   * @throws UnsupportedOperationException if DB supports INHERITS.
   * @see #mapPClassIdToClassDecl(String)
   */
    String mapClassDeclToPClassId(ClassDecl table);

    /**
   * Tests whether modified but unstored persistent objects will be denied upon
   * a {@link #commitTransaction() commit}.
   *
   * @return <code>true</code> if there will be raised an exception for an
   *   modified but unstored object upon a commit, otherwise {@code false}.
   */
    boolean isStrictMode();

    /**
   * Sets whether modified but unstored persistent objects will be denied upon
   * a {@link #commitTransaction() commit}.
   *
   * @see #isStrictMode()
   * @see #commitTransaction()
   */
    void setStrictMode(boolean strictMode);

    /** Starts a new transaction.
   *
   * @throws IllegalStateException if still a transaction is not finished with
   *   either {@link #commitTransaction} or {@link #rollbackTransaction}.
   * @throws PObjSQLException if the DBMS is unable to start a new transaction.
   */
    void beginTransaction() throws PObjSQLException;

    /** Commits all statements since the last {@link #beginTransaction}.
   *
   * <p>If {@link #isStrictMode()} yields <code>false</code> then just a
   * warning for each dirty object is written, otherwise an exception is
   * raised.</p>
   *
   * @throws PObjSQLException if the DBMS in unable to finish the current
   *   transaction by committing it or if an modified but unstored object is
   *   detected while in {@link #isStrictMode() strict mode}.
   */
    void commitTransaction() throws PObjSQLException;

    /** Rolls back all statements since the last {@link #beginTransaction}.
   *
   * <p>The in-memory data will be reverted also as far as possible.</p>
   *
   * @throws PObjSQLException if the DBMS is unable to stop the current
   *   transaction by rolling back all made changes within it.
   * @see PObject#revert
   */
    void rollbackTransaction() throws PObjSQLException;

    /** Resets the container.
   *
   * <p>Use this method to forget all changes made to any object.</p>
   *
   * <p><b>NOTE:</b> Any running transaction will be rolled back.</p>
   *
   * @see #rollbackTransaction()
   */
    void reset();

    void close() throws PObjSQLException;

    /** Returns the current value of the given sequence.
   *
   * @throws PObjSQLException if any database error occurs.
   * @throws UnsupportedOperationException if database does not support
   *   sequences.
   */
    String getSequenceCurrentValue(Sequence seq) throws PObjSQLException;

    /** Returns a new value of the given sequence.
   *
   * @throws PObjSQLException if any database error occurs.
   * @throws UnsupportedOperationException if database does not support
   *   sequences.
   */
    String getSequenceNextValue(Sequence seq) throws PObjSQLException;

    /**
   * Creates a new {@link Record} instance for the given
   * {@link TableExpr table expression} without any binding to this container.
   *
   * @throws ExceptionInInitializerError if the initialization provoked by this
   *   method fails.
   * @throws SecurityException if a security manager denies this operation.
   * @see #getObject(ClassDecl,String)
   * @see #getData(TableExpr)
   * @see Class#newInstance()
   */
    Record createObject(TableExpr te);

    /**
   * Returns a {@link Record} instance for the given
   * {@link TableExpr table expression} without reading any data from the DB.
   *
   * @throws IllegalArgumentException if <code>te</code> is a
   *   {@link ClassDecl class declaration} with an {@link IdField id field}.
   * @see #getObject(ClassDecl,String)
   * @see TableExpr#isWritableClass()
   */
    Record getData(TableExpr te);

    boolean insertRecord(Record obj) throws PObjConstraintException, PObjSQLException;

    boolean deleteRecord(Record obj) throws PObjConstraintException, PObjSQLException;

    /**
   * Returns a {@link PObject} instance for the given
   * {@link TableExpr table expression} without reading any data from the DB.
   *
   * @throws IllegalArgumentException if <code>te</code> is a
   *   {@link ClassDecl class declaration} without an {@link IdField id field}.
   * @see #getData(TableExpr)
   * @see #retrieveData(PObject)
   */
    PObject getObject(ClassDecl te, String id);

    /**
   * Returns a {@link PObject} instance for the given table class without
   * reading any data from the DB.
   *
   * @throws IllegalArgumentException if <code>tableClass</code> is not a
   *   {@link PObject} instance or its {@link ClassDecl class declaration} has
   *   no {@link IdField id field}.
   * @see #getTableExpr(Class)
   * @see #getObject(ClassDecl,String)
   */
    PObject getObject(Class<? extends PObject> tableClass, String id);

    /** Inserts the given object into the database.
   *
   * @throws PObjConstraintException if a database constraint will be violated.
   * @throws PObjSQLException if any other database error occurs.
   */
    String insertObject(PObject obj) throws PObjConstraintException, PObjSQLException;

    /** Updates the given persistent object in the database.
   *
   * @throws PObjConstraintException if a database constraint will be violated.
   * @throws PObjSQLException if any other database error occurs.
   * @see PObject#isPersistent()
   */
    void updateObject(PObject obj) throws PObjConstraintException, PObjSQLException;

    /** Deletes the given persistent object from the database.
   *
   * @throws PObjConstraintException if a database constraint will be violated.
   * @throws PObjSQLException if any other database error occurs.
   * @see PObject#isPersistent()
   */
    void deleteObject(PObject obj) throws PObjConstraintException, PObjSQLException;

    /** Creates the connection between the object and the database container.
   *
   * <p>This method tells the object the database container and the container
   * that this object is a newly created one that might be added to the DBMS.
   * </p>
   *
   * @throws IllegalStateException if the object is already in any container,
   *   or any reference within points to an object which isn't persistent or in
   *   another container.
   * @see ForeignKeyConstraint
   */
    void makePersistent(PObject obj);

    /** Notifies this container that the object has been changed.
   *
   * @see PObject#set(Column,Object)
   * @see PObject#revert()
   */
    void notifyChange(PObject obj, boolean reverted);

    /** Stores all changed objects belonging to this container.
   *
   * <p>The current implementation of this method may fail for some kinds of
   * cyclical data references involving delete and update operations, as no
   * reordering will be done currently.  The order of the made changes to the
   * objects will be used, so that the object which got changed first will be
   * the first stored.</p>
   *
   * @throws PObjConstraintException
   * @throws PObjSQLException
   * @see PObject#store()
   */
    void storeAll() throws PObjConstraintException, PObjSQLException;

    /** Deletes all entries within the specified table.
   *
   * <p><b>NOTE:</b> The objects which represent a row of this table or refer
   * to rows within it aren't updated.  So the best way to ensure consistency
   * is to finish the current active transaction before calling this method and
   * afterwards doing a {@link #reset}.</p>
   *
   * @throws IllegalArgumentException if <code>te</code> is <code>null</code>.
   * @throws IllegalStateException if no connection to a DBMS exists.
   * @throws PObjConstraintException if a database constraint will be violated.
   * @throws PObjSQLException if any other database error occurs.
   */
    int deleteAll(ClassDecl te) throws PObjSQLException, PObjConstraintException;

    /** Deletes all entries within the specified table.
   *
   * <p><b>NOTE:</b> The objects which represent a row of this table or refer
   * to rows within it aren't updated.  So the best way to ensure consistency
   * is to finish the current active transaction before calling this method and
   * afterwards doing a {@link #reset}.</p>
   *
   * @throws IllegalArgumentException if <code>tableClass</code> does not
   *   represent a database table.
   * @throws IllegalStateException if no connection to a DBMS exists.
   * @throws PObjConstraintException if a database constraint will be violated.
   * @throws PObjSQLException if any other database error occurs.
   * @see #deleteAll(ClassDecl)
   * @see #getTableExpr(Class)
   */
    int deleteAll(Class<? extends Record> tableClass) throws PObjSQLException, PObjConstraintException;

    /** Deletes all entries within the specified table whose foreign key
   * <code>fk</code> equals the given value.
   *
   * <p><b>NOTE:</b> The objects which represent a row of this table or refer
   * to rows within it aren't updated.  So the best way to ensure consistency
   * is to finish the current active transaction before calling this method and
   * afterwards doing a {@link #reset}.</p>
   *
   * @throws IllegalArgumentException if <code>cl</code> is <code>null</code>.
   * @throws IllegalStateException if no connection to a DBMS exists.
   * @throws PObjConstraintException if a database constraint will be violated.
   * @throws PObjSQLException if any other database error occurs.
   */
    int deleteAll(ClassDecl cl, Column fk, Object value) throws PObjSQLException, PObjConstraintException;

    /** Retrieves the data for the given persistent object from the database.
   *
   * @throws PObjReadError if any database error occurs during reading.
   */
    void retrieveData(PObject obj);

    /** Executes a single command on the database.
   *
   * @param command The command declaration describing the command to be
   *   executed.
   * @param arguments Optional set of arguments that will be passed into the
   *   variables of the command.
   * @throws IllegalArgumentException if the command requires a different
   *   amount of arguments then passed.
   * @throws PObjConstraintException if any constraint would be violated by the
   *   execution.
   * @throws PObjSQLException if any other database error occurs.
   * @throws UnsupportedOperationException if execution of commands is not
   *   supported by the container.
   */
    int executeCommand(CommandDecl command, Object... arguments) throws PObjConstraintException, PObjSQLException;

    EvalContext newEvalContext();

    /** Creates a new empty query. */
    Query newQuery();

    /** Creates a new query for all rows of the given table expression. */
    Query newQuery(TableExpr tableExpr);

    /** Creates a new query with a filter on the given table expression. */
    Query newQuery(TableExpr tableExpr, Predicate filter);

    /** Creates a new query with a filter on the given table expression. */
    Query newQuery(TableExpr tableExpr, Predicate[] filters);

    /** Creates a new query with a filter on the given table expressions. */
    Query newQuery(TableExpr[] tableExprs, Predicate[] filters);

    /** Creates a new query for all rows of the given table expression.
   *
   * @see #getTableExpr(Class)
   * @see #newQuery(TableExpr)
   */
    Query newQuery(Class<? extends Record> tableClass);

    /** Creates a new query with a filter on the given table expression.
   *
   * @see #getTableExpr(Class)
   * @see #newQuery(TableExpr,Predicate)
   */
    Query newQuery(Class<? extends Record> tableClass, Predicate filter);

    /** Creates a new query with a filter on the given table expression.
   *
   * @see #getTableExpr(Class)
   * @see #newQuery(TableExpr,Predicate[])
   */
    Query newQuery(Class<? extends Record> tableClass, Predicate[] filters);

    /** Creates a new query with a filter on the given table expression.
   *
   * @see #getTableExpr(Class)
   * @see #newQuery(TableExpr[],Predicate[])
   */
    Query newQuery(Class<? extends Record>[] tableClasses, Predicate[] filters);
}
