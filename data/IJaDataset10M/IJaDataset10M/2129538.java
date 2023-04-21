package de.mguennewig.pobjects.tests;

import junit.framework.TestCase;
import de.mguennewig.pobjects.*;
import de.mguennewig.pobjects.jdbc.JdbcContainer;
import de.mguennewig.pobjects.metadata.Column;
import de.mguennewig.pobjects.metadata.TableExpr;

/** Tests for correct <code>SELECT</code> statement creation.
 *
 * @author Michael G�nnewig
 */
public abstract class BaseSQLContextTestCase extends TestCase {

    public static final String TEST_SCHEMA = "";

    private static final Literal DORTMUND = new Literal("Dortmund");

    private static final Literal ROOM_NR = new Literal("1.28");

    private JdbcContainer db;

    public BaseSQLContextTestCase(final String name) {
        super(name);
        db = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        de.mguennewig.pobjects.Globals.getModule();
        de.mguennewig.pobjects.tests.Globals.getModule();
        db = getContainer();
    }

    protected abstract JdbcContainer getContainer();

    protected final void noInheritsOrJoin() {
        System.err.println("Test `" + getName() + "' skipped as DBMS has no INHERITS and also no JOIN support");
    }

    public final void testAddTableExpr() {
        final EvalContext queryCtx = db.newEvalContext();
        final TableExprContext tblCtx = queryCtx.addTableExpr(Location.cdeclLocation, false);
        final TableRef tr = tblCtx.getTableReference(null);
        assertNotNull(tr);
        assertEquals("fromIndex", 0, tr.getFromIndex());
        assertEquals("tableExpr", Location.cdeclLocation, tr.getTableExpr());
        assertEquals("idField", Location.attrId, tr.getIdField());
    }

    public final void testAddTableExprPassNull() {
        final EvalContext q = db.newEvalContext();
        try {
            q.addTableExpr(null, false);
            fail("addTableExpr accepted null as table expression");
        } catch (IllegalArgumentException e) {
            assertEquals("table==null", e.getMessage());
        }
    }

    public final void testGetNumColumns() {
        final EvalContext q = db.newEvalContext();
        assertEquals("no table added", 0, q.getNumTableExprs());
        q.addTableExpr(Location.cdeclLocation, false);
        assertEquals("first table added", 1, q.getNumTableExprs());
        q.addTableExpr(Location.cdeclLocation, false);
        assertEquals("second table added", 2, q.getNumTableExprs());
    }

