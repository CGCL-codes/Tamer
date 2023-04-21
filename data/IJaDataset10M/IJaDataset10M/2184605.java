package test.lib.db.entity;

import lablog.lib.db.conn.DBConnection;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PersonTest.class, RoleTest.class, GroupTest.class, ProjectTest.class, DirectoryTest.class, PropertySectionTest.class })
public class AllEntityTests {

    /**
    * @throws java.lang.Exception
    */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        if (!DBConnection.instance().isConnected()) DBConnection.instance().configure("localhost", 3306, "lablog_dev", "lablog", "lablog", "mysql_dev");
    }
}
