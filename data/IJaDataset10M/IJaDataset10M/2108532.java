package net.admin4j.jdbc.driver;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import junit.framework.Assert;
import net.admin4j.deps.commons.lang3.JavaVersion;
import net.admin4j.deps.commons.lang3.SystemUtils;
import net.admin4j.jdbc.driver.sql.ConnectionWrapper30Base;
import net.admin4j.vo.DataMeasurementSummaryVO;
import net.admin4j.vo.SqlStatementPerformanceSummaryVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAdmin4jJdbcDriverPerformance {

    private static final String DROP_FOO_SQL = "drop table foo";

    private static final String CREATE_FOO_SQL = "create table foo (fooId integer)";

    private static final String CALL_TEST_SQL = "call CURRENT_USER";

    private static final String SELECT_FOO_SQL = "select * from foo";

    private static final String INSERT_FOO_SQL = "insert into foo values (?)";

    private Connection nativeConnection;

    private Connection admin4JConnection;

    @Before
    public void setUp() throws Exception {
        Driver admin4JDriver = null;
        if (SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_7)) {
            admin4JDriver = (Driver) Class.forName("net.admin4j.jdbc.driver.Admin4jJdbcDriverJdk7").newInstance();
        } else {
            admin4JDriver = (Driver) Class.forName("net.admin4j.jdbc.driver.Admin4jJdbcDriverJdk5").newInstance();
        }
        org.hsqldb.jdbcDriver nativeDriver = new org.hsqldb.jdbcDriver();
        Properties props = new Properties();
        props.put("user", "sa");
        props.put("password", "");
        admin4JConnection = admin4JDriver.connect("jdbcx:admin4j:driver=org.hsqldb.jdbcDriver,poolName=mainPoolDB::jdbc:hsqldb:mem:Admin4JTestDb", new Properties());
        nativeConnection = nativeDriver.connect("jdbc:hsqldb:mem:Admin4JTestDb", new Properties());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNativeDriver() throws Exception {
        timeSql("Native driver", nativeConnection, false);
    }

    @Test
    public void testAdmin4JDriver() throws Exception {
        DriverContextRegistry.setTrackExecutionStacks(((ConnectionWrapper30Base) admin4JConnection).getDriverContext(), true);
        timeSql("Admin4J driver - with stack trace", admin4JConnection, true);
    }

    @Test
    public void testAdmin4JDriver2() throws Exception {
        DriverContextRegistry.setTrackExecutionStacks(((ConnectionWrapper30Base) admin4JConnection).getDriverContext(), false);
        timeSql("Admin4J driver - without stack trace", admin4JConnection, true);
    }

    private void timeSql(String label, Connection conn, boolean testOutput) throws SQLException {
        long beginTime;
        Statement stmt = conn.createStatement();
        stmt.execute(CREATE_FOO_SQL);
        stmt.close();
        beginTime = System.currentTimeMillis();
        performInserts(conn);
        long elapsedTime = System.currentTimeMillis() - beginTime;
        System.out.println(label + ", 10000 inserts.  time=" + elapsedTime);
        beginTime = System.currentTimeMillis();
        performSelects(conn);
        elapsedTime = System.currentTimeMillis() - beginTime;
        System.out.println(label + ", 10000 selects and fetches.  time=" + elapsedTime);
        beginTime = System.currentTimeMillis();
        performCalls(conn);
        elapsedTime = System.currentTimeMillis() - beginTime;
        System.out.println(label + ", 10000 callable statements.  time=" + elapsedTime);
        stmt = conn.createStatement();
        stmt.execute(DROP_FOO_SQL);
        stmt.close();
        conn.close();
        Map<String, Set<DataMeasurementSummaryVO>> map = SqlStatementTimerFactory.getDataSummaryMap();
        System.out.println("Nbr of Sql Statements: " + map.keySet().size());
        System.out.println("Sql Statements: " + map.keySet());
        if (!testOutput) {
            return;
        }
        boolean createRecorded = false;
        boolean dropRecorded = false;
        boolean insertRecorded = false;
        boolean selectRecorded = false;
        boolean callRecorded = false;
        SqlStatementPerformanceSummaryVO stmtSummary;
        Set<DataMeasurementSummaryVO> set;
        for (String key : map.keySet()) {
            System.out.println("Map key:" + key);
            set = map.get(key);
            for (DataMeasurementSummaryVO summary : set) {
                System.out.println("Map set:" + set);
                stmtSummary = new SqlStatementPerformanceSummaryVO(summary);
                System.out.println("Map summary:" + stmtSummary);
                if (CALL_TEST_SQL.equals(stmtSummary.getSqlText())) {
                    Assert.assertTrue(stmtSummary.getSummary().getNbrDataItems() == 10000 || stmtSummary.getSummary().getNbrDataItems() == 20000);
                    callRecorded = true;
                } else if (INSERT_FOO_SQL.equals(stmtSummary.getSqlText())) {
                    Assert.assertTrue(stmtSummary.getSummary().getNbrDataItems() == 10000 || stmtSummary.getSummary().getNbrDataItems() == 20000);
                    insertRecorded = true;
                } else if (SELECT_FOO_SQL.equals(stmtSummary.getSqlText())) {
                    Assert.assertTrue(stmtSummary.getSummary().getNbrDataItems() == 10000 || stmtSummary.getSummary().getNbrDataItems() == 20000);
                    selectRecorded = true;
                } else if (CREATE_FOO_SQL.equals(stmtSummary.getSqlText())) {
                    Assert.assertTrue(stmtSummary.getSummary().getNbrDataItems() == 1 || stmtSummary.getSummary().getNbrDataItems() == 2);
                    createRecorded = true;
                } else if (DROP_FOO_SQL.equals(stmtSummary.getSqlText())) {
                    Assert.assertTrue(stmtSummary.getSummary().getNbrDataItems() == 1 || stmtSummary.getSummary().getNbrDataItems() == 2);
                    dropRecorded = true;
                } else {
                    Assert.fail("SQL Text not recognized=" + stmtSummary.getSqlText());
                }
            }
        }
        Assert.assertTrue(createRecorded);
        Assert.assertTrue(dropRecorded);
        Assert.assertTrue(insertRecorded);
        Assert.assertTrue(selectRecorded);
        Assert.assertTrue(callRecorded);
    }

    private void performCalls(Connection conn) throws SQLException {
        ResultSet rSet;
        CallableStatement cstmt;
        String tempStr;
        for (int i = 0; i < 10000; i++) {
            cstmt = conn.prepareCall(CALL_TEST_SQL);
            rSet = cstmt.executeQuery();
            if (rSet.next()) {
                tempStr = rSet.getString(1);
                if (tempStr == null) {
                    System.out.println("Hey");
                }
            }
            rSet.close();
        }
    }

    private void performSelects(Connection conn) throws SQLException {
        PreparedStatement pStmt;
        ResultSet rSet;
        Integer tempInt;
        for (int i = 0; i < 10000; i++) {
            pStmt = conn.prepareStatement(SELECT_FOO_SQL);
            rSet = pStmt.executeQuery();
            while (rSet.next()) {
                tempInt = rSet.getInt(1);
            }
            rSet.close();
            pStmt.close();
        }
    }

    private void performInserts(Connection conn) throws SQLException {
        PreparedStatement pStmt;
        for (int i = 0; i < 10000; i++) {
            pStmt = conn.prepareStatement(INSERT_FOO_SQL);
            pStmt.setInt(1, i);
            pStmt.executeUpdate();
            conn.commit();
            pStmt.close();
        }
    }
}