    public final void testResolveSelectorTableField() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, false);
        final ColumnReference ref = q.resolveSelector(0, Room.attrName);
        assertNotNull("column reference", ref);
        final TableRef tr = ref.getTable();
        assertNotNull("tableRef", tr);
        assertEquals("column", Room.attrName, ref.getColumn());
        assertEquals("fromIndex", 0, tr.getFromIndex());
        assertEquals("tableExpr", Room.cdeclRoom, tr.getTableExpr());
        assertEquals("idField", Room.attrId, tr.getIdField());
    }

    public final void testResolveSelectorForeignKey() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, false);
        final ColumnReference ref = q.resolveSelector(0, Room.attrLocation, Location.attrId);
        assertNotNull("column reference", ref);
        final TableRef tr = ref.getTable();
        assertNotNull("tableRef", tr);
        assertEquals("column", Location.attrId, ref.getColumn());
        assertEquals("fromIndex", 1, tr.getFromIndex());
        assertEquals("tableExpr", Location.cdeclLocation, tr.getTableExpr());
        assertEquals("idField", Location.attrId, tr.getIdField());
    }

    public final void testResolveSelectorNonExistingTable() {
        final EvalContext q = db.newEvalContext();
        try {
            q.resolveSelector(0, Room.attrName);
            fail("can not resolve column on non-existing table");
        } catch (IllegalArgumentException e) {
            assertEquals("table expression 0 does not exist", e.getMessage());
        }
    }

    public final void testResolveSelectorUsingNonExistingColumn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        try {
            q.resolveSelector(0, Room.attrLocation);
            fail("can not resolve selector for non-existing table column");
        } catch (IllegalArgumentException e) {
            assertEquals("column `location' does not exist in table `Location'", e.getMessage());
        }
    }

    public final void testQueryPullInReferenceOnNotPulledInField() {
        final Query q = db.newQuery();
        q.addTableExpr(Room.cdeclRoom, false);
        try {
            q.pullInReference(new Member(0, Room.attrLocation));
            fail("Pull in reference of not pulled in field accepted");
        } catch (IllegalStateException e) {
            assertEquals("pull in reference of not pulled in field", e.getMessage());
        }
    }

    public final void testAddOrderByNull() {
        final EvalContext q = db.newEvalContext();
        try {
            q.addOrderBy(null, true);
            fail("addOrderBy accepted null as designator");
        } catch (IllegalArgumentException e) {
            assertEquals("designator==null", e.getMessage());
        }
    }

    public final void testAddOrderByWithoutTable() {
        final EvalContext q = db.newEvalContext();
        try {
            q.addOrderBy(new Member(0, Location.attrName), true);
            fail("must not add order by clause for non-existing table");
        } catch (IllegalArgumentException e) {
            assertEquals("table expression 0 does not exist", e.getMessage());
        }
    }

    public final void testAddOrderByUsingNonExistingColumn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        try {
            q.addOrderBy(new Member(0, Room.attrLocation), true);
            fail("must not add order by on non-existing table column");
        } catch (IllegalArgumentException e) {
            assertEquals("column `location' does not exist in table `Location'", e.getMessage());
        }
    }

    public final void testIsDistinctDefault() {
        final EvalContext q = db.newEvalContext();
        assertFalse(q.isDistinct());
    }

    public final void testIsDistinct() {
        final EvalContext q = db.newEvalContext();
        q.setDistinct(true);
        assertTrue(q.isDistinct());
    }

    public final void testGetLimitDefault() {
        final EvalContext q = db.newEvalContext();
        assertTrue(q.getLimit() <= 0);
    }

    public final void testGetOffsetDefault() {
        final EvalContext q = db.newEvalContext();
        assertTrue(q.getOffset() <= 0);
    }

    public final void testSetLimitWithoutOrderBy() {
        final EvalContext q = db.newEvalContext();
        try {
            q.setLimit(1);
            fail("accepted limit without predictable order");
        } catch (IllegalStateException e) {
            assertEquals("LIMIT requires predictable order", e.getMessage());
        }
    }

    public final void testSetLimitWithOrderBy() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addOrderBy(new Member(0, Location.attrName), true);
        q.setLimit(1);
    }

    public final void testSetOffsetWithoutOrderBy() {
        final EvalContext q = db.newEvalContext();
        try {
            q.setOffset(1);
            fail("offset requires predictable order");
        } catch (IllegalStateException e) {
            assertEquals("OFFSET requires predictable order", e.getMessage());
        }
    }

    public final void testSetOffsetWithoutLimit() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addOrderBy(new Member(0, Location.attrName), true);
        try {
            q.setOffset(1);
            fail("offset requires limit");
        } catch (IllegalStateException e) {
            assertEquals("OFFSET requires a LIMIT", e.getMessage());
        }
    }

    public final void testClearLimitWithOffset() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addOrderBy(new Member(0, Location.attrName), true);
        q.setLimit(1);
        q.setOffset(1);
        try {
            q.setLimit(0);
            fail("offset requires limit");
        } catch (IllegalStateException e) {
            assertEquals("OFFSET requires a LIMIT", e.getMessage());
        }
    }

    public final void testAddConjWithoutCondition() {
        final EvalContext q = db.newEvalContext();
        try {
            q.addConj(null);
            fail("must not add null as condition");
        } catch (IllegalArgumentException e) {
            assertEquals("condition==null", e.getMessage());
        }
    }

    public void testToSqlStringAllNoPullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        doTest(q, "SELECT t0.id FROM Location t0");
    }

    public void testToSqlStringAllPullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, true);
        doTest(q, "SELECT t0.id, t0.name FROM Location t0");
    }

    public void testToSqlStringEmptyTable() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Empty.cdeclEmpty, true);
        doTest(q, "SELECT t0.id FROM Empty t0");
    }

    public void testToSqlStringOrderByAscending() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, true);
        q.addOrderBy(new Member(0, Location.attrName), Query.ASCENDING);
        doTest(q, "SELECT t0.id, t0.name FROM Location t0 ORDER BY t0.name ASC");
    }

    public void testToSqlStringOrderByNotPulledInField() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addOrderBy(new Member(0, Location.attrName), Query.ASCENDING);
        doTest(q, "SELECT t0.id FROM Location t0 ORDER BY t0.name ASC");
    }

    public void testToSqlStringOrderByMultipleColumns() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addOrderBy(new Member(0, Location.attrName), Query.ASCENDING);
        q.addOrderBy(new Member(0, Location.attrId), Query.ASCENDING);
        doTest(q, "SELECT t0.id FROM Location t0 ORDER BY t0.name ASC, t0.id ASC");
    }

    public void testToSqlStringOrderByMultipleColumnsPullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, true);
        q.addOrderBy(new Member(0, Location.attrName), Query.ASCENDING);
        q.addOrderBy(new Member(0, Location.attrId), Query.ASCENDING);
        doTest(q, "SELECT t0.id, t0.name FROM Location t0 ORDER BY t0.name ASC, t0.id ASC");
    }

    public void testToSqlStringDistinctBeforeTable() {
        final EvalContext q = db.newEvalContext();
        q.setDistinct(true);
        q.addTableExpr(Location.cdeclLocation, true);
        doTest(q, "SELECT DISTINCT t0.id FROM Location t0");
    }

    public void testToSqlStringDistinctAfterTable() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.setDistinct(true);
        doTest(q, "SELECT DISTINCT t0.id FROM Location t0");
    }

    public void testToSqlStringDistinctAfterTablePullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, true);
        q.setDistinct(true);
        doTest(q, "SELECT DISTINCT t0.id, t0.name FROM Location t0");
    }

    public void testToSqlStringDistinctOrderBy() {
        final EvalContext q = db.newEvalContext();
        q.setDistinct(true);
        q.addTableExpr(Location.cdeclLocation, false);
        q.addOrderBy(new Member(0, Location.attrName), true);
        doTest(q, "SELECT DISTINCT t0.id, t0.name FROM Location t0 ORDER BY t0.name ASC");
    }

    public void testToSqlStringLiteralEscape() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addConj(Predicate.equals(new Member(0, Location.attrName), new Literal("Test '_?%\\'")));
        doTest(q, "SELECT t0.id FROM Location t0 WHERE (t0.name='Test ''_?%\\''')");
    }

    public void testToSqlStringLikeEscape() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addConj(Predicate.like(new Member(0, Location.attrName), new Literal("Test '_?%\\'")));
        doTest(q, "SELECT t0.id FROM Location t0 WHERE (t0.name LIKE 'Test ''_?%\\''' ESCAPE '\\')");
    }

    public void testToSqlStringLiteralBooleanTrue() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(TypeTests.cdeclTypeTests, false);
        q.addConj(Predicate.equals(new Member(0, TypeTests.attrBool), new Literal(Boolean.TRUE)));
        final String sql = q.toSqlString(false);
        if (db.supportsBoolean()) {
            assertEquals("SELECT t0.id FROM type_tests t0 WHERE (t0.bool=true)", sql);
        } else {
            assertEquals("SELECT t0.id FROM type_tests t0 WHERE (t0.bool='t')", sql);
        }
    }

    public void testToSqlStringLiteralBooleanFalse() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(TypeTests.cdeclTypeTests, false);
        q.addConj(Predicate.equals(new Member(0, TypeTests.attrBool), new Literal(Boolean.FALSE)));
        final String sql = q.toSqlString(false);
        if (db.supportsBoolean()) {
            assertEquals("SELECT t0.id FROM type_tests t0 WHERE (t0.bool=false)", sql);
        } else {
            assertEquals("SELECT t0.id FROM type_tests t0 WHERE (t0.bool='f')", sql);
        }
    }

    public void testToSqlStringConditionNot() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, false);
        q.addConj(Predicate.not(Predicate.equals(new Member(0, Room.attrName), ROOM_NR)));
        doTest(q, "SELECT t0.id FROM Room t0 WHERE (NOT (t0.name='1.28'))");
    }

    public void testToSqlStringConditionNotPullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, true);
        q.addConj(Predicate.not(Predicate.equals(new Member(0, Room.attrName), ROOM_NR)));
        doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0 WHERE (NOT (t0.name='1.28'))");
    }

    public void testToSqlStringEquiJoin() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, false);
        q.addTableExpr(Room.cdeclRoom, false);
        q.addConj(Predicate.equals(new Member(0, Location.attrId), new Member(1, Room.attrLocation)));
        doTest(q, "SELECT t0.id, t1.id FROM Location t0, Room t1 WHERE (t0.id=t1.location)");
    }

    public void testToSqlStringEquiJoinPullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, true);
        q.addTableExpr(Room.cdeclRoom, true);
        q.addConj(Predicate.equals(new Member(0, Location.attrId), new Member(1, Room.attrLocation)));
        doTest(q, "SELECT t0.id, t0.name, t1.id, t1.location, t1.name FROM Location t0, Room t1 WHERE (t0.id=t1.location)");
    }

    public void testToSqlStringConditionPullInReferenceOuterJoin() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, false);
        q.addConj(Predicate.equals(new Member(0, new Column[] { Room.attrLocation, Location.attrName }), DORTMUND));
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id FROM Room t0 LEFT OUTER JOIN Location t1 ON " + "t0.location=t1.id WHERE (t1.name='Dortmund')");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id FROM Room t0, Location t1 WHERE " + "(t0.location=t1.id(+)) AND (t1.name='Dortmund')");
        } else {
            doTest(q, "SELECT t0.id FROM Room t0, Location t1 WHERE " + "(t0.location=t1.id) AND (t1.name='Dortmund')");
        }
    }

    public void testToSqlStringConditionPullInReferenceOuterJoinPullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, true);
        q.addConj(Predicate.equals(new Member(0, new Column[] { Room.attrLocation, Location.attrName }), DORTMUND));
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0 LEFT OUTER " + "JOIN Location t1 ON t0.location=t1.id WHERE (t1.name='Dortmund')");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0, Location t1" + " WHERE (t0.location=t1.id(+)) AND (t1.name='Dortmund')");
        } else {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0, Location t1" + " WHERE (t0.location=t1.id) AND (t1.name='Dortmund')");
        }
    }

    public void testToSqlStringConditionOrPullInReference() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, false);
        Predicate p0 = Predicate.equals(new Member(0, Room.attrName), ROOM_NR);
        Predicate p1 = Predicate.equals(new Member(0, new Column[] { Room.attrLocation, Location.attrName }), DORTMUND);
        q.addConj(Predicate.or(p0, p1));
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id FROM Room t0 LEFT OUTER JOIN Location t1 ON " + "t0.location=t1.id WHERE ((t0.name='1.28') OR (t1.name='Dortmund'))");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id FROM Room t0, Location t1 WHERE ((t0.name='1.28')" + " OR ((t0.location=t1.id(+)) AND (t1.name='Dortmund')))");
        } else {
            doTest(q, "SELECT t0.id FROM Room t0, Location t1 WHERE ((t0.name='1.28')" + " OR ((t0.location=t1.id) AND (t1.name='Dortmund')))");
        }
    }

    public void testToSqlStringConditionOrPullInReferencePullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, true);
        Predicate p0 = Predicate.equals(new Member(0, Room.attrName), ROOM_NR);
        Predicate p1 = Predicate.equals(new Member(0, new Column[] { Room.attrLocation, Location.attrName }), DORTMUND);
        q.addConj(Predicate.or(p0, p1));
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0 LEFT OUTER " + "JOIN Location t1 ON t0.location=t1.id WHERE ((t0.name='1.28') OR (t1.name='Dortmund'))");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0, Location t1" + " WHERE ((t0.name='1.28') OR ((t0.location=t1.id(+)) AND (t1.name='Dortmund')))");
        } else {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0, Location t1" + " WHERE ((t0.name='1.28') OR ((t0.location=t1.id) AND (t1.name='Dortmund')))");
        }
    }

    public void testToSqlStringConditionNotPullInReferenceOuterJoin() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, false);
        Predicate p0 = Predicate.equals(new Member(0, new Column[] { Room.attrLocation, Location.attrName }), DORTMUND);
        q.addConj(Predicate.not(p0));
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id FROM Room t0 LEFT OUTER JOIN Location t1 ON " + "t0.location=t1.id WHERE (NOT (t1.name='Dortmund'))");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id FROM Room t0, Location t1 WHERE (NOT " + "((t0.location=t1.id(+)) AND (t1.name='Dortmund')))");
        } else {
            doTest(q, "SELECT t0.id FROM Room t0, Location t1 WHERE " + "(t0.location=t1.id) AND (NOT (t1.name='Dortmund'))");
        }
    }

    public void testToSqlStringConditionNotPullInReferenceOuterJoinPullIn() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, true);
        Predicate p0 = Predicate.equals(new Member(0, new Column[] { Room.attrLocation, Location.attrName }), DORTMUND);
        q.addConj(Predicate.not(p0));
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0 LEFT OUTER " + "JOIN Location t1 ON t0.location=t1.id WHERE (NOT (t1.name='Dortmund'))");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0, Location t1" + " WHERE (NOT ((t0.location=t1.id(+)) AND (t1.name='Dortmund')))");
        } else {
            doTest(q, "SELECT t0.id, t0.location, t0.name FROM Room t0, Location t1" + " WHERE (t0.location=t1.id) AND (NOT (t1.name='Dortmund'))");
        }
    }

    public void testToSqlStringLeftJoin() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, true);
        q.addTableExpr(Room.cdeclRoom, false);
        q.addConj(Predicate.equals(new Member(0, Location.attrId), new Member(1, Room.attrLocation, true)));
        if (db.supportsSQL99Join()) {
            doTest(q, ("SELECT t0.id, t0.name, t1.id FROM Location t0" + " LEFT OUTER JOIN Room t1 ON (t0.id=t1.location)"));
        } else if (db.supportsOracleJoin()) {
            doTest(q, ("SELECT t0.id, t0.name, t1.id FROM Location t0, Room t1" + " WHERE (t0.id=t1.location(+))"));
        } else System.err.println(getName() + " skipped as DBMS does not support join");
    }

    public void testToSqlStringLeftJoinWithCondition() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Location.cdeclLocation, true);
        q.addTableExpr(Room.cdeclRoom, false);
        q.addConj(Predicate.equals(new Member(0, Location.attrId), new Member(1, Room.attrLocation, true)));
        q.addConj(Predicate.equals(new Member(0, Location.attrName), DORTMUND));
        q.addConj(Predicate.or(Predicate.equals(new Member(1, Room.attrName), ROOM_NR), Predicate.isNull(new Member(1, Room.attrName))));
        if (db.supportsSQL99Join()) {
            doTest(q, ("SELECT t0.id, t0.name, t1.id FROM Location t0" + " LEFT OUTER JOIN Room t1 ON (t0.id=t1.location) WHERE" + " (t0.name='Dortmund') AND ((t1.name='1.28') OR (t1.name IS NULL))"));
        } else if (db.supportsOracleJoin()) {
            doTest(q, ("SELECT t0.id, t0.name, t1.id FROM Location t0, Room t1" + " WHERE (t0.id=t1.location(+)) AND (t0.name='Dortmund') AND" + " ((t1.name='1.28') OR (t1.name IS NULL))"));
        } else System.err.println(getName() + " skipped as DBMS does not support join");
    }

    public void testToSqlStringSelfReference1() {
        final Column[] columns = new Column[] { SelfRef.attrSelfRef, SelfRef.attrSelfRef };
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(SelfRef.cdeclSelfRef, true);
        q.addConj(Predicate.isNull(new Member(0, columns)));
        doTest(q, "SELECT t0.id, t0.self_ref FROM self_ref t0, self_ref t1 WHERE (t0.self_ref=t1.id) AND (t1.self_ref IS NULL)");
    }

    public void testToSqlStringSelfReference2() {
        final Column[] columns3 = new Column[] { SelfRef.attrSelfRef, SelfRef.attrSelfRef, SelfRef.attrId };
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(SelfRef.cdeclSelfRef, false);
        q.addConj(Predicate.equals(new Member(0, columns3), new Literal(Integer.valueOf(0))));
        q.addConj(Predicate.equals(new Member(0, SelfRef.attrSelfRef, SelfRef.attrId), new Literal(Integer.valueOf(1))));
        q.addConj(Predicate.equals(new Member(0, SelfRef.attrId), new Literal(Integer.valueOf(2))));
        doTest(q, "SELECT t0.id FROM self_ref t0, self_ref t1, self_ref t2 " + "WHERE (t0.self_ref=t1.id) AND (t1.self_ref=t2.id) AND " + "(t2.id=0) AND (t1.id=1) AND (t0.id=2)");
    }

    public void testToSqlStringPullInReference() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, true);
        q.pullInReference(0, Room.attrLocation);
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name, t1.id, t1.name FROM Room t0" + " LEFT OUTER JOIN Location t1 ON (t0.location=t1.id)");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id, t0.location, t0.name, t1.id, t1.name FROM Room " + "t0, Location t1 WHERE (t0.location=t1.id(+))");
        } else {
            doTest(q, "SELECT t0.id, t0.location, t0.name, t1.id, t1.name FROM Room " + "t0, Location t1 WHERE (t0.location=t1.id)");
        }
    }

    public void testToSqlStringPullInReferenceNested() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(SelfRef.cdeclSelfRef, true);
        q.pullInReference(0, SelfRef.attrSelfRef);
        q.pullInReference(0, SelfRef.attrSelfRef, SelfRef.attrSelfRef);
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id, t0.self_ref, t1.id, t1.self_ref, t3.id, " + "t3.self_ref FROM self_ref t0 LEFT OUTER JOIN self_ref t1 ON " + "(t0.self_ref=t1.id), self_ref t2 LEFT OUTER JOIN self_ref t3 ON" + " (t2.self_ref=t3.id) WHERE (t0.self_ref=t2.id)");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id, t0.self_ref, t1.id, t1.self_ref, t3.id, " + "t3.self_ref FROM self_ref t0, self_ref t1, self_ref t2, " + "self_ref t3 WHERE (t0.self_ref=t1.id(+)) AND (t0.self_ref=t2.id)" + " AND (t2.self_ref=t3.id(+))");
        } else {
            noInheritsOrJoin();
        }
    }

    public void testToSqlStringPullInReferenceDistinct() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Room.cdeclRoom, true);
        q.pullInReference(0, Room.attrLocation);
        q.setDistinct(true);
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT DISTINCT t0.id, t0.location, t0.name, t1.id, t1.name " + "FROM Room t0 LEFT OUTER JOIN Location t1 ON (t0.location=t1.id)");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT DISTINCT t0.id, t0.location, t0.name, t1.id, t1.name " + "FROM Room t0, Location t1 WHERE (t0.location=t1.id(+))");
        } else {
            doTest(q, "SELECT DISTINCT t0.id, t0.location, t0.name, t1.id, t1.name " + "FROM Room t0, Location t1 WHERE (t0.location=t1.id)");
        }
    }

    public void testToSqlStringMultiplePullInReference() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(C.cdeclC, true);
        q.pullInReference(0, C.attrA);
        q.pullInReference(0, C.attrB);
        if (db.supportsSQL99Join()) {
            doTest(q, "SELECT t0.id, t0.x, t0.a, t0.b, t1.id, t1.x, t1.alias_for, " + "t2.id, t2.x FROM C t0 LEFT OUTER JOIN A t1 ON (t0.a=t1.id) LEFT" + " OUTER JOIN B t2 ON (t0.b=t2.id)");
        } else if (db.supportsOracleJoin()) {
            doTest(q, "SELECT t0.id, t0.x, t0.a, t0.b, t1.id, t1.x, t1.alias_for, " + "t2.id, t2.x FROM C t0, A t1, B t2 WHERE (t0.a=t1.id(+)) AND " + "(t0.b=t2.id(+))");
        } else {
            noInheritsOrJoin();
        }
    }

    public void testQueryExtensible1() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level0.cdeclLevel0, false);
        if (!db.supportsInherits()) doTest(q, "SELECT t0.id, t0.pclass_ FROM Level0 t0"); else doTest(q, "SELECT t0.id FROM Level0 t0");
    }

    public void testQueryExtensible2() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level1.cdeclLevel1, false);
        if (!db.supportsInherits()) {
            doTest(q, "SELECT t0.id, t1.pclass_ FROM Level1 t0, Level0 t1 WHERE (t0.id=t1.id)");
        } else doTest(q, "SELECT t0.id FROM Level1 t0");
    }

    public void testQueryExtensible3() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level2.cdeclLevel2, false);
        if (!db.supportsInherits()) {
            doTest(q, "SELECT t0.id, t1.pclass_ FROM Level2 t0, Level0 t1 WHERE (t0.id=t1.id)");
        } else doTest(q, "SELECT t0.id FROM Level2 t0");
    }

    public void testQueryExtensible4() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level3.cdeclLevel3, false);
        if (!db.supportsInherits()) {
            doTest(q, ("SELECT t0.id, t1.pclass_" + " FROM Level2 t0, Level0 t1" + " WHERE (t0.id=t1.id) AND t1.pclass_=" + db.mapClassDeclToPClassId(Level3.cdeclLevel3)));
        } else doTest(q, "SELECT t0.id FROM Level3 t0");
    }

    public void testQueryExtensible5() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level0.cdeclLevel0, true);
        if (!db.supportsInherits()) {
            if (db.supportsSQL99Join()) {
                doTest(q, ("SELECT t0.id, t0.pclass_, t0.a, t1.id, t1.b, t2.id, t2.c" + " FROM Level0 t0 LEFT OUTER JOIN Level1 t1 ON t0.id=t1.id" + " LEFT OUTER JOIN Level2 t2 ON t0.id=t2.id"));
            } else if (db.supportsOracleJoin()) {
                doTest(q, ("SELECT t0.id, t0.pclass_, t0.a, t1.id, t1.b, t2.id, t2.c" + " FROM Level0 t0, Level1 t1, Level2 t2" + " WHERE (t0.id=t1.id(+)) AND (t0.id=t2.id(+))"));
            } else noInheritsOrJoin();
        } else doTest(q, "SELECT t0.id, t0.a FROM Level0 t0");
    }

    public void testQueryExtensible6() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level1.cdeclLevel1, true);
        if (!db.supportsInherits()) {
            if (db.supportsSQL99Join()) {
                doTest(q, ("SELECT t0.id, t1.pclass_, t1.a, t0.b, t2.id, t2.c" + " FROM Level0 t1," + " Level1 t0 LEFT OUTER JOIN Level2 t2 ON t0.id=t2.id" + " WHERE (t0.id=t1.id)"));
            } else if (db.supportsOracleJoin()) {
                doTest(q, ("SELECT t0.id, t1.pclass_, t1.a, t0.b, t2.id, t2.c" + " FROM Level1 t0, Level0 t1, Level2 t2" + " WHERE (t0.id=t1.id) AND (t0.id=t2.id(+))"));
            } else noInheritsOrJoin();
        } else doTest(q, "SELECT t0.id, t0.a, t0.b FROM Level1 t0");
    }

    public void testQueryExtensible7() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level2.cdeclLevel2, true);
        if (!db.supportsInherits()) {
            if (db.supportsSQL99Join()) {
                doTest(q, "SELECT t0.id, t1.pclass_, t1.a, t2.b, t0.c FROM Level2 t0, Level0 t1, Level1 t2 WHERE (t0.id=t1.id) AND (t0.id=t2.id)");
            } else if (db.supportsOracleJoin()) {
                doTest(q, ("SELECT t0.id, t1.pclass_, t1.a, t2.b, t0.c" + " FROM Level2 t0, Level0 t1, Level1 t2" + " WHERE (t0.id=t1.id) AND (t0.id=t2.id)"));
            } else noInheritsOrJoin();
        } else doTest(q, "SELECT t0.id, t0.a, t0.b, t0.c FROM Level2 t0");
    }

    public void testQueryExtensible8() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(RefLevel.cdeclRefLevel, true);
        if (!db.supportsInherits()) {
            if (db.supportsSQL99Join()) {
                doTest(q, "SELECT t0.id, t0.ref0, t1.pclass_, t0.ref1, t3.pclass_, " + "t0.ref2, t5.pclass_ FROM ref_level t0, Level0 t1, Level1 t2 " + "LEFT OUTER JOIN Level0 t3 ON t2.id=t3.id, Level2 t4 LEFT " + "OUTER JOIN Level0 t5 ON t4.id=t5.id WHERE (t0.ref0=t1.id) AND" + " (t0.ref1=t2.id) AND (t0.ref2=t4.id)");
            } else if (db.supportsOracleJoin()) {
                doTest(q, "SELECT t0.id, t0.ref0, t1.pclass_, t0.ref1, t3.pclass_, " + "t0.ref2, t5.pclass_ FROM ref_level t0, Level0 t1, Level1 t2, " + "Level0 t3, Level2 t4, Level0 t5 WHERE (t0.ref0=t1.id) AND " + "(t0.ref1=t2.id) AND (t2.id=t3.id(+)) AND (t0.ref2=t4.id) AND" + " (t4.id=t5.id(+))");
            } else noInheritsOrJoin();
        } else doTest(q, "SELECT t0.id, t0.ref0, t0.ref1, t0.ref2 FROM ref_level t0");
    }

    public void testQueryExtensible9() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level0.cdeclLevel0, false);
        q.addTableExpr(RefLevel.cdeclRefLevel, false);
        q.addConj(Predicate.equals(new Member(0, Level0.attrId), new Member(1, RefLevel.attrRef0)));
        if (!db.supportsInherits()) {
            doTest(q, "SELECT t0.id, t0.pclass_, t1.id FROM Level0 t0, ref_level t1 WHERE (t0.id=t1.ref0)");
        } else doTest(q, "SELECT t0.id, t1.id FROM Level0 t0, ref_level t1 WHERE (t0.id=t1.ref0)");
    }

    public void testQueryExtensible10() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level0.cdeclLevel0, true);
        q.addTableExpr(RefLevel.cdeclRefLevel, false);
        q.addConj(Predicate.equals(new Member(0, Level0.attrId), new Member(1, RefLevel.attrRef0)));
        if (!db.supportsInherits()) {
            if (db.supportsSQL99Join()) {
                doTest(q, "SELECT t0.id, t0.pclass_, t0.a, t1.id, t1.b, t2.id, t2.c, t3.id FROM Level0 t0 LEFT OUTER JOIN Level1 t1 ON t0.id=t1.id LEFT OUTER JOIN Level2 t2 ON t0.id=t2.id, ref_level t3 WHERE (t0.id=t3.ref0)");
            } else if (db.supportsOracleJoin()) {
                doTest(q, "SELECT t0.id, t0.pclass_, t0.a, t1.id, t1.b, t2.id, t2.c, t3.id FROM Level0 t0, Level1 t1, Level2 t2, ref_level t3 WHERE (t0.id=t1.id(+)) AND (t0.id=t2.id(+)) AND (t0.id=t3.ref0)");
            } else noInheritsOrJoin();
        } else doTest(q, "SELECT t0.id, t0.a, t1.id FROM Level0 t0, ref_level t1 WHERE (t0.id=t1.ref0)");
    }

    public void testQueryExtensible11() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(Level1.cdeclLevel1, true);
        q.addTableExpr(RefLevel.cdeclRefLevel, false);
        q.addConj(Predicate.equals(new Member(0, Level1.attrId), new Member(1, RefLevel.attrRef1)));
        if (!db.supportsInherits()) {
            if (db.supportsSQL99Join()) {
                doTest(q, "SELECT t0.id, t1.pclass_, t1.a, t0.b, t2.id, t2.c, t3.id FROM Level0 t1, Level1 t0 LEFT OUTER JOIN Level2 t2 ON t0.id=t2.id, ref_level t3 WHERE (t0.id=t1.id) AND (t0.id=t3.ref1)");
            } else if (db.supportsOracleJoin()) {
                doTest(q, "SELECT t0.id, t1.pclass_, t1.a, t0.b, t2.id, t2.c, t3.id FROM Level1 t0, Level0 t1, Level2 t2, ref_level t3 WHERE (t0.id=t1.id) AND (t0.id=t2.id(+)) AND (t0.id=t3.ref1)");
            } else noInheritsOrJoin();
        } else doTest(q, "SELECT t0.id, t0.a, t0.b, t1.id FROM Level1 t0, ref_level t1 WHERE (t0.id=t1.ref1)");
    }

    public void testToSqlStringPullInReferenceExtensible() {
        final EvalContext q = db.newEvalContext();
        q.addTableExpr(RefLevel.cdeclRefLevel, true);
        q.pullInReference(0, RefLevel.attrRef1);
        if (!db.supportsInherits()) {
            if (db.supportsSQL99Join()) {
                doTest(q, "SELECT t0.id, t0.ref0, t1.pclass_, t0.ref1, t3.pclass_, " + "t0.ref2, t5.pclass_, t6.id, t7.pclass_, t7.a, t6.b, t8.id, " + "t8.c FROM ref_level t0, Level0 t1, Level1 t2 LEFT OUTER JOIN " + "Level0 t3 ON t2.id=t3.id, Level2 t4 LEFT OUTER JOIN Level0 t5" + " ON t4.id=t5.id, Level0 t7, Level1 t6 LEFT OUTER JOIN Level2 " + "t8 ON t6.id=t8.id WHERE (t0.ref0=t1.id) AND (t0.ref1=t2.id) " + "AND (t0.ref2=t4.id) AND (t6.id=t7.id) AND (t0.ref1=t6.id)");
            } else if (db.supportsOracleJoin()) {
                doTest(q, "SELECT t0.id, t0.ref0, t1.pclass_, t0.ref1, t3.pclass_, " + "t0.ref2, t5.pclass_, t6.id, t7.pclass_, t7.a, t6.b, t8.id, " + "t8.c FROM ref_level t0, Level0 t1, Level1 t2, Level0 t3, " + "Level2 t4, Level0 t5, Level1 t6, Level0 t7, Level2 t8 WHERE " + "(t0.ref0=t1.id) AND (t0.ref1=t2.id) AND (t2.id=t3.id(+)) AND " + "(t0.ref2=t4.id) AND (t4.id=t5.id(+)) AND (t6.id=t7.id) AND " + "(t6.id=t8.id(+)) AND (t0.ref1=t6.id)");
            } else {
                doTest(q, "SELECT t0.id, t0.ref0, t1.pclass_, t0.ref1, t3.pclass_, " + "t0.ref2, t5.pclass_, t6.id, t7.pclass_, t7.a, t6.b, t8.id, " + "t8.c FROM ref_level t0, Level0 t1, Level1 t2, Level0 t3, " + "Level2 t4, Level0 t5, Level1 t6, Level0 t7, Level2 t8 WHERE " + "(t0.ref0=t1.id) AND (t0.ref1=t2.id) AND (t2.id=t3.id) AND " + "(t0.ref2=t4.id) AND (t4.id=t5.id) AND (t6.id=t7.id) AND " + "(t6.id=t8.id) AND (t0.ref1=t6.id)");
            }
        } else {
            doTest(q, "SELECT t0.id, t0.ref0, t0.ref1, t0.ref2, t1.a, t2.a, t2.b," + " t2.a, t2.b FROM ref_level t0, Level0 t1, Level1 t2, Level2 t3" + " WHERE (t0.ref0=t1.id) AND (t0.ref1=t2.id) AND (t0.ref2=t3.id)");
        }
    }

    public void testSelectExprToSql() {
        final String sql = CountLocation.sdeclCountLocation.getSqlFromClause(TableExpr.SQL_ARG_QUESTION_MARK);
        assertEquals("(SELECT COUNT(*) AS number FROM Location)", sql);
    }

    public void testSelectExprToSqlWithForml() {
        final String sql = RoomQuery.sdeclRoomQuery.getSqlFromClause(TableExpr.SQL_ARG_QUESTION_MARK);
        assertEquals("(SELECT location AS loc, name, substring(name,1,position('.' in name)) AS level, substring(name,position('.' in name),length(name)) AS room_number FROM Room WHERE name LIKE ? || '.%')", sql);
    }

    public void testToSqlStringSelectNoPullIn() {
        try {
            final EvalContext q = db.newEvalContext();
            q.addTableExpr(CountLocation.sdeclCountLocation, false);
            doTest(q, "SELECT t0.number FROM (SELECT COUNT(*) AS number FROM Location) t0");
        } catch (UnsupportedOperationException e) {
            assertEquals("Select and View expressions are not supported", e.getMessage());
        }
    }

    public void testToSqlStringSelectPullIn() {
        try {
            final EvalContext q = db.newEvalContext();
            q.addTableExpr(CountLocation.sdeclCountLocation, true);
            doTest(q, "SELECT t0.number FROM (SELECT COUNT(*) AS number FROM Location) t0");
        } catch (UnsupportedOperationException e) {
            assertEquals("Select and View expressions are not supported", e.getMessage());
        }
    }

    public void testToSqlStringSelectExtensible() {
        try {
            final EvalContext q = db.newEvalContext();
            q.addTableExpr(RefQuery.sdeclRefQuery, true);
            doTest(q, "SELECT t0.ref0, t1.pclass_ FROM (SELECT ref0 FROM ref_level) t0, Level0 t1 WHERE (t0.ref0=t1.id)");
        } catch (UnsupportedOperationException e) {
            assertEquals("Select and View expressions are not supported", e.getMessage());
        }
    }

    public void testToSqlStringSelectWithForm() {
        try {
            final EvalContext q = db.newEvalContext();
            q.addTableExpr(RoomQuery.sdeclRoomQuery, true);
            doTest(q, "SELECT t0.loc, t0.name, t0.level, t0.room_number FROM (SELECT " + "location AS loc, name, substring(name,1,position('.' in name))" + " AS level, substring(name,position('.' in name),length(name)) " + "AS room_number FROM Room WHERE name LIKE ? || '.%') t0");
        } catch (UnsupportedOperationException e) {
            assertEquals("Select and View expressions are not supported", e.getMessage());
        }
    }

    protected void doTest(final EvalContext q, final String expectedResult) {
        System.err.println("test  : " + getName());
        try {
            final String sql = q.toSqlString(false);
            if (!expectedResult.equals(sql)) {
                System.err.println("expect: <<" + expectedResult + ">>");
                System.err.println("got   : <<" + sql + ">>");
            }
            assertEquals(expectedResult, sql);
        } catch (UnsupportedOperationException e) {
            System.err.println("Test `" + getName() + "' skipped due to unsupported feature: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
