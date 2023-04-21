package com.entelience.test.test14sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.sql.Db;
import com.entelience.sql.DbConnection;
import com.entelience.sql.DbHelper;

/**
 * Test for database connection encapsulation.
 *
 * Test various transactional things.  Its important that
 * test cases follow in sequence...
 */
public class test07ConnectionEncapsulation extends junit.framework.TestCase {

    public class OurException extends Exception {

        public OurException(String message) {
            super(message);
        }
    }

    @Test
    public void test_create_test_table() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), false, false);
        try {
            try {
                db.executeSql("DROP TABLE test_bug_1952;");
            } catch (SQLException e) {
                assertTrue(true);
            }
            db.executeSql("CREATE TABLE test_bug_1952 (key TEXT, value TEXT);");
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_table_created() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), true, false);
        try {
            PreparedStatement ps = db.prepareStatement("SELECT key, value FROM test_bug_1952;");
            ResultSet rs = db.executeQuery(ps);
            assertTrue(!rs.next());
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_good_write_data() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), false, true);
        try {
            db.begin();
            db.executeSql("INSERT INTO test_bug_1952 (key, value) VALUES ('key1', 'value1');");
            assertTrue(help_check(db, "1"));
            db.commit();
        } finally {
            db.safeClose();
        }
    }

    private void help_write(Db db, String append, boolean throwSpannerInWorks) throws Exception {
        try {
            db.enter();
            PreparedStatement ps = db.prepareStatement("INSERT INTO test_bug_1952 (key, value) VALUES (?, ?);");
            ps.setString(1, "key" + append);
            ps.setString(2, "value" + append);
            db.executeUpdate(ps);
            if (throwSpannerInWorks) throw new OurException("This is the proverbial spanner.");
        } finally {
            db.exit();
        }
    }

    private boolean help_check(Db db, String append) throws Exception {
        try {
            db.enter();
            PreparedStatement ps = db.prepareStatement("SELECT COUNT(*) FROM test_bug_1952 WHERE key = ?;");
            ps.setString(1, "key" + append);
            ResultSet rs = db.executeQuery(ps);
            if (!rs.next()) {
                assertTrue(false);
            }
            int n = rs.getInt(1);
            if (rs.next()) {
                assertTrue(false);
            }
            return n == 1;
        } finally {
            db.exit();
        }
    }

    @Test
    public void test_good_write_data_with_helper() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), false, true);
        try {
            db.begin();
            help_write(db, "2", false);
            assertTrue(help_check(db, "2"));
            db.commit();
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_bad_write_on_readonly() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), true, false);
        try {
            help_write(db, "3", false);
            assertTrue(false);
        } catch (IllegalStateException e) {
            assertTrue(true);
            assertFalse(help_check(db, "3"));
        } catch (SQLException e) {
            assertTrue(true);
            assertFalse(help_check(db, "3"));
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_bad_write_with_helper() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), false, true);
        try {
            db.begin();
            help_write(db, "4", true);
            assertTrue(false);
            db.commit();
        } catch (OurException e) {
            assertTrue(true);
            assertTrue(help_check(db, "4"));
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_good_bad_write_with_helper_disabled_txns() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), false, true);
        db.disableTx();
        try {
            db.begin();
            help_write(db, "5", true);
            assertTrue(false);
            db.commit();
        } catch (OurException e) {
            assertTrue(true);
            assertTrue(help_check(db, "5"));
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_rollback() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), false, true);
        try {
            db.begin();
            help_write(db, "6", false);
            assertTrue(help_check(db, "6"));
            db.rollback();
            db.begin();
            assertFalse(help_check(db, "6"));
        } catch (OurException e) {
            assertTrue(true);
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_very_bad_write_on_readonly() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), true, false);
        try {
            db.executeSql("INSERT INTO test_bug_1952 (key, value) VALUES ('key7', 'value7');");
            assertTrue(false);
        } catch (SQLException e) {
            assertTrue(true);
            assertFalse(help_check(db, "7"));
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_doublecheck() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), true, false);
        try {
            String goodKeyList = "('key1', 'key2', 'key5')";
            String badKeyList = "('key3', 'key4', 'key6', 'key7')";
            PreparedStatement ps_good_ok = db.prepareStatement("SELECT COUNT(*) FROM test_bug_1952 WHERE key IN " + goodKeyList);
            PreparedStatement ps_good_ko = db.prepareStatement("SELECT COUNT(*) FROM test_bug_1952 WHERE key NOT IN " + goodKeyList);
            PreparedStatement ps_bad = db.prepareStatement("SELECT COUNT(*) FROM test_bug_1952 WHERE key IN " + badKeyList);
            Integer good_ok = DbHelper.getKey(ps_good_ok);
            Integer good_ko = DbHelper.getKey(ps_good_ko);
            Integer bad = DbHelper.getKey(ps_bad);
            assertTrue(good_ok.intValue() == 3);
            assertTrue(good_ko.intValue() == 0);
            assertTrue(bad.intValue() == 0);
        } finally {
            db.safeClose();
        }
    }

    @Test
    public void test_drop_test_table() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), false, false);
        try {
            db.executeSql("DROP TABLE test_bug_1952;");
        } finally {
            db.safeClose();
        }
    }
}
