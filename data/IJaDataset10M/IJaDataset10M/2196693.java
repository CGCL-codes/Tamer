package de.mguennewig.pobjects;

import de.mguennewig.pobjects.metadata.*;

public interface EvalContext {

    /**
   * The field type of the implicit added type table reference if required by
   * the DBMS.
   */
    public static final RefType atypePclass = new RefType("pobjects", "Pclass", Pclass.cdeclPclass);

    /**
   * The field declaration of the implicit added type table reference if
   * required by the DBMS.
   *
   * <p>This field will only be added to the
   * {@link ClassDecl#getBottomClass() most specific base class}.</p>
   */
    public static final Column attrPclass = new Field("pclass_", Pclass.cdeclPclass.getSchemaName(), atypePclass, false, false, "pclass", "pclass");

    TableExprContext addTableExpr(TableExpr te, boolean pullInData);

    TableExprContext getTableExpr(int index);

    int getNumTableExprs();

    void addConj(Condition condition);

    void addOrderBy(Member designator, boolean ascending);

    /** Returns whether the result will be sorted.
   *
   * @see #addOrderBy(Member, boolean)
   */
    boolean isOrdered();

    /** Returns whether this query evaluation context will filter out duplicates.
   *
   * <p>There are some limitations with this feature, which are described under
   * {@link #setDistinct(boolean)}.</p>
   */
    boolean isDistinct();

    /** Sets whether this query evaluation context should filter out duplicates.
   *
   * <p>Even if the query is marked as distinct, it can still return
   * duplicates, due to the nature of created query and how {@code DISTINCT}
   * works in {@code SQL}.  To get a real distinct result the following things
   * have to be met:</p>
   * <ol>
   *   <li>
   *     the parameter <code>pullInFields</code> of the
   *     <code>addTableExpr</code> methods must be <code>false</code> for every
   *     table.  Note that for {@link SelectExpr} and {@link ViewDecl} this
   *     parameter is always <code>true</code>, so that the distinctness
   *     depends on the distinctness of these expressions.
   *   </li>
   *   <li>{@link #pullInReference(int,Column[])} has not to be used.</li>
   * </ol>
   *
   * <p>Note that if invoked with {@code true} then the {@code pullInFields}
   * parameter of {@code addTableExpr} will be ignored for further invocations,
   * until invoked with {@code false} again.<br />
   * For example:</p>
   * <pre>
       q.addTableExpr(Room.class, true);
       q.setDistinct(true);
       q.addTableExpr(Location.class, true);
     </pre>
   * results in the following SQL statement:
   * <pre>
       SELECT DISTINCT t0.id, t0.location, t0.name, t1.id
       FROM Room t0, Location t1
     </pre>
   *
   * @see #addTableExpr(TableExpr, boolean)
   * @see #pullInReference(int, Column[])
   */
    void setDistinct(boolean distinct);

    /** Returns the current result set limit.
   *
   * <p>A limit of a result set can be used together with an
   * {@link #getOffset() offset} to implement paging.</p>
   *
   * <p>A limit of {@code 1} can be used for existence tests.</p>
   *
   * @return A value of zero or less specifies no limit, otherwise the number
   *   of rows to be returned.
   */
    int getLimit();

    /** Sets an upper limit of rows returned by the result set.
   *
   * @throws IllegalStateException if an offset is to be set but no limit is
   *   set or no predictable order is specified.
   * @see #addOrderBy(Member, boolean)
   * @see #getOffset()
   */
    void setLimit(int limit);

    /** Returns the current offset within the result set.
   *
   * @return A value of zero or less specifies no offset, otherwise the number
   *   of rows to be skipped.
   * @see #getLimit()
   */
    int getOffset();

    /** Sets the number of results that should be skipped for the result set.
   *
   * <p>A negative or zero offset value disables the skipping of results.</p>
   *
   * <p><b>NOTE:</b> If set to a value greater zero you must also
   * {@link #setLimit(int) specify a limit}.</p>
   *
   * @throws IllegalStateException if an offset is to be set but no limit is
   *   set.
   * @see #getLimit()
   */
    void setOffset(int offset);

    /**
   *
   * <p><b>NOTE</b> this has the side-effect of adding further tables to the
   * {@code FROM} clause if required.</p>
   *
   * @throws IllegalArgumentException if any selector does not belong to the
   *   referenced table
   * @see Member
   */
    void pullInReference(int baseTable, Column... selector);

    /**
   *
   * <p><b>NOTE</b> this has the side-effect of adding further tables to the
   * {@code FROM} clause if required.</p>
   *
   * @throws IllegalArgumentException if any selector does not belong to the
   *   referenced table
   * @see Member
   */
    ColumnReference resolveSelector(int baseColumn, Column... selector);

    String toSqlString(boolean forCount);

    /** Tests whether joins after SQL99 are supported.
   *
   * @see Container#SUPPORTS_SQL99_JOIN
   */
    boolean supportsSQL99Join();

    /** Tests whether the Oracle join style is supported.
   *
   * @see Container#SUPPORTS_ORACLE_JOIN
   */
    boolean supportsOracleJoin();
}
