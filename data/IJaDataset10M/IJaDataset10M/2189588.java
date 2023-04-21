package uk.org.ogsadai.types;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.types.SQLDateTimeTypesTestBase;

/**
 * Test class for SQL Server dates, times and timestamps. It creates a table
 * with various data then runs tuple-related tests on this. It
 * allows us to identify any drivers or databases that give
 * rise to problematic date, time or timestamp types.
 * This class could easily be extended to handle checks for
 * other types. This class expects  
 * test properties to be provided in a file whose location is
 * specified in a system property,
 * <code>ogsadai.test.properties</code>. The following properties need
 * to be provided:
 * <ul>
 * <li>
 * <code>jdbc.connection.url</code> - URL of the relational database
 * exposed by the above resource. 
 * </li>
 * <li>
 * <code>jdbc.driver.class</code> - JDBC driver class name.
 * </li>
 * <li>
 * <code>jdbc.user.name</code> - user name for above URL.
 * </li>
 * <li>
 * <code>jdbc.password</code> - password for above user name.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class SQLServerSQLDateTimeTypesTestCase extends SQLDateTimeTypesTestBase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** SQL Server table create statement. */
    private static final String CREATE_SQL = "CREATE TABLE daidatetest (id INTEGER, somedate DATETIME, sometime DATETIME, sometimestamp DATETIME)";

    /**
     * Constructor.
     *
     * @param name
     *     Test case name.
     * @throws Exception
     *     If any problems arise in reading the test properties.
     */
    public SQLServerSQLDateTimeTypesTestCase(final String name) throws Exception {
        super(name);
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *     Ignored.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(SQLServerSQLDateTimeTypesTestCase.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateTable(PreparedStatement statement) throws SQLException {
        statement.setInt(1, 1);
        statement.setDate(2, null);
        statement.setTime(3, null);
        statement.setTimestamp(4, null);
        statement.execute();
        statement.setInt(1, 2);
        statement.setDate(2, Date.valueOf("1963-11-22"));
        statement.setTime(3, Time.valueOf("06:15:00"));
        statement.setTimestamp(4, Timestamp.valueOf("1970-01-01 12:00:00"));
        statement.execute();
        statement.setInt(1, 3);
        statement.setDate(2, Date.valueOf("1970-01-01"));
        statement.setTime(3, Time.valueOf("12:00:00"));
        statement.setTimestamp(4, Timestamp.valueOf("1970-01-01 12:00:00"));
        statement.execute();
        statement.setInt(1, 4);
        statement.setDate(2, Date.valueOf("2009-05-11"));
        statement.setTime(3, Time.valueOf("23:59:59"));
        statement.setTimestamp(4, Timestamp.valueOf("2009-05-11 23:59:59"));
        statement.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreateSQL() {
        return CREATE_SQL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getTupleTypes() {
        return new int[] { TupleTypes._INT, TupleTypes._TIMESTAMP, TupleTypes._TIMESTAMP, TupleTypes._TIMESTAMP };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?>[] getColumnClasses() {
        return new Class[] { null, Timestamp.class, Timestamp.class, Timestamp.class };
    }
}
