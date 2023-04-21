package testsuite.perf;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import testsuite.BaseTestCase;

/**
 * Simple performance testing unit test.
 * 
 * @author Mark Matthews
 */
public class LoadStorePerfTest extends BasePerfTest {

    /** The table type to use (only for MySQL), 'HEAP' by default */
    private String tableType = "HEAP";

    private boolean takeMeasurements = false;

    private boolean useColumnNames = false;

    private boolean largeResults = false;

    /**
	 * Constructor for LoadStorePerfTest.
	 * 
	 * @param name
	 *            the name of the test to run
	 */
    public LoadStorePerfTest(String name) {
        super(name);
        String newTableType = System.getProperty("com.mysql.jdbc.test.tabletype");
        this.largeResults = "TRUE".equalsIgnoreCase(System.getProperty("com.mysql.jdbc.testsuite.loadstoreperf.useBigResults"));
        if ((newTableType != null) && (newTableType.length() > 0)) {
            this.tableType = newTableType;
            System.out.println("Using specified table type of '" + this.tableType + "'");
        }
    }

    /**
	 * Runs all tests in this test case
	 * 
	 * @param args
	 *            ignored
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
    public static void main(String[] args) throws Exception {
        new LoadStorePerfTest("test1000Transactions").run();
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    public void setUp() throws Exception {
        super.setUp();
        try {
            this.stmt.executeUpdate("DROP TABLE perfLoadStore");
        } catch (SQLException sqlEx) {
        }
        String dateTimeType = "DATETIME";
        if (BaseTestCase.dbUrl.indexOf("oracle") != -1) {
            dateTimeType = "TIMESTAMP";
        }
        String query = "CREATE TABLE perfLoadStore (priKey INT NOT NULL, " + "fk1 INT NOT NULL, " + "fk2 INT NOT NULL, " + "dtField " + dateTimeType + ", " + "charField1 CHAR(32), " + "charField2 CHAR(32), " + "charField3 CHAR(32), " + "charField4 CHAR(32), " + "intField1 INT, " + "intField2 INT, " + "intField3 INT, " + "intField4 INT, " + "doubleField1 DECIMAL," + "doubleField2 DOUBLE," + "doubleField3 DOUBLE," + "doubleField4 DOUBLE," + "PRIMARY KEY (priKey))";
        if (BaseTestCase.dbUrl.indexOf("mysql") != -1) {
            query += (" TYPE=" + this.tableType);
        }
        this.stmt.executeUpdate(query);
        String currentDateValue = "NOW()";
        if (BaseTestCase.dbUrl.indexOf("sqlserver") != -1) {
            currentDateValue = "GETDATE()";
        }
        if (BaseTestCase.dbUrl.indexOf("oracle") != -1) {
            currentDateValue = "CURRENT_TIMESTAMP";
        }
        int numLoops = 1;
        if (this.largeResults) {
            numLoops = 32;
        }
        System.out.println("Inserting " + numLoops + " rows to retrieve...");
        for (int i = 0; i < numLoops; i++) {
            this.stmt.executeUpdate("INSERT INTO perfLoadStore (" + "priKey, " + "fk1, " + "fk2, " + "dtField, " + "charField1, " + "charField2, " + "charField3, " + "charField4, " + "intField1, " + "intField2, " + "intField3, " + "intField4, " + "doubleField1," + "doubleField2," + "doubleField3," + "doubleField4" + ") VALUES (" + i + "," + "2," + "3," + currentDateValue + "," + "'0123456789ABCDEF0123456789ABCDEF'," + "'0123456789ABCDEF0123456789ABCDEF'," + "'0123456789ABCDEF0123456789ABCDEF'," + "'0123456789ABCDEF0123456789ABCDEF'," + "7," + "8," + "9," + "10," + "1.20," + "2.30," + "3.40," + "4.50" + ")");
        }
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    public void tearDown() throws Exception {
        try {
            this.stmt.executeUpdate("DROP TABLE perfLoadStore");
        } catch (SQLException sqlEx) {
        }
        super.tearDown();
    }

    /**
	 * Tests and times 1000 load/store type transactions
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
    public void test1000Transactions() throws Exception {
        this.takeMeasurements = false;
        warmUp();
        this.takeMeasurements = true;
        doIterations(29);
        reportResults("\n\nResults for instance # 1: ");
    }

    /**
	 * Runs one iteration of the test.
	 * 
	 * @see testsuite.perf.BasePerfTest#doOneIteration()
	 */
    protected void doOneIteration() throws Exception {
        PreparedStatement pStmtStore = this.conn.prepareStatement("UPDATE perfLoadStore SET " + "priKey = ?, " + "fk1 = ?, " + "fk2 = ?, " + "dtField = ?, " + "charField1 = ?, " + "charField2 = ?, " + "charField3 = ?, " + "charField4 = ?, " + "intField1 = ?, " + "intField2 = ?, " + "intField3 = ?, " + "intField4 = ?, " + "doubleField1 = ?," + "doubleField2 = ?," + "doubleField3 = ?," + "doubleField4 = ?" + " WHERE priKey=?");
        PreparedStatement pStmtCheck = this.conn.prepareStatement("SELECT COUNT(*) FROM perfLoadStore WHERE priKey=?");
        PreparedStatement pStmtLoad = null;
        if (this.largeResults) {
            pStmtLoad = this.conn.prepareStatement("SELECT " + "priKey, " + "fk1, " + "fk2, " + "dtField, " + "charField1, " + "charField2, " + "charField3, " + "charField4, " + "intField1, " + "intField2, " + "intField3, " + "intField4, " + "doubleField1," + "doubleField2, " + "doubleField3," + "doubleField4" + " FROM perfLoadStore");
        } else {
            pStmtLoad = this.conn.prepareStatement("SELECT " + "priKey, " + "fk1, " + "fk2, " + "dtField, " + "charField1, " + "charField2, " + "charField3, " + "charField4, " + "intField1, " + "intField2, " + "intField3, " + "intField4, " + "doubleField1," + "doubleField2, " + "doubleField3," + "doubleField4" + " FROM perfLoadStore WHERE priKey=?");
        }
        NumberFormat numFormatter = NumberFormat.getInstance();
        numFormatter.setMaximumFractionDigits(4);
        numFormatter.setMinimumFractionDigits(4);
        int transactionCount = 5000;
        if (this.largeResults) {
            transactionCount = 50;
        }
        long begin = System.currentTimeMillis();
        for (int i = 0; i < transactionCount; i++) {
            this.conn.setAutoCommit(false);
            pStmtCheck.setInt(1, 1);
            this.rs = pStmtCheck.executeQuery();
            while (this.rs.next()) {
                this.rs.getInt(1);
            }
            this.rs.close();
            if (!this.largeResults) {
                pStmtLoad.setInt(1, 1);
            }
            this.rs = pStmtLoad.executeQuery();
            if (this.rs.next()) {
                int key = this.rs.getInt(1);
                if (!this.useColumnNames) {
                    pStmtStore.setInt(1, key);
                    pStmtStore.setInt(2, this.rs.getInt(2));
                    pStmtStore.setInt(3, this.rs.getInt(3));
                    pStmtStore.setTimestamp(4, this.rs.getTimestamp(4));
                    pStmtStore.setString(5, this.rs.getString(5));
                    pStmtStore.setString(6, this.rs.getString(7));
                    pStmtStore.setString(7, this.rs.getString(7));
                    pStmtStore.setString(8, this.rs.getString(8));
                    pStmtStore.setInt(9, this.rs.getInt(9));
                    pStmtStore.setInt(10, this.rs.getInt(10));
                    pStmtStore.setInt(11, this.rs.getInt(11));
                    pStmtStore.setInt(12, this.rs.getInt(12));
                    pStmtStore.setDouble(13, this.rs.getDouble(13));
                    pStmtStore.setDouble(14, this.rs.getDouble(14));
                    pStmtStore.setDouble(15, this.rs.getDouble(15));
                    pStmtStore.setDouble(16, this.rs.getDouble(16));
                    pStmtStore.setInt(17, key);
                } else {
                    pStmtStore.setInt(1, key);
                    pStmtStore.setInt(2, this.rs.getInt("fk1"));
                    pStmtStore.setInt(3, this.rs.getInt("fk2"));
                    pStmtStore.setTimestamp(4, this.rs.getTimestamp("dtField"));
                    pStmtStore.setString(5, this.rs.getString("charField1"));
                    pStmtStore.setString(6, this.rs.getString("charField2"));
                    pStmtStore.setString(7, this.rs.getString("charField3"));
                    pStmtStore.setString(8, this.rs.getString("charField4"));
                    pStmtStore.setInt(9, this.rs.getInt("intField1"));
                    pStmtStore.setInt(10, this.rs.getInt("intField2"));
                    pStmtStore.setInt(11, this.rs.getInt("intField3"));
                    pStmtStore.setInt(12, this.rs.getInt("intField4"));
                    pStmtStore.setDouble(13, this.rs.getDouble("doubleField1"));
                    pStmtStore.setDouble(14, this.rs.getDouble("doubleField2"));
                    pStmtStore.setDouble(15, this.rs.getDouble("doubleField3"));
                    pStmtStore.setDouble(16, this.rs.getDouble("doubleField4"));
                    pStmtStore.setInt(17, key);
                }
                pStmtStore.executeUpdate();
            }
            this.rs.close();
            this.conn.commit();
            this.conn.setAutoCommit(true);
        }
        pStmtStore.close();
        pStmtCheck.close();
        pStmtLoad.close();
        long end = System.currentTimeMillis();
        long timeElapsed = (end - begin);
        double timeElapsedSeconds = (double) timeElapsed / 1000;
        double tps = transactionCount / timeElapsedSeconds;
        if (this.takeMeasurements) {
            addResult(tps);
            System.out.print("1 [ " + numFormatter.format(getMeanValue()) + " ] ");
        } else {
            System.out.println("Warm-up: " + tps + " trans/sec");
        }
    }

    /**
	 * Runs the test 10 times to get JIT going, and GC going
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
    protected void warmUp() throws Exception {
        try {
            System.out.print("Warm-up period (10 iterations)");
            for (int i = 0; i < 10; i++) {
                doOneIteration();
                System.out.print(".");
            }
            System.out.println();
            System.out.println("Warm-up period ends");
            System.out.println("\nUnits for this test are transactions/sec.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
