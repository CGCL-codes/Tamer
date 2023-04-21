package it.unibo.lmc.pjdbc.statement;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.PropertyConfigurator;

public class testUpdateOverMeta extends TestCase {

    private static Connection conn = null;

    private static Statement stmt = null;

    /**
	 * Costruttore 
	 * @param testName
	 */
    public testUpdateOverMeta(String testName) {
        super(testName);
    }

    /**
	 * Setup
	 */
    protected void setUp() throws Exception {
        Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
        if (null == conn) {
            conn = DriverManager.getConnection("jdbc:prolog:target/classes/database/catalog1:prolog1");
            stmt = conn.createStatement();
        }
        super.setUp();
    }

    /**
	 * @return the suite of tests being tested
	 */
    public static Test suite() {
        Properties properties = new Properties();
        String userDir = System.getProperty("user.dir");
        File propFile = new File(userDir + "/target/classes/common.properties");
        if (propFile.exists()) {
            try {
                properties.load(new FileInputStream(propFile));
            } catch (Exception e) {
                System.out.println("><" + e.getLocalizedMessage());
            }
        }
        PropertyConfigurator.configure(properties);
        TestSuite ts = new TestSuite();
        ts.addTest(new testUpdateOverMeta("testDropTable"));
        ts.addTest(new testUpdateOverMeta("testDeleteRowTable"));
        ts.addTest(new testUpdateOverMeta("testUpdateRowTable"));
        return ts;
    }

    /**
	 * TEST: Select di un campo specifico
	 */
    public void testDropTable() {
        System.out.println(" ====================== ");
        System.out.println("  testDropTable         ");
        System.out.println(" ====================== ");
        try {
            int n = stmt.executeUpdate("drop table prolog1.employee;");
            assertEquals(4, n);
        } catch (Exception e) {
            e.printStackTrace();
            fail(" ExecuteQuery ha ritornato: " + e);
        }
        assertTrue(true);
    }

    /**
	 * TEST: Select di un campo specifico
	 */
    public void testDeleteRowTable() {
        System.out.println(" ====================== ");
        System.out.println("  testDeleteRowTable    ");
        System.out.println(" ====================== ");
        try {
            int n = stmt.executeUpdate("delete from prolog1.dept where dept.id = 1;");
            assertEquals(1, n);
        } catch (Exception e) {
            e.printStackTrace();
            fail(" ExecuteQuery ha ritornato: " + e);
        }
        assertTrue(true);
    }

    /**
	 * TEST: Select di un campo specifico
	 */
    public void testDeleteAllRowTable() {
        System.out.println(" ====================== ");
        System.out.println("  testDeleteAllRowTable ");
        System.out.println(" ====================== ");
        try {
            int n = stmt.executeUpdate("delete from prolog1.dept;");
            assertEquals(3, n);
        } catch (Exception e) {
            e.printStackTrace();
            fail(" ExecuteQuery ha ritornato: " + e);
        }
        assertTrue(true);
    }

    /**
	 * TEST: Select di un campo specifico
	 */
    public void testUpdateRowTable() {
        System.out.println(" ====================== ");
        System.out.println("  testUpdateRowTable    ");
        System.out.println(" ====================== ");
        try {
            int n = stmt.executeUpdate("update prolog1.age SET $1 = 5 WHERE $0 = 'smith' ;");
            assertEquals(1, n);
        } catch (Exception e) {
            e.printStackTrace();
            fail(" ExecuteQuery ha ritornato: " + e);
        }
        assertTrue(true);
    }
}
