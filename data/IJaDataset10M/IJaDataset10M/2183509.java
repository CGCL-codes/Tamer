package tests.sql;

import dalvik.annotation.KnownFailure;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.Map;
import junit.framework.Test;

@TestTargetClass(Connection.class)
public class ConnectionTest extends SQLTest {

    /**
     * @test {@link java.sql.Connection#createStatement()}
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "createStatement", args = {  })
    public void testCreateStatement() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            assertNotNull(statement);
            assertEquals(ResultSet.FETCH_UNKNOWN, statement.getFetchDirection());
            assertNull(statement.getWarnings());
            assertTrue(statement.getQueryTimeout() > 0);
        } catch (SQLException sqle) {
            fail("SQL Exception was thrown: " + sqle.getMessage());
        } catch (Exception e) {
            fail("Unexpected Exception " + e.getMessage());
        }
        try {
            conn.close();
            statement.executeQuery("select * from zoo");
            fail("SQLException is not thrown after close");
        } catch (SQLException e) {
        }
    }

    /**
     * @test {@link java.sql.Connection#createStatement(int resultSetType, int
     *       resultSetConcurrency)} 
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Exception tests fail.", method = "createStatement", args = { int.class, int.class })
    @KnownFailure("Scrolling on a forward only RS not allowed. conn.close() does not wrap up")
    public void testCreateStatement_int_int() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            st.execute("select id, name from zoo");
            rs = st.getResultSet();
            try {
                rs.deleteRow();
                fail("Could delete row for READ_ONLY ResultSet");
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (Exception ee) {
            }
        }
        try {
            st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            st.execute("select id, name from zoo");
            rs = st.getResultSet();
            try {
                rs.absolute(1);
                rs.previous();
                fail("Could scroll backwards");
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (Exception ee) {
            }
        }
        try {
            st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            st.execute("select id, name from zoo");
            rs = st.getResultSet();
            try {
                rs.last();
                rs.first();
                fail("Could scroll backwards");
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (Exception ee) {
            }
        }
        try {
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            st.execute("select name, family from zoo");
            rs = st.getResultSet();
            try {
                rs.insertRow();
                rs.updateObject("family", "bird");
                rs.next();
                rs.previous();
                assertEquals("parrot", (rs.getString(1)));
                fail("SQLException was not thrown");
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (Exception ee) {
            }
        }
        try {
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            st.execute("select name, family from zoo");
            rs = st.getResultSet();
            try {
                rs.insertRow();
                rs.updateObject("family", "bird");
                rs.next();
                rs.previous();
                assertEquals("bird", (rs.getString(1)));
                fail("SQLException was not thrown");
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (Exception ee) {
            }
        }
        conn.close();
        try {
            conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, -1);
            fail("Illigal arguments: should return exception.");
        } catch (SQLException sqle) {
        }
        try {
            conn.createStatement(Integer.MIN_VALUE, ResultSet.CONCUR_READ_ONLY);
            fail("Illigal arguments: should return exception.");
        } catch (SQLException sqle) {
        }
    }

    /**
     * @test java.sql.Connection#createStatement(int resultSetType, int
     *       resultSetConcurrency, int resultSetHoldability)
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "ResultSet.HOLD_CURSORS_AT_COMMIT", method = "createStatement", args = { int.class, int.class, int.class })
    public void testCreateStatement_int_int_int() {
        Statement st = null;
        try {
            assertNotNull(conn);
            st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            assertNotNull(st);
            st.execute("select id, name from zoo");
            ResultSet rs = st.getResultSet();
            rs.next();
            int pos = rs.getRow();
            conn.commit();
            assertEquals("ResultSet cursor position has changed", pos, rs.getRow());
            try {
                rs.close();
            } catch (SQLException sqle) {
                fail("Unexpected exception was thrown during closing ResultSet");
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, -100);
            fail("SQLException was not thrown");
        } catch (SQLException sqle) {
        }
    }

    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "ResultSet.CLOSE_CURSORS_AT_COMMIT as argument is not supported", method = "createStatement", args = { int.class, int.class, int.class })
    @KnownFailure("not supported")
    public void testCreateStatementIntIntIntNotSupported() {
        Statement st = null;
        try {
            st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            assertNotNull(st);
            st.execute("select id, name from zoo");
            ResultSet rs = st.getResultSet();
            try {
                rs.close();
                fail("SQLException was not thrown");
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ee) {
                }
            }
        }
    }

    /**
     * @test java.sql.Connection#getMetaData()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "SQLException test fails", method = "getMetaData", args = {  })
    @KnownFailure("conn.close() does not wrap up")
    public void testGetMetaData() throws SQLException {
        try {
            DatabaseMetaData md = conn.getMetaData();
            Connection con = md.getConnection();
            assertEquals(conn, con);
        } catch (SQLException e) {
            fail("SQLException is thrown");
        }
        conn.close();
        try {
            conn.getMetaData();
            fail("Exception expected");
        } catch (SQLException e) {
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#clearWarnings()
     * 
     * TODO clearWarnings is not supported
     */
    @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "test fails. not supported. always returns null.", method = "clearWarnings", args = {  })
    @KnownFailure("not supported")
    public void testClearWarnings() throws SQLException {
        try {
            SQLWarning w = conn.getWarnings();
            assertNull(w);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.execute("select animals from zoo");
            fail("SQLException was not thrown");
        } catch (SQLException e) {
            assertNotNull(conn.getWarnings());
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.clearWarnings();
            SQLWarning w = conn.getWarnings();
            assertNull(w);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
        try {
            st = conn.createStatement();
            st.execute("select monkey from zoo");
            fail("SQLException was not thrown");
        } catch (SQLException e) {
            assertEquals("SQLite.Exception: error in prepare/compile", e.getMessage());
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            SQLWarning w = conn.getWarnings();
            assertNotNull(w);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
        conn.close();
        try {
            conn.clearWarnings();
            fail("Exception expected");
        } catch (SQLException e) {
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#getWarnings()
     * 
     * TODO GetWarnings is not supported: returns null
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported. always returns null. SQLException test fails", method = "getWarnings", args = {  })
    @KnownFailure("not supported")
    public void testGetWarnings() throws SQLException {
        Statement st = null;
        int errorCode1 = -1;
        int errorCode2 = -1;
        try {
            st = conn.createStatement();
            st.execute("select animals from zoooo");
            fail("SQLException was not thrown");
        } catch (SQLException e) {
            errorCode1 = e.getErrorCode();
        }
        try {
            SQLWarning wrs = conn.getWarnings();
            assertNull(wrs);
        } catch (Exception e) {
            fail("Change test implementation: get warnings is supported now");
        }
        conn.close();
        try {
            conn.getWarnings();
            fail("Exception expected");
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#getAutoCommit()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "SQLException checking missed", method = "getAutoCommit", args = {  })
    public void testGetAutoCommit() {
        try {
            conn.setAutoCommit(true);
            assertTrue(conn.getAutoCommit());
            conn.setAutoCommit(false);
            assertFalse(conn.getAutoCommit());
            conn.setAutoCommit(true);
            assertTrue(conn.getAutoCommit());
        } catch (SQLException e) {
            fail("SQLException is thrown: " + e.getMessage());
        }
    }

    /**
     * @test java.sql.Connection#setAutoCommit(boolean)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "SQLException test throws exception", method = "setAutoCommit", args = { boolean.class })
    @KnownFailure("conn.close() does not wrap up")
    public void testSetAutoCommit() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            conn.setAutoCommit(true);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (3, 'Chichichi', 'monkey');");
            conn.commit();
        } catch (SQLException e) {
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            st = conn.createStatement();
            st.execute("select * from zoo");
            rs = st.getResultSet();
            assertEquals(3, getCount(rs));
        } catch (SQLException e) {
            fail("Unexpected Exception thrown");
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (4, 'Burenka', 'cow');");
            st.execute("select * from zoo");
            rs = st.getResultSet();
            assertEquals(4, getCount(rs));
            conn.commit();
            rs1 = st.getResultSet();
            assertEquals(0, getCount(rs1));
        } catch (SQLException e) {
            fail("SQLException is thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                rs1.close();
                st.close();
            } catch (SQLException ee) {
            }
        }
        conn.close();
        try {
            conn.setAutoCommit(true);
            fail("Exception expected");
        } catch (SQLException e) {
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#isReadOnly()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Instead of SQLException nullpointer exception is thrown.", method = "isReadOnly", args = {  })
    @KnownFailure("conn.close() does not wrap up")
    public void testIsReadOnly() throws SQLException {
        try {
            conn.setReadOnly(true);
            assertTrue(conn.isReadOnly());
            conn.setReadOnly(false);
            assertFalse(conn.isReadOnly());
        } catch (SQLException sqle) {
            fail("SQLException was thrown: " + sqle.getMessage());
        }
        conn.close();
        try {
            conn.isReadOnly();
            fail("Exception expected");
        } catch (SQLException e) {
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#setReadOnly(boolean)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Not supported. test fails", method = "setReadOnly", args = { boolean.class })
    @KnownFailure("not supported")
    public void testSetReadOnly() throws SQLException {
        Statement st = null;
        try {
            conn.setReadOnly(true);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (3, 'ChiChiChi', 'monkey');");
        } catch (SQLException sqle) {
            fail("Set readonly is actually implemented: activate correct test");
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        st = null;
        try {
            conn.setReadOnly(true);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (3, 'ChiChiChi', 'monkey');");
            fail("SQLException is not thrown");
        } catch (SQLException sqle) {
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.setReadOnly(true);
            st = conn.createStatement();
            st.executeUpdate("insert into zoo (id, name, family) values (4, 'ChaChaCha', 'monkey');");
            fail("SQLException is not thrown");
        } catch (SQLException sqle) {
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.setReadOnly(false);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (4, 'ChiChiChi', 'monkey');");
        } catch (SQLException sqle) {
            fail("SQLException was thrown: " + sqle.getMessage());
        } finally {
            try {
                st.close();
            } catch (SQLException ee) {
            }
        }
        conn.close();
        try {
            conn.setReadOnly(true);
            fail("Exception expected");
        } catch (SQLException e) {
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#getHoldability()
     * 
     * TODO ResultSet.CLOSE_CURSORS_AT_COMMIT is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "+option ResultSet.CLOSE_CURSORS_AT_COMMIT not supported. SQLException test fails.", method = "getHoldability", args = {  })
    @KnownFailure("not supported")
    public void testGetHoldability() throws SQLException {
        try {
            conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
            assertEquals(ResultSet.HOLD_CURSORS_OVER_COMMIT, conn.getHoldability());
        } catch (SQLException sqle) {
            fail("SQLException was thrown: " + sqle.getMessage());
        }
        try {
            conn.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
            assertEquals(ResultSet.CLOSE_CURSORS_AT_COMMIT, conn.getHoldability());
        } catch (SQLException e) {
            assertEquals("not supported", e.getMessage());
        }
        conn.close();
        try {
            conn.getHoldability();
            fail("Could execute statement on closed connection.");
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#setHoldability(int)
     * 
     * TODO ResultSet.CLOSE_CURSORS_AT_COMMIT is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "ResultSet.CLOSE_CURSORS_AT_COMMIT is not supported", method = "setHoldability", args = { int.class })
    @KnownFailure("not supported")
    public void testSetHoldability() {
        Statement st = null;
        try {
            conn.setAutoCommit(false);
            conn.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
            assertEquals(ResultSet.CLOSE_CURSORS_AT_COMMIT, conn.getHoldability());
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (4, 'ChiChiChi', 'monkey');");
            ResultSet rs = st.getResultSet();
            conn.commit();
            try {
                rs.next();
            } catch (SQLException sqle) {
            }
            conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
            assertEquals(ResultSet.HOLD_CURSORS_OVER_COMMIT, conn.getHoldability());
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (4, 'ChiChiChi', 'monkey');");
            rs = st.getResultSet();
            conn.commit();
            try {
                rs.next();
            } catch (SQLException sqle) {
                fail("SQLException was thrown: " + sqle.getMessage());
            }
        } catch (SQLException sqle) {
            fail("SQLException was thrown: " + sqle.getMessage());
        } finally {
            try {
                st.close();
            } catch (Exception ee) {
            }
        }
        try {
            conn.setHoldability(-1);
            fail("SQLException is not thrown");
        } catch (SQLException sqle) {
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#getTransactionIsolation()
     * 
     * TODO only Connection.TRANSACTION_SERIALIZABLE is supported
     */
    @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "SQLException testing throws exception. Connection.TRANSACTION_SERIALIZABLE.", method = "getTransactionIsolation", args = {  })
    @KnownFailure("not supported")
    public void testGetTransactionIsolation() throws SQLException {
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            assertEquals(Connection.TRANSACTION_READ_UNCOMMITTED, conn.getTransactionIsolation());
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertEquals(Connection.TRANSACTION_READ_COMMITTED, conn.getTransactionIsolation());
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            assertEquals(Connection.TRANSACTION_REPEATABLE_READ, conn.getTransactionIsolation());
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            assertEquals(Connection.TRANSACTION_SERIALIZABLE, conn.getTransactionIsolation());
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        }
        conn.close();
        try {
            conn.getTransactionIsolation();
            fail("Could execute statement on closed connection.");
        } catch (SQLException e) {
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#getTransactionIsolation()
     * 
     * TODO only Connection.TRANSACTION_SERIALIZABLE is supported
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "not supported options", method = "getTransactionIsolation", args = {  })
    public void testGetTransactionIsolationNotSupported() throws SQLException {
    }

    /**
     * @test java.sql.Connection#setTransactionIsolation(int)
     * 
     * TODO only Connection.TRANSACTION_SERIALIZABLE is supported
     */
    @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "not fully supported", method = "setTransactionIsolation", args = { int.class })
    public void testSetTransactionIsolation() {
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            assertEquals(Connection.TRANSACTION_SERIALIZABLE, conn.getTransactionIsolation());
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        }
        try {
            conn.setTransactionIsolation(0);
            fail("SQLException is not thrown");
        } catch (SQLException sqle) {
        }
    }

    /**
     * @test java.sql.Connection#setCatalog(String catalog)
     * 
     * TODO setCatalog method does nothing: Hint default catalog sqlite_master.
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "setCatalog", args = { java.lang.String.class })
    public void testSetCatalog() {
        String[] catalogs = { "test", "test1", "test" };
        Statement st = null;
        try {
            for (int i = 0; i < catalogs.length; i++) {
                conn.setCatalog(catalogs[i]);
                assertNull(catalogs[i], conn.getCatalog());
                st = conn.createStatement();
                st.equals("create table test_table (id integer not null, name varchar(20), primary key(id));");
                st.equals("drop table test_table;");
            }
        } catch (SQLException sqle) {
            fail("SQLException is thrown");
        } finally {
            try {
                st.close();
            } catch (Exception ee) {
            }
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#getCatalog()
     * 
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported. test fails", method = "getCatalog", args = {  })
    @KnownFailure("not supported")
    public void testGetCatalog() throws SQLException {
        try {
            assertEquals("sqlite_master", conn.getCatalog());
        } catch (SQLException sqle) {
            fail("SQL Exception " + sqle.getMessage());
        } catch (Exception e) {
            fail("Unexpected Exception " + e.getMessage());
        }
        String[] catalogs = { "sqlite_test", "sqlite_test1", "sqlite_test" };
        Statement st = null;
        try {
            for (int i = 0; i < catalogs.length; i++) {
                conn.setCatalog(catalogs[i]);
                assertNull(conn.getCatalog());
            }
        } catch (SQLException sqle) {
            fail("SQL Exception " + sqle.getMessage());
        } catch (Exception e) {
            fail("Reeimplement tests now that the method is implemented");
        }
        conn.close();
        try {
            conn.getCatalog();
            fail("Could execute statement on closed connection.");
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#setTypeMap(Map<String,Class<?>> map)
     * 
     * TODO setTypeMap is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "setTypeMap", args = { java.util.Map.class })
    public void testSetTypeMap() {
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#getTypeMap()
     * 
     * TODO getTypeMap is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "getTypeMap", args = {  })
    public void testGetTypeMap() throws SQLException {
    }

    /**
     * @test java.sql.Connection#nativeSQL(String sql)
     * 
     * TODO nativeSQL is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "nativeSQL", args = { java.lang.String.class })
    public void testNativeSQL() throws SQLException {
        String[] queries = { "select * from zoo;", "insert into zoo (id, name, family) values (3, 'Chichichi', 'monkey');", "create table zoo_office(id integer not null, name varchar(20), primary key(id));", "drop table zoo_office;" };
        String[] native_queries = { "select * from zoo;", "insert into zoo (id, name, family) values (3, 'Chichichi', 'monkey');", "create table zoo_office(id integer not null, name varchar(20), primary key(id));", "drop table zoo_office;" };
        Statement st = null;
        String nativeQuery = "";
        try {
            for (int i = 0; i < queries.length; i++) {
                nativeQuery = conn.nativeSQL(queries[i]);
                assertEquals(native_queries[i], nativeQuery);
                st = conn.createStatement();
                st.execute(nativeQuery);
            }
        } catch (SQLException sqle) {
        } catch (Exception e) {
            fail("Unexpected Exception " + e.getMessage());
        } finally {
            try {
                st.close();
            } catch (Exception ee) {
            }
        }
        String[] inc_queries = { "", "  ", "not query" };
        for (int i = 0; i < inc_queries.length; i++) {
            try {
                nativeQuery = conn.nativeSQL(inc_queries[i]);
                assertEquals(inc_queries[i], nativeQuery);
            } catch (SQLException e) {
                assertEquals("not supported", e.getMessage());
            }
        }
        conn.close();
        try {
            conn.nativeSQL(inc_queries[0]);
            fail("Could execute statement on closed connection.");
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#prepareCall(String sql)
     * 
     * TODO prepareCall is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "prepareCall", args = { java.lang.String.class })
    public void testPrepareCall() throws SQLException {
        CallableStatement cstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement st = null;
        Statement st1 = null;
        try {
            cstmt = conn.prepareCall("call welcomeAnimal(3, 'Petya', 'Cock')");
            st = conn.createStatement();
            st.execute("select * from zoo");
            rs = st.getResultSet();
            assertEquals(2, getCount(rs));
            cstmt.execute();
            st1 = conn.createStatement();
            st1.execute("select * from zoo");
            rs1 = st1.getResultSet();
            assertEquals(3, getCount(rs1));
        } catch (SQLException e) {
        } finally {
            try {
                st.close();
                st1.close();
                rs.close();
                rs1.close();
                cstmt.close();
            } catch (Exception ee) {
            }
        }
        try {
            conn.prepareCall("welcomeAnimal(4, 'Petya', 'Cock')");
            fail("SQL Exception is not thrown");
        } catch (SQLException e) {
        }
        try {
            conn.prepareCall(null);
            fail("SQL Exception is not thrown");
        } catch (SQLException e) {
        }
        conn.close();
        try {
            conn.prepareCall("");
            fail("Could execute statement on closed connection.");
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#prepareCall(String sql, int resultSetType, int
     *       resultSetConcurrency)
     *       
     * TODO prepareCall is not supported      
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "prepareCall", args = { java.lang.String.class, int.class, int.class })
    public void testPrepareCall_String_int_int() {
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            String query = "call welcomeAnimal(3, 'Petya', 'Cock')";
            cstmt = conn.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#prepareCall(String sql, int resultSetType, int
     *       resultSetConcurrency, int resultSetHoldability)
     *       
     * TODO prepareCall is not supported     
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "prepareCall", args = { java.lang.String.class, int.class, int.class, int.class })
    public void testPrepareCall_String_int_int_int() {
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            String query = "call welcomeAnimal(?, ?, ?)";
            cstmt = conn.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#prepareStatement(String sql) 
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "prepareStatement", args = { java.lang.String.class })
    public void testPrepareStatement() {
        PreparedStatement prst = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            String update = "update zoo set family = ? where name = ?;";
            prst = conn.prepareStatement(update);
            prst.setString(1, "cat");
            prst.setString(2, "Yasha");
            st = conn.createStatement();
            st.execute("select * from zoo where family = 'cat'");
            rs = st.getResultSet();
            assertEquals(0, getCount(rs));
            prst.executeUpdate();
            st.execute("select * from zoo where family = 'cat'");
            rs1 = st.getResultSet();
            assertEquals(1, getCount(rs1));
        } catch (SQLException e) {
            fail("SQLException is thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                rs1.close();
                prst.close();
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            prst = conn.prepareStatement("");
            prst.execute();
            fail("SQLException is not thrown");
        } catch (SQLException e) {
        }
        try {
            conn.prepareStatement(null);
            fail("SQLException is not thrown");
        } catch (Exception e) {
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Statment.Return_generated_keys/getGeneratedKeys is not supported", method = "prepareStatement", args = { java.lang.String.class, int.class })
    @KnownFailure("not supported")
    public void testPrepareStatement_String_int() {
        PreparedStatement prst = null;
        PreparedStatement prst1 = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        try {
            String insert = "insert into zoo (id, name, family) values (?, ?, ?);";
            prst = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            fail("Fail: prepareStatement does not fail");
        } catch (SQLException e) {
        }
        try {
            String insert = "insert into zoo (id, name, family) values (?, ?, ?);";
            prst = conn.prepareStatement(insert, Statement.NO_GENERATED_KEYS);
            prst.setInt(1, 8);
            prst.setString(2, "Tuzik");
            prst.setString(3, "dog");
            st = conn.createStatement();
            st.execute("select * from zoo");
            rs = st.getResultSet();
            assertEquals(2, getCount(rs));
            prst.execute();
            st.execute("select * from zoo where family = 'dog'");
            rs1 = st.getResultSet();
            assertEquals(1, getCount(rs1));
            rs4 = prst.getGeneratedKeys();
            assertEquals(0, getCount(rs4));
            prst1 = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            prst1.setInt(1, 5);
            prst1.setString(2, "Layka");
            prst1.setString(3, "dog");
            prst1.execute();
            rs5 = prst1.getGeneratedKeys();
            assertEquals(0, getCount(rs5));
        } catch (SQLException e) {
            fail("SQLException is thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                rs1.close();
                prst.close();
                st.close();
            } catch (Exception ee) {
            }
        }
    }

    /**
     * @test java.sql.Connection#commit()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "commit", args = {  })
    public void testCommit() {
        Statement st = null;
        Statement st1 = null;
        Statement st2 = null;
        Statement st3 = null;
        Statement st4 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (3, 'Vorobey', 'sparrow');");
            st.execute("insert into zoo (id, name, family) values (4, 'Orel', 'eagle');");
            st1 = conn.createStatement();
            st1.execute("select * from zoo");
            rs1 = st1.getResultSet();
            assertEquals(4, getCount(rs1));
            try {
                conn.commit();
                st2 = conn.createStatement();
                st2.execute("select * from zoo");
                rs2 = st2.getResultSet();
                assertEquals(4, getCount(rs2));
            } catch (SQLException e) {
                fail("SQLException is thrown: " + e.toString());
            } finally {
                try {
                    rs2.close();
                    st2.close();
                } catch (SQLException ee) {
                }
            }
            try {
                st3 = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
                st3.execute("select * from zoo");
                rs3 = st3.getResultSet();
                conn.commit();
                assertEquals(4, getCount(rs3));
            } catch (SQLException e) {
                fail("SQLException is thrown: " + e.toString());
            } finally {
                try {
                    if (rs3 != null) rs3.close();
                    if (st3 != null) st3.close();
                } catch (SQLException ee) {
                }
            }
        } catch (SQLException sqle) {
            fail("SQLException was thrown: " + sqle.toString());
        } finally {
            try {
                rs1.close();
                st.close();
                st1.close();
            } catch (Exception ee) {
            }
        }
    }

    /**
     * @throws SQLException 
     * @test java.sql.Connection#rollback()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "rollback", args = {  })
    public void testRollback() throws SQLException {
        Statement st = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (3, 'Vorobey', 'sparrow');");
            st.execute("insert into zoo (id, name, family) values (4, 'Orel', 'eagle');");
            conn.rollback();
            st1 = conn.createStatement();
            st1.execute("select * from zoo");
            rs1 = st1.getResultSet();
            assertEquals("Rollback was ineffective", 2, getCount(rs1));
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        } finally {
            conn.setAutoCommit(true);
            try {
                st.close();
                st1.close();
                rs1.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            st.execute("insert into zoo (id, name, family) values (3, 'Vorobey', 'sparrow');");
            st.execute("insert into zoo (id, name, family) values (4, 'Orel', 'eagle');");
            if (!conn.getAutoCommit()) {
                st1 = conn.createStatement();
                st1.execute("select * from zoo");
                rs1 = st1.getResultSet();
                assertEquals(4, getCount(rs1));
                Statement st2 = null;
                Statement st3 = null;
                try {
                    conn.commit();
                    st2 = conn.createStatement();
                    st2.execute("select * from zoo");
                    rs2 = st2.getResultSet();
                    assertEquals(4, getCount(rs2));
                    conn.rollback();
                    st3 = conn.createStatement();
                    st3.execute("select * from zoo");
                    rs3 = st3.getResultSet();
                    assertEquals(4, getCount(rs3));
                } catch (SQLException e) {
                    fail("SQLException is thrown: " + e.toString());
                } finally {
                    conn.setAutoCommit(true);
                    try {
                        rs2.close();
                        rs3.close();
                        st2.close();
                        st3.close();
                    } catch (SQLException ee) {
                    }
                }
            } else {
                fail("Error in test setup: cannot turn autocommit off.");
            }
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        } finally {
            try {
                st.close();
                st1.close();
                rs1.close();
            } catch (SQLException ee) {
            }
        }
        conn.close();
        try {
            conn.rollback();
            fail("SQLException expected");
        } catch (SQLException e) {
        }
    }

    /**
     * @test java.sql.Connection#setSavepoint()
     * 
     * TODO setSavepoint is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "setSavepoint", args = {  })
    public void testSetSavepoint() {
        try {
            conn.setAutoCommit(false);
            try {
                Savepoint sp = conn.setSavepoint();
            } catch (SQLException e) {
            }
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        }
    }

    /**
     * @test java.sql.Connection#setSavepoint(String name)
     * 
     * TODO setSavepoint is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "setSavepoint", args = { java.lang.String.class })
    public void testSetSavepoint_String() {
        String testSavepoint = "testSavepoint";
        try {
            conn.setAutoCommit(false);
            try {
                Savepoint sp = conn.setSavepoint(testSavepoint);
            } catch (SQLException e) {
            }
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        }
    }

    /**
     * @test java.sql.Connection#rollback(Savepoint savepoint)
     * 
     * TODO Savepoint is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "rollback", args = { java.sql.Savepoint.class })
    public void testRollback_Savepoint() {
        Savepoint sp = new DummySavePoint();
        try {
            conn.setAutoCommit(false);
            try {
                conn.rollback(sp);
            } catch (SQLException e) {
            }
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        }
    }

    /**
     * @test java.sql.Connection#releaseSavepoint(Savepoint savepoint)
     * 
     * TODO Savepoint is not supported
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "releaseSavepoint", args = { java.sql.Savepoint.class })
    public void testReleaseSavepoint_Savepoint() {
        Savepoint sp = new DummySavePoint();
        try {
            conn.setAutoCommit(false);
            try {
                conn.releaseSavepoint(sp);
            } catch (SQLException e) {
            }
        } catch (SQLException sqle) {
            fail("SQLException is thrown: " + sqle.toString());
        }
    }

    /**
     * @test java.sql.Connection#prepareStatement(String sql, int[]
     *       columnIndexes)
     *       
     * TODO prepareStatement(String sql, int[] columnIndexes) is not
     * supported      
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "prepareStatement", args = { java.lang.String.class, int[].class })
    public void testPrepareStatement_String_intArray() {
        PreparedStatement prst = null;
        try {
            String insert = "insert into zoo (id, name, family) values (?, ?, ?);";
            prst = conn.prepareStatement(insert, new int[] { 0, 1, 2 });
        } catch (SQLException e) {
        } finally {
            try {
                prst.close();
            } catch (Exception ee) {
            }
        }
    }

    /**
     * @test java.sql.Connection#prepareStatement(String sql, int resultSetType,
     *       int resultSetConcurrency)
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "not fully supported", method = "prepareStatement", args = { java.lang.String.class, int.class, int.class })
    public void testPrepareStatement_String_int_int() {
        String query = "insert into zoo (id, name, family) values (?, ?, ?);";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            st.execute("select id, name from zoo");
            rs = st.getResultSet();
            try {
                rs.deleteRow();
                fail("Can delete row for READ_ONLY ResultSet");
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            st = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            st.execute("select name, family from zoo");
            rs = st.getResultSet();
            try {
                rs.insertRow();
                rs.updateObject("family", "bird");
                rs.next();
                rs.previous();
                assertEquals("bird", (rs.getString(1)));
            } catch (SQLException sqle) {
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, -1);
        } catch (SQLException sqle) {
        }
        try {
            conn.prepareStatement(query, Integer.MIN_VALUE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException sqle) {
        }
    }

    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "not supported options: ResultSet.TYPE_SCROLL_INSENSITIVE," + "ResultSet.CONCUR_UPDATABLE", method = "prepareStatement", args = { java.lang.String.class, int.class, int.class })
    @KnownFailure("not supported")
    public void testPrepareStatementNotSupported() {
        String query = "insert into zoo (id, name, family) values (?, ?, ?);";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            st.execute("select name, family from zoo");
            rs = st.getResultSet();
            try {
                rs.insertRow();
                rs.updateObject("family", "bird");
                rs.next();
                rs.previous();
                assertEquals("parrot", (rs.getString(1)));
            } catch (SQLException sqle) {
                fail("Got Exception " + sqle.getMessage());
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException ee) {
            }
        }
    }

    @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Not fully implemented: ResultSet.CLOSE_CURSORS_AT_COMMIT not supported", method = "prepareStatement", args = { java.lang.String.class, int.class, int.class, int.class })
    public void testPrepareStatement_String_int_int_int() {
        String query = "insert into zoo (id, name, family) values (?, ?, ?);";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            st.setInt(1, 3);
            st.setString(2, "Petya");
            st.setString(3, "Cock");
            st.execute("select id, name from zoo");
            rs = st.getResultSet();
            try {
                rs.close();
            } catch (SQLException sqle) {
                fail("Unexpected exception was thrown during closing ResultSet");
            }
        } catch (SQLException e) {
            fail("SQLException was thrown: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException ee) {
            }
        }
        try {
            conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, -100);
            fail("SQLException was not thrown");
        } catch (SQLException sqle) {
        }
    }

    /**
     * @test java.sql.Connection#prepareStatement(String sql, String[]
     *       columnNames)
     *       
     * TODO prepareStatement(String sql, String[] columnNames) method is
     * not supported      
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "prepareStatement", args = { java.lang.String.class, java.lang.String[].class })
    public void testPrepareStatement_String_StringArray() {
        PreparedStatement prst = null;
        PreparedStatement prst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        try {
            String insert = "insert into zoo (id, name, family) values (?, ?, ?);";
            conn.prepareStatement(insert, new String[] { "id", "name", "family" });
        } catch (SQLException e) {
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported: it should release all resources but it doesn't", method = "close", args = {  })
    public void testClose() {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
            assertTrue(conn.isClosed());
            try {
                conn.prepareCall("select * from zoo");
                fail("Should not be able to prepare query closed connection");
            } catch (SQLException e) {
            }
        } catch (SQLException e) {
            fail("Error in implementation");
            e.printStackTrace();
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "not supported", method = "isClosed", args = {  })
    public void testIsClosed() throws Exception {
        assertFalse(conn.isClosed());
        conn.close();
        assertTrue(conn.isClosed());
        conn = DriverManager.getConnection("jdbc:sqlite:/" + dbFile.getPath());
        assertFalse(conn.isClosed());
        Statement st = conn.createStatement();
        st.execute("select * from zoo");
    }

    private static class DummySavePoint implements Savepoint {

        public int getSavepointId() {
            return 0;
        }

        public String getSavepointName() {
            return "NoName";
        }
    }
}
